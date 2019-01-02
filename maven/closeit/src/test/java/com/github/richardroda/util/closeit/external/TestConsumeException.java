/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.richardroda.util.closeit.external;

import com.github.richardroda.util.closeit.CloseIt0;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author Richard
 */
public class TestConsumeException extends BaseTest {
    
    boolean consumed;
    @BeforeTest public void resetConsumed() {
        consumed = false;
    }
    
    protected void exConsumer(Throwable ex) {
        consumed = true;
    }
        
    @Test 
    public void consumeCheckedException() {
        try(CloseIt0 it = CloseIt0.consumeException(this::closeThrowChecked, this::exConsumer)) {
            
        }
        Assert.assertTrue(consumed);
    }
    
    @Test(expectedExceptions = ArithmeticException.class) 
    public void consumeUnCheckedException() {
        try(CloseIt0 it = CloseIt0.consumeException(this::closeThrowUnChecked, this::exConsumer)) {
            
        }
    }
    
    @Test(expectedExceptions = AssertionError.class) 
    public void consumeError() {
        try(CloseIt0 it = CloseIt0.consumeException(this::closeThrowError, this::exConsumer)) {
            
        }
    }
    
// ***************************************************************************************
    
    @Test 
    public void consumeAllCheckedException() {
        try(CloseIt0 it = CloseIt0.consumeAllException(this::closeThrowChecked, this::exConsumer)) {
            
        }
        Assert.assertTrue(consumed);
    }
    
    @Test 
    public void consumeAllUnCheckedException() {
        try(CloseIt0 it = CloseIt0.consumeAllException(this::closeThrowUnChecked, this::exConsumer)) {
            
        }
        Assert.assertTrue(consumed);
    }
    
    @Test(expectedExceptions = AssertionError.class) 
    public void consumeAllError() {
        try(CloseIt0 it = CloseIt0.consumeAllException(this::closeThrowError, this::exConsumer)) {
            
        }
    }
    
// ***************************************************************************************
    
    @Test 
    public void consumeThrowableCheckedException() {
        try(CloseIt0 it = CloseIt0.consumeAllThrowable(this::closeThrowChecked, this::exConsumer)) {
            
        }
        Assert.assertTrue(consumed);
    }
    
    @Test 
    public void consumeThrowableUnCheckedException() {
        try(CloseIt0 it = CloseIt0.consumeAllThrowable(this::closeThrowUnChecked, this::exConsumer)) {
            
        }
        Assert.assertTrue(consumed);
    }
    
    @Test 
    public void consumeThrowableError() {
        try(CloseIt0 it = CloseIt0.consumeAllThrowable(this::closeThrowError, this::exConsumer)) {
            
        }
        Assert.assertTrue(consumed);
    }
    
// ***************************************************************************************
}
