/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.richardroda.util.closeit;

/**
 *
 * @author Richard
 */
class CloseItHelper {
    static <T extends Throwable> RuntimeException hideException(Throwable th, Class<T> clazz) throws T {
        throw (T)th;
    }
    
    static RuntimeException hideException(Throwable th) {
        return hideException(th, RuntimeException.class);
    }
    
}
