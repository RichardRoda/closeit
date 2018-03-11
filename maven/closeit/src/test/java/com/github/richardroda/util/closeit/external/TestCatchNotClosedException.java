/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.richardroda.util.closeit.external;

import com.github.richardroda.util.closeit.CloseIt0;
import com.github.richardroda.util.closeit.NotClosedException;
import java.util.logging.Logger;
import org.testng.annotations.Test;

/**
 *
 * @author Richard
 */
public class TestCatchNotClosedException {

    static final Logger log = Logger.getLogger(TestCatchNotClosedException.class.getName());
    
        @Test
        public void testCatchCloseException() {
            try (CloseIt0 it = CloseIt0.wrapException(this::closeThrowChecked)) {
                
            } catch (NotClosedException ex) {
                log.warning("Exception occurred when closing resource " + ex.getCause());
            }
        }

        @Test
        public void testCatchCloseAllException() {
            try (CloseIt0 it = CloseIt0.wrapAllException(this::closeThrowUnChecked)) {
                
            } catch (NotClosedException ex) {
                log.warning("Exception occurred when closing resource " + ex.getCause());
            }
        }

        @Test
        public void testCatchCloseAllThrowable() {
            try (CloseIt0 it = CloseIt0.wrapAllException(this::closeThrowError)) {
                
            } catch (NotClosedException ex) {
                log.warning("Exception occurred when closing resource " + ex.getCause());
            }
        }

    public void closeThrowChecked() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void closeThrowUnChecked() throws ArithmeticException {
        throw new ArithmeticException();
    }
    
    public void closeThrowError() throws AssertionError {
        throw new AssertionError();
    }

}
