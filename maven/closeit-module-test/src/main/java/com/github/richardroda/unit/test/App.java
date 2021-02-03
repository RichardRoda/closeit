package com.github.richardroda.unit.test;

import com.github.richardroda.util.closeit.CloseIt0;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args )
    {
        try(CloseIt0 cl = App::close) {
            System.out.println("Hello World!");
        }
    }

    public static void close() {
        System.out.println("Goodbye World!");
    }
}
