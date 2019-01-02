/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.richardroda.util.closeit.external;

import com.github.richardroda.util.closeit.CloseIt1;
import org.testng.annotations.Test;

/**
 *
 * @author Richard
 */
public class TestCloseIt1ExceptionWrapping extends BaseTest {
    
    @Test(expectedExceptions = MyCheckedAppException.class)
    public void testChecked() throws MyCheckedAppException {
        try (CloseIt1<MyCheckedAppException> it = CloseIt1.wrapException(this::closeThrowChecked, MyCheckedAppException::new)) {
            
        }
    }
    
    @Test(expectedExceptions = ArithmeticException.class)
    public void testUnchecked() throws MyCheckedAppException {
        try (CloseIt1<MyCheckedAppException> it = CloseIt1.wrapException(this::closeThrowUnChecked, MyCheckedAppException::new)) {
            
        }
    }
    
    @Test(expectedExceptions = AssertionError.class)
    public void testError() throws MyCheckedAppException {
        try (CloseIt1<MyCheckedAppException> it = CloseIt1.wrapException(this::closeThrowError, MyCheckedAppException::new)) {
            
        }
    }
    
    @Test(expectedExceptions = MyCheckedAppException.class)
    public void testAllChecked() throws MyCheckedAppException {
        try (CloseIt1<MyCheckedAppException> it = CloseIt1.wrapAllException(this::closeThrowChecked, MyCheckedAppException::new)) {
            
        }
    }
    
    @Test(expectedExceptions = MyCheckedAppException.class)
    public void testAllUnchecked() throws MyCheckedAppException {
        try (CloseIt1<MyCheckedAppException> it = CloseIt1.wrapAllException(this::closeThrowUnChecked, MyCheckedAppException::new)) {
            
        }
    }
    
    @Test(expectedExceptions = AssertionError.class)
    public void testAllError() throws MyCheckedAppException {
        try (CloseIt1<MyCheckedAppException> it = CloseIt1.wrapAllException(this::closeThrowError, MyCheckedAppException::new)) {
            
        }
    }
    
    @Test(expectedExceptions = MyCheckedAppException.class)
    public void testThrowableChecked() throws MyCheckedAppException {
        try (CloseIt1<MyCheckedAppException> it = CloseIt1.wrapAllThrowable(this::closeThrowChecked, MyCheckedAppException::new)) {
            
        }
    }
    
    @Test(expectedExceptions = MyCheckedAppException.class)
    public void testThrowableUnchecked() throws MyCheckedAppException {
        try (CloseIt1<MyCheckedAppException> it = CloseIt1.wrapAllThrowable(this::closeThrowUnChecked, MyCheckedAppException::new)) {
            
        }
    }
    
    @Test(expectedExceptions = MyCheckedAppException.class)
    public void testThrowableError() throws MyCheckedAppException {
        try (CloseIt1<MyCheckedAppException> it = CloseIt1.wrapAllThrowable(this::closeThrowError, MyCheckedAppException::new)) {
            
        }
    }
    
    
}
