/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.richardroda.util.closeit.external;

/**
 * Typical application exception class, for testing CloseIt1 exception
 * wrapping.
 * @author Richard
 */
public class MyCheckedAppException extends Exception {

    public MyCheckedAppException() {
    }

    public MyCheckedAppException(String message) {
        super(message);
    }

    public MyCheckedAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyCheckedAppException(Throwable cause) {
        super(cause);
    }
    
}
