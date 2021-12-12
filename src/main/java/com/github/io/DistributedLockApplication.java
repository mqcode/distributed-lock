package com.github.io;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class DistributedLockApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(DistributedLockApplication.class,args);
        System.out.println( "Hello World!" );
    }
}
