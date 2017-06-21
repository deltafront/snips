package snips.listener;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

public class VoidMessageProcessorTest
{
    private final String filename = "test.text";
    private final Integer corePoolSize = 10;
    private final Integer maximumPoolSize = 15;

    @Test
    public void happyPathTestSingle(){
        testHappyPath(1);
    }

    @Test
    public void happyPathCorePoolSize(){
        testHappyPath(corePoolSize);
    }

    @Test
    public void happyPathMaximumPoolSize(){
        testHappyPath(maximumPoolSize);
    }

    @Test
    public void happyPathMaxPoolSizePlus(){
        testHappyPath(maximumPoolSize * 2);
    }
    private void testHappyPath(Integer maxCount){
        final AmazonSQS sqs = mockSqs(maxCount);
        final VoidMessageProcessorFactory voidMessageProcessorFactory = constructFactory();
        final VoidMessageProcessor voidMessageProcessor = voidMessageProcessorFactory.voidMessageProcessor(sqs, 100L);
        final Consumer<Message>messageConsumer = messageConsumer();
        voidMessageProcessor.process("blah",messageConsumer);
        IntStream.range(0,maxCount).forEach(index ->{
            final String filename = getFilename(index);
            final File file = new File(filename);
            assertTrue(String.format("File '%s' does not exist!", filename), file.exists());
        });
    }


    private VoidMessageProcessorFactory constructFactory(){
        final Long keepAliveTime = 10L;
        final String timeUnit = TimeUnit.MINUTES.name();
        return new VoidMessageProcessorFactory(corePoolSize,maximumPoolSize,keepAliveTime,timeUnit);
    }

    private AmazonSQS mockSqs(Integer count){
        final AmazonSQS sqs = createMock(AmazonSQS.class);
        expect(sqs.receiveMessage(anyString())).andReturn(mockReceiveMessageResult(count)).times(count);
        replay(sqs);
        return sqs;
    }

    private ReceiveMessageResult mockReceiveMessageResult(Integer count){
       final ReceiveMessageResult receiveMessageResult = createMock(ReceiveMessageResult.class);
        expect(receiveMessageResult.getMessages()).andReturn(mockMessages(count)).times(count);
        replay(receiveMessageResult);
        return receiveMessageResult;
    }

    private List<Message> mockMessages(Integer count){
        final List<Message>messages = new LinkedList<>();
        IntStream.range(0,count).forEach(index->messages.add(mockMessage(index)));
        return messages;
    }

    private Message mockMessage(Integer count){
        final Message message = createMock(Message.class);
        expect(message.getBody()).andReturn(getFilename(count));
        replay(message);
        return message;
    }

    private String getFilename(Integer count){
        return String.format("%s%d",filename,count);
    }
    private Consumer<Message> messageConsumer()
    {
        return message -> {
            final String filename = message.getBody();
           try{
               File file = new File(filename);
               Writer writer = new FileWriter(file);
               writer.write("this is some text");
               file.deleteOnExit();
           }
           catch (IOException e){
               e.printStackTrace();
           }
        };
    }
}
