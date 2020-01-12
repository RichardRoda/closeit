/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.richardroda.util.closeit;

import com.github.richardroda.util.closeit.external.BaseTest;
import java.util.function.Function;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author Richard
 */
public class TestCloseItRethrow extends BaseTest {
    
    boolean isHandled;
    boolean exceptionExpected;
    boolean handledExpected;
    
    @BeforeMethod public void clearHandled() {
        isHandled = false;
    }
    
    @AfterMethod public void assertHandeled() {
        Assert.assertEquals(isHandled, handledExpected);
    }

    @Override
    protected void close() {
        exceptionExpected = false;
        handledExpected = false;
        super.close();
    }

    @Override
    protected void closeThrowUnChecked() throws ArithmeticException {
        exceptionExpected = true;
        handledExpected = true;
        super.closeThrowUnChecked();
    }

    @Override
    protected void closeThrowError() throws AssertionError {
        exceptionExpected = true;
        handledExpected = true;
        super.closeThrowError();
    }

    @Override
    protected void closeThrowChecked() throws CloneNotSupportedException {
        exceptionExpected = true;
        handledExpected = true;
        super.closeThrowChecked();
    }
    
    @Test(dataProvider = "closeIt") public void testCloseIt(Function<? super TestCloseItRethrow,? extends AutoCloseable> closableBuilder) {
        boolean exceptionCaught = false;
        try (AutoCloseable it = closableBuilder.apply(this)) {
        } catch (Throwable ex) {
            exceptionCaught = true;
        }
        Assert.assertEquals(exceptionCaught, exceptionExpected);
    }
    
