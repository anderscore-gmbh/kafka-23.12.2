package com.anderscore;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        new HelloConsumer().start();
        new HelloProducer().produce();
    }
}
