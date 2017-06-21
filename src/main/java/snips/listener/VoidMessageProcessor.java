package snips.listener;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

public class VoidMessageProcessor
{
    private final ThreadPoolExecutor executorService;
    private final AmazonSQS sqs;
    private final Long sleepMillis;
    private final Logger LOGGER = LoggerFactory.getLogger(VoidMessageProcessor.class);


    VoidMessageProcessor(ThreadPoolExecutor executorService,AmazonSQS sqs, Long sleepMillis){
        this.executorService = executorService;
        this.sqs = sqs;
        this.sleepMillis = sleepMillis;
    }


    public void process(String queueUrl, Consumer<Message> consumer){
        final Boolean hasAvailableThreads = hasAvailableThreads();
        if(hasAvailableThreads){
            final List<Message>messages = getMessages(queueUrl);
            if(messages.size() > 0){
                processMessages(messages, consumer);
            }
            else{
                doSleepAndProcessDueToNoMessagesBeingOnTheQueue(queueUrl, consumer);
            }
        }
        else {
            doSleepAndProcessDueToNoThreadsBeingAvailable(queueUrl, consumer);
        }
    }


    private void processMessages(List<Message>messages, Consumer<Message> consumer){
        messages.forEach(message -> executorService.submit(() -> processMessage(message, consumer)));
    }

    private void processMessage(Message message, Consumer<Message> consumer){
        consumer.accept(message);
    }

    private List<Message>getMessages(String queueUrl){
        final List<Message>messages = new LinkedList<>();
        messages.addAll(sqs.receiveMessage(queueUrl).getMessages());
        LOGGER.info("Received {} messages from SQS Queue {}", messages.size(), queueUrl);
        return messages;
    }

    private Boolean hasAvailableThreads(){
        return executorService.getActiveCount() == 0;
    }

    private void doSleepAndProcessDueToNoThreadsBeingAvailable(String queueUrl, Consumer<Message> consumer){
        doSleepAndProcess("No available threads.", queueUrl, consumer);
    }

    private void doSleepAndProcessDueToNoMessagesBeingOnTheQueue(String queueUrl, Consumer<Message>consumer){
        doSleepAndProcess("No messages on the Queue.", queueUrl, consumer);
    }

    private void doSleepAndProcess(String message, String queueUrl, Consumer<Message>consumer){
        doSleep(message);
        process(queueUrl,consumer);
    }

    private void doSleep(String message){
        final String logMessage  = String.format("%s Sleeping for {} milliseconds.", message);
        LOGGER.debug(logMessage,sleepMillis);
        try{
            Thread.sleep(sleepMillis);
        }
        catch (InterruptedException e){
            LOGGER.error(e.getMessage(),e);
        }
    }
}
