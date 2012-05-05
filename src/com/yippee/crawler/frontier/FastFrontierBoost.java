package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * An implementation of a really fast crawler!
 */
public class FastFrontierBoost implements URLFrontier{
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(FastFrontierBoost.class);
    private final int MAX_SIZE = 10000;
    /**
     * The queue containing the current url-frontier
     */
    private BlockingQueue<String> current = new ArrayBlockingQueue<String>(MAX_SIZE);
    /**
     *
     */
    private BlockingQueue<String> seen = new ArrayBlockingQueue<String>(MAX_SIZE);

    /**
     * Pulls the url from the shared queue.
     *
     * @return a Message object containing the link information
     * @throws InterruptedException
     */
    public synchronized Message pull() throws InterruptedException {
        int queueSize = current.size();
        if (queueSize>1000){
            logger.info(queueSize);
        }
        String url = current.take();
        logger.info("PULL: " + url);
        return new Message(url);
    }

    public synchronized void push(Message message) {

        String url = message.getURL().toString();
        logger.info(">>>>>>>>TRY: " + url);
        if (seen.contains(url)) {
            logger.info("seen!");
            return;
        }

        int queueSize = current.size();
        if ((queueSize>1000) && (queueSize < MAX_SIZE-1)){
            logger.info(queueSize);
        } else if (queueSize == MAX_SIZE -1) {
            logger.info("QUEUE IS FULL: Discarding url:" + url);
            return;
        }
        logger.info("PUSH: " + url);
        current.add(url);
        seen.add(url);
        return;
    }

    public synchronized boolean isSeen(String url){
        if (seen.contains(url)) {
            logger.info("Seen " + url);
            return true;
        } else return false;
    }

    public boolean save() {
        return false;
    }

    public boolean load() {
        return false;
    }

}
