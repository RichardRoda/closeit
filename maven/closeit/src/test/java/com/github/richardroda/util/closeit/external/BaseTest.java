/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.richardroda.util.closeit.external;

import java.util.logging.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 *
 * @author Richard
 */
public class BaseTest {

    final Logger log = Logger.getLogger(getClass().getName());

    protected boolean isClosed = false;
    
    /**
     * Reset isClosed to false before each test is run.
     */
    @BeforeMethod public void resetIsClosed() {
        isClosed = false;
    }

    /**
     * Verify isClosed is true at the conclusion of each test, to verify
     * that the specified close method was called.
     */
    @AfterMethod public void checkIsClosed() {
        Assert.assertTrue(isClosed);
    }
    
    public void closeThrowChecked() throws CloneNotSupportedException {
        isClosed = true;
        throw new CloneNotSupportedException();
    }

    public void closeThrowError() throws AssertionError {
        isClosed = true;
        throw new AssertionError();
    }

    public void closeThrowUnChecked() throws ArithmeticException {
        isClosed = true;
        throw new ArithmeticException();
    }
    
}
