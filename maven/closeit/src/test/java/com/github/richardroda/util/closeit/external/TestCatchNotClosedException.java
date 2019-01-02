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
public class TestCatchNotClosedException extends BaseTest {

    
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
            try (CloseIt0 it = CloseIt0.wrapAllThrowable(this::closeThrowError)) {
                
            } catch (NotClosedException ex) {
                log.warning("Exception occurred when closing resource " + ex.getCause());
            }
        }


}
