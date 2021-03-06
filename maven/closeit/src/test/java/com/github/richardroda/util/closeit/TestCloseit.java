package com.github.richardroda.util.closeit;

import com.github.richardroda.util.closeit.external.BaseTest;
import java.util.EnumSet;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This tests the compilation of the code to verify that each CloseIt
 * interface allows the correct exceptions, and if the specified close method is
 * successfully called.  These tests use checked exceptions defined in java.lang
 * to avoid using any dependencies that may not be available when run under
 * Java9 due to the removal of some APIs.
 * 
 * @author Richard Roda
 */
public class TestCloseit extends BaseTest {

    /**
     * Enum to represent each close method that is called.
     */
    enum CloseMethodsTested {
        CLOSE, CLOSE1, CLOSE2, CLOSE3, CLOSE4, CLOSE5
    }
    
    /**
     * Set to track the calling of each close method.
     */
    EnumSet<CloseMethodsTested> closeMethodsCalled = EnumSet.noneOf(CloseMethodsTested.class);
    
    /**
     * Before running test methods in this class, reset the close methods called.
     */
    @BeforeClass public void resetCloseMethods() {
        closeMethodsCalled = EnumSet.noneOf(CloseMethodsTested.class);
    }
    
    /**
     * After running test methods in this class, make sure all the close methods
     * are tested.
     */
    @AfterClass public void checkCloseMethodsTested() {
        Assert.assertEquals(closeMethodsCalled.size(), CloseMethodsTested.values().length);
    }
    
