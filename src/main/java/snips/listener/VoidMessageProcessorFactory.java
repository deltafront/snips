package snips.listener;

import com.amazonaws.services.sqs.AmazonSQS;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VoidMessageProcessorFactory
{
    private final ThreadPoolExecutor threadPoolExecutor;

    public VoidMessageProcessorFactory(Integer corePoolSize, Integer maximumPoolSize, Long keepAliveTime, String timeUnit){
        final BlockingQueue<Runnable>blockingQueue= new LinkedBlockingQueue<>();
        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.valueOf(timeUnit),blockingQueue);
    }

    public VoidMessageProcessor voidMessageProcessor(AmazonSQS sqs, Long sleepMillis){
        return new VoidMessageProcessor(threadPoolExecutor,sqs,sleepMillis);
    }
}
