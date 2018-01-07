/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.richardroda.util.closeit;

/**
 * Interface to allow try-with-resources to be used with any lambda expression
 * that throws a two checked exceptions.
 * @author Richard
 */
@FunctionalInterface
public interface CloseIt2<E1 extends Exception, 
        E2 extends Exception> extends AutoCloseable {
    @Override
    default void close() throws E1, E2 {
        closeIt();
    }

    void closeIt() throws E1, E2;
}
