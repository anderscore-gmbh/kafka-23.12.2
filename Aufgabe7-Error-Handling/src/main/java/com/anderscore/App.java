package com.anderscore;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        var consumer = new HelloConsumer();

        // In diesem Beispiel gibt es nur eine Partition - also gibt es nur einen offset-Parameter
        new HelloProducer().produce();

        if(args.length == 1) {
            String consumerOffset = args[0];
            consumer.setOffset(Long.parseLong(consumerOffset));
        }

        new HelloConsumer().start();
    }
}
