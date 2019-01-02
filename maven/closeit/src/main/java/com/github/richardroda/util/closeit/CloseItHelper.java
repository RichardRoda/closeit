package com.github.richardroda.util.closeit;

/**
 * Helper class for various methods to support the static interface methods.
 * @author Richard
 */
class CloseItHelper {
    static <T extends Throwable> RuntimeException hideException(Throwable th, Class<T> clazz) throws T {
        throw (T)th;
    }

    /**
     * Conceal a runtime exception from the compiler.
     * @param th The exception to conceal.
     * @return Method never returns, but return value is declared to allow it
     * to be used in a functional lambda that expects a runtime exception to
     * be returned.0
     * @throws RuntimeException The actual exception thrown is th, but it is
     * made to appear to the compiler as a runtime exception.
     */
    static RuntimeException hideException(Throwable th) throws RuntimeException {
        return hideException(th, RuntimeException.class);
    }
    
    static void noOp(Throwable th){}
}