    @Test
    public void testCloseIt() {
        try(CloseIt0 it = this::close) {            
        }
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCloseItEx() {
        try(CloseIt0 it = this::close) {
            throw new IllegalArgumentException();
        }
    }
    
    @Test
    public void testCloseIt1() throws CloneNotSupportedException {
        try(CloseIt1<CloneNotSupportedException> it = this::close1) {            
        }
    }
    
    @Test(expectedExceptions = CloneNotSupportedException.class)
    public void testCloseIt1Ex() throws CloneNotSupportedException {
        try(CloseIt1<CloneNotSupportedException> it = this::close1) {
            throw new CloneNotSupportedException();
        }
    }
    
    @Test
    public void testCloseIt2() throws CloneNotSupportedException, InstantiationException {
        try(CloseIt2<CloneNotSupportedException, InstantiationException> it = this::close2) {            
        }
    }
    
    @Test(expectedExceptions = CloneNotSupportedException.class)
    public void testCloseIt2Ex() throws CloneNotSupportedException, InstantiationException {
        try(CloseIt2<CloneNotSupportedException, InstantiationException> it = this::close2) {
            throw new CloneNotSupportedException();
        }
    }
    
    @Test
    public void testCloseIt3() throws CloneNotSupportedException, InstantiationException, InterruptedException {
        try(CloseIt3<CloneNotSupportedException, InstantiationException
                , InterruptedException> it = this::close3) {            
        }
    }
    
    @Test(expectedExceptions = CloneNotSupportedException.class)
    public void testCloseIt3Ex() throws CloneNotSupportedException, InstantiationException, InterruptedException {
        try(CloseIt3<CloneNotSupportedException, InstantiationException, InterruptedException> it = this::close3) {
            throw new CloneNotSupportedException();
        }
    }
    
    @Test
    public void testCloseIt4() throws CloneNotSupportedException, InstantiationException, InterruptedException, NoSuchFieldException {
        try(CloseIt4<CloneNotSupportedException, InstantiationException
                , InterruptedException, NoSuchFieldException> it = this::close4) {            
        }
    }
    
    @Test(expectedExceptions = CloneNotSupportedException.class)
    public void testCloseIt4Ex() throws CloneNotSupportedException, InstantiationException, InterruptedException, NoSuchFieldException {
        try(CloseIt4<CloneNotSupportedException, InstantiationException
                , InterruptedException, NoSuchFieldException> it = this::close4) {
            throw new CloneNotSupportedException();
        }
    }
    
    @Test
    public void testCloseIt5() throws CloneNotSupportedException, InstantiationException, InterruptedException, NoSuchFieldException, NoSuchMethodException {
        try(CloseIt5<CloneNotSupportedException, InstantiationException
                , InterruptedException, NoSuchFieldException, NoSuchMethodException> it = this::close5) {            
        }
    }
    
    @Test(expectedExceptions = CloneNotSupportedException.class)
    public void testCloseIt5Ex() throws CloneNotSupportedException, InstantiationException, InterruptedException, NoSuchFieldException, NoSuchMethodException {
        try(CloseIt5<CloneNotSupportedException, InstantiationException
                , InterruptedException, NoSuchFieldException, NoSuchMethodException> it = this::close5) {
            throw new CloneNotSupportedException();
        }
    }
    
    @Test(expectedExceptions = CloneNotSupportedException.class)
    public void testHideException() {
        try(CloseIt0 it = CloseIt0.hideException(this::closeThrowChecked)) {
        }        
    }
    
    @Test(expectedExceptions = NotClosedException.class)
    public void testWrapException() {
        try(CloseIt0 it = CloseIt0.wrapException(this::closeThrowChecked)) {
        }        
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapExceptionSuppressed() {
        try(CloseIt0 it = CloseIt0.wrapException(this::closeThrowChecked)) {
            throw new IllegalArgumentException();
        }        
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void testWrapExceptionUnchecked() {
        try(CloseIt0 it = CloseIt0.wrapException(this::closeThrowUnChecked)) {
        }        
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testWrapExceptionError() {
        try(CloseIt0 it = CloseIt0.wrapException(this::closeThrowError)) {
        }        
    }

    @Test(expectedExceptions = NotClosedException.class)
    public void testWrapAllException() {
        try(CloseIt0 it = CloseIt0.wrapAllException(this::closeThrowChecked)) {
        }        
    }

    @Test(expectedExceptions = NotClosedException.class)
    public void testWrapAllExceptionUnchecked() {
        try(CloseIt0 it = CloseIt0.wrapAllException(this::closeThrowUnChecked)) {
        }        
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testWrapAllExceptionError() {
        try(CloseIt0 it = CloseIt0.wrapAllException(this::closeThrowError)) {
        }        
    }

    @Test(expectedExceptions = NotClosedException.class)
    public void testWrapAllThrowable() {
        try(CloseIt0 it = CloseIt0.wrapAllThrowable(this::closeThrowChecked)) {
        }        
    }

    @Test(expectedExceptions = NotClosedException.class)
    public void testWrapAllThrowableUnchecked() {
        try(CloseIt0 it = CloseIt0.wrapAllThrowable(this::closeThrowUnChecked)) {
        }        
    }

    @Test(expectedExceptions = NotClosedException.class)
    public void testWrapAllThrowableError() {
        try(CloseIt0 it = CloseIt0.wrapAllThrowable(this::closeThrowError)) {
        }        
    }

    @Test
    public void ignoreCheckedException() {
        try(CloseIt0 it = CloseIt0.toCloseIt0(this::closeThrowChecked, ex->null)) {
        }
    }
    
    @Override
     protected void close() {
        super.close();
        closeMethodsCalled.add(CloseMethodsTested.CLOSE);
    }
    protected void close1() throws CloneNotSupportedException {
        isClosed = true;
        closeMethodsCalled.add(CloseMethodsTested.CLOSE1);
    }   
    protected void close2() throws CloneNotSupportedException, 
        InstantiationException { 
        isClosed = true;
        closeMethodsCalled.add(CloseMethodsTested.CLOSE2);
    }
    protected void close3() throws CloneNotSupportedException, 
        InstantiationException, InterruptedException {
        isClosed = true;
        closeMethodsCalled.add(CloseMethodsTested.CLOSE3);
    }
    protected void close4() throws CloneNotSupportedException, 
        InstantiationException, InterruptedException, NoSuchFieldException {
        isClosed = true;
        closeMethodsCalled.add(CloseMethodsTested.CLOSE4);
    }
    protected void close5() throws CloneNotSupportedException, 
        InstantiationException, InterruptedException, NoSuchFieldException,
        NoSuchMethodException{
        isClosed = true;
        closeMethodsCalled.add(CloseMethodsTested.CLOSE5);
    }

}
/*
BSD 2-Clause License

Copyright (c) 2018, Richard Roda
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