    @DataProvider(name = "closeIt")
    public static Object[][] dataProviderMethod() 
    {
        return new Object[][] { 
            { t0(t -> CloseIt0.rethrow(t::close, t::rethrowConsumer)) }
            , { t0(t -> CloseIt0.rethrowWhen(t::close, t::rethrowTrue)) } 
            , { t0(t -> CloseIt0.rethrowWhen(t::close, t::rethrowFalse)) } 
            , { t0(t -> CloseIt0.rethrow(t::closeThrowUnChecked, t::rethrowConsumer)) }
            , { t0(t -> CloseIt0.rethrowWhen(t::closeThrowUnChecked, t::rethrowTrue)) } 
            , { t0(t -> CloseIt0.rethrowWhen(t::closeThrowUnChecked, t::rethrowFalse)) } 
            , { t0(t -> CloseIt0.rethrow(t::closeThrowError, t::rethrowConsumer)) }
            , { t0(t -> CloseIt0.rethrowWhen(t::closeThrowError, t::rethrowTrue)) } 
            , { t0(t -> CloseIt0.rethrowWhen(t::closeThrowError, t::rethrowFalse)) }

            , { t1(t -> CloseIt1.rethrow(t::close, t::rethrowConsumer)) }
            , { t1(t -> CloseIt1.rethrowWhen(t::close, t::rethrowTrue)) } 
            , { t1(t -> CloseIt1.rethrowWhen(t::close, t::rethrowFalse)) } 
            , { t1(t -> CloseIt1.rethrow(t::closeThrowUnChecked, t::rethrowConsumer)) }
            , { t1(t -> CloseIt1.rethrowWhen(t::closeThrowUnChecked, t::rethrowTrue)) } 
            , { t1(t -> CloseIt1.rethrowWhen(t::closeThrowUnChecked, t::rethrowFalse)) } 
            , { t1(t -> CloseIt1.rethrow(t::closeThrowError, t::rethrowConsumer)) }
            , { t1(t -> CloseIt1.rethrowWhen(t::closeThrowError, t::rethrowTrue)) } 
            , { t1(t -> CloseIt1.rethrowWhen(t::closeThrowError, t::rethrowFalse)) }
            , { t1(t -> CloseIt1.rethrow(t::closeThrowChecked, t::rethrowConsumer)) }
            , { t1(t -> CloseIt1.rethrowWhen(t::closeThrowChecked, t::rethrowTrue)) } 
            , { t1(t -> CloseIt1.rethrowWhen(t::closeThrowChecked, t::rethrowFalse)) }

            , { t2(t -> CloseIt2.rethrow(t::close, t::rethrowConsumer)) }
            , { t2(t -> CloseIt2.rethrowWhen(t::close, t::rethrowTrue)) } 
            , { t2(t -> CloseIt2.rethrowWhen(t::close, t::rethrowFalse)) } 
            , { t2(t -> CloseIt2.rethrow(t::closeThrowUnChecked, t::rethrowConsumer)) }
            , { t2(t -> CloseIt2.rethrowWhen(t::closeThrowUnChecked, t::rethrowTrue)) } 
            , { t2(t -> CloseIt2.rethrowWhen(t::closeThrowUnChecked, t::rethrowFalse)) } 
            , { t2(t -> CloseIt2.rethrow(t::closeThrowError, t::rethrowConsumer)) }
            , { t2(t -> CloseIt2.rethrowWhen(t::closeThrowError, t::rethrowTrue)) } 
            , { t2(t -> CloseIt2.rethrowWhen(t::closeThrowError, t::rethrowFalse)) }
            , { t2(t -> CloseIt2.rethrow(t::closeThrowChecked, t::rethrowConsumer)) }
            , { t2(t -> CloseIt2.rethrowWhen(t::closeThrowChecked, t::rethrowTrue)) } 
            , { t2(t -> CloseIt2.rethrowWhen(t::closeThrowChecked, t::rethrowFalse)) }

            , { t3(t -> CloseIt3.rethrow(t::close, t::rethrowConsumer)) }
            , { t3(t -> CloseIt3.rethrowWhen(t::close, t::rethrowTrue)) } 
            , { t3(t -> CloseIt3.rethrowWhen(t::close, t::rethrowFalse)) } 
            , { t3(t -> CloseIt3.rethrow(t::closeThrowUnChecked, t::rethrowConsumer)) }
            , { t3(t -> CloseIt3.rethrowWhen(t::closeThrowUnChecked, t::rethrowTrue)) } 
            , { t3(t -> CloseIt3.rethrowWhen(t::closeThrowUnChecked, t::rethrowFalse)) } 
            , { t3(t -> CloseIt3.rethrow(t::closeThrowError, t::rethrowConsumer)) }
            , { t3(t -> CloseIt3.rethrowWhen(t::closeThrowError, t::rethrowTrue)) } 
            , { t3(t -> CloseIt3.rethrowWhen(t::closeThrowError, t::rethrowFalse)) }
            , { t3(t -> CloseIt3.rethrow(t::closeThrowChecked, t::rethrowConsumer)) }
            , { t3(t -> CloseIt3.rethrowWhen(t::closeThrowChecked, t::rethrowTrue)) } 
            , { t3(t -> CloseIt3.rethrowWhen(t::closeThrowChecked, t::rethrowFalse)) }

            , { t4(t -> CloseIt4.rethrow(t::close, t::rethrowConsumer)) }
            , { t4(t -> CloseIt4.rethrowWhen(t::close, t::rethrowTrue)) } 
            , { t4(t -> CloseIt4.rethrowWhen(t::close, t::rethrowFalse)) } 
            , { t4(t -> CloseIt4.rethrow(t::closeThrowUnChecked, t::rethrowConsumer)) }
            , { t4(t -> CloseIt4.rethrowWhen(t::closeThrowUnChecked, t::rethrowTrue)) } 
            , { t4(t -> CloseIt4.rethrowWhen(t::closeThrowUnChecked, t::rethrowFalse)) } 
            , { t4(t -> CloseIt4.rethrow(t::closeThrowError, t::rethrowConsumer)) }
            , { t4(t -> CloseIt4.rethrowWhen(t::closeThrowError, t::rethrowTrue)) } 
            , { t4(t -> CloseIt4.rethrowWhen(t::closeThrowError, t::rethrowFalse)) }
            , { t4(t -> CloseIt4.rethrow(t::closeThrowChecked, t::rethrowConsumer)) }
            , { t4(t -> CloseIt4.rethrowWhen(t::closeThrowChecked, t::rethrowTrue)) } 
            , { t4(t -> CloseIt4.rethrowWhen(t::closeThrowChecked, t::rethrowFalse)) }

            , { t5(t -> CloseIt5.rethrow(t::close, t::rethrowConsumer)) }
            , { t5(t -> CloseIt5.rethrowWhen(t::close, t::rethrowTrue)) } 
            , { t5(t -> CloseIt5.rethrowWhen(t::close, t::rethrowFalse)) } 
            , { t5(t -> CloseIt5.rethrow(t::closeThrowUnChecked, t::rethrowConsumer)) }
            , { t5(t -> CloseIt5.rethrowWhen(t::closeThrowUnChecked, t::rethrowTrue)) } 
            , { t5(t -> CloseIt5.rethrowWhen(t::closeThrowUnChecked, t::rethrowFalse)) } 
            , { t5(t -> CloseIt5.rethrow(t::closeThrowError, t::rethrowConsumer)) }
            , { t5(t -> CloseIt5.rethrowWhen(t::closeThrowError, t::rethrowTrue)) } 
            , { t5(t -> CloseIt5.rethrowWhen(t::closeThrowError, t::rethrowFalse)) }
            , { t5(t -> CloseIt5.rethrow(t::closeThrowChecked, t::rethrowConsumer)) }
            , { t5(t -> CloseIt5.rethrowWhen(t::closeThrowChecked, t::rethrowTrue)) } 
            , { t5(t -> CloseIt5.rethrowWhen(t::closeThrowChecked, t::rethrowFalse)) }
        };
    }
    
