/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.richardroda.util.closeit.external;

import com.github.richardroda.util.closeit.CloseIt0;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author Richard
 */
public class TestIgnoreException extends BaseTest {
    
    @Test 
    public void ignoreCheckedException() {
        try(CloseIt0 it = CloseIt0.ignoreException(this::closeThrowChecked)) {
            
        }
    }
    
    @Test(expectedExceptions = ArithmeticException.class) 
    public void ignoreUnCheckedException() {
        try(CloseIt0 it = CloseIt0.ignoreException(this::closeThrowUnChecked)) {
            
        }
    }
    
    @Test(expectedExceptions = AssertionError.class) 
    public void ignoreError() {
        try(CloseIt0 it = CloseIt0.ignoreException(this::closeThrowError)) {
            
        }
    }
        
// ***************************************************************************************
    
    @Test 
    public void ignoreAllCheckedException() {
        try(CloseIt0 it = CloseIt0.ignoreAllException(this::closeThrowChecked)) {
            
        }
    }
    
    @Test
    public void ignoreAllUnCheckedException() {
        try(CloseIt0 it = CloseIt0.ignoreAllException(this::closeThrowUnChecked)) {
            
        }
    }
    
    @Test(expectedExceptions = AssertionError.class) 
    public void ignoreAllError() {
        try(CloseIt0 it = CloseIt0.ignoreAllException(this::closeThrowError)) {
            
        }
    }
    
    
// ***************************************************************************************
    
    @Test 
    public void ignoreThrowableCheckedException() {
        try(CloseIt0 it = CloseIt0.ignoreAllThrowable(this::closeThrowChecked)) {
            
        }
    }
    
    @Test
    public void ignoreThrowableUnCheckedException() {
        try(CloseIt0 it = CloseIt0.ignoreAllThrowable(this::closeThrowUnChecked)) {
            
        }
    }
    
    @Test
    public void ignoreThrowableError() {
        try(CloseIt0 it = CloseIt0.ignoreAllThrowable(this::closeThrowError)) {
            
        }
    }    
// ***************************************************************************************
}
