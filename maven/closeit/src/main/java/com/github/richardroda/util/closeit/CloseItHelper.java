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
