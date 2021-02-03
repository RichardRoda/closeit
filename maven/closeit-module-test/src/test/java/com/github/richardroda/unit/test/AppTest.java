package com.github.richardroda.unit.test;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.github.richardroda.util.closeit.CloseIt0;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    boolean closed;

    @BeforeMethod
    public void beforeTest() {
        closed = false;
    }

    @Test
    public void testApp()
    {
        App.main(new String[]{});
    }

    @Test
    public void testClose() {
        try(CloseIt0 cl = this::close) {
            Assert.assertFalse(closed);
        }
        Assert.assertTrue(closed);
    }

    protected void close() {
        closed = true;
    }
}
