package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CounterDemo {
    private static final Logger logger = LoggerFactory.getLogger(CounterDemo.class);

    private String previousFlag = "second";

    public static void main(String[] args) {
        CounterDemo o = new CounterDemo();

        new Thread(() -> o.action(new Counter(), "first")).start();
        new Thread(() -> o.action(new Counter(), "second")).start();
    }

    private synchronized void action(Counter counter, String flag) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (flag.equals(previousFlag)) {
                    wait();
                }

                previousFlag = flag;

                logger.info(String.valueOf(counter.getNextValue()));
                sleep();
                notifyAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}