    protected void rethrowConsumer(Throwable ex) {
        isHandled = true;
    }
    
    protected boolean rethrowTrue(Throwable ex) {
        isHandled = true;
        return true;
    }
    
    protected boolean rethrowFalse(Throwable ex) {
        exceptionExpected = false;
        isHandled = true;
        return false;
    }

    protected static Function<TestCloseItRethrow,CloseIt0> 
        t0(Function<TestCloseItRethrow,CloseIt0> lambda) {
        return lambda;
    }
    
    protected static Function<TestCloseItRethrow,CloseIt1<CloneNotSupportedException>> 
        t1(Function<TestCloseItRethrow,CloseIt1<CloneNotSupportedException>> lambda) {
        return lambda;
    }
    
    protected static Function<TestCloseItRethrow,CloseIt2<CloneNotSupportedException, InstantiationException>> 
        t2(Function<TestCloseItRethrow,CloseIt2<CloneNotSupportedException, InstantiationException>> lambda) {
        return lambda;
    }
    
    protected static Function<TestCloseItRethrow,CloseIt3<CloneNotSupportedException, InstantiationException, InterruptedException>> 
        t3(Function<TestCloseItRethrow,CloseIt3<CloneNotSupportedException, InstantiationException, InterruptedException>> lambda) {
        return lambda;
    }
    
    protected static Function<TestCloseItRethrow,CloseIt4<CloneNotSupportedException, InstantiationException, InterruptedException, NoSuchFieldException>> 
        t4(Function<TestCloseItRethrow,CloseIt4<CloneNotSupportedException, InstantiationException, InterruptedException, NoSuchFieldException>> lambda) {
        return lambda;
    }
    
    protected static Function<TestCloseItRethrow,CloseIt5<CloneNotSupportedException, InstantiationException, InterruptedException, NoSuchFieldException, NoSuchMethodException>> 
        t5(Function<TestCloseItRethrow,CloseIt5<CloneNotSupportedException, InstantiationException, InterruptedException, NoSuchFieldException, NoSuchMethodException>> lambda) {
        return lambda;
    }
}
