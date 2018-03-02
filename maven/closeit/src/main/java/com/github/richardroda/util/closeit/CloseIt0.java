package com.github.richardroda.util.closeit;

import java.util.Objects;
import java.util.function.Function;

/**
 * Functional Interface to allow a lambda that throws no checked exceptions to
 * be used with a try-with-resources construct.  Also has static utility
 * methods to convert an {@code AutoCloseable} into this interface by
 * specifying how checked exceptions should be mapped.
 *
 * @author Richard Roda
 */
@FunctionalInterface
public interface CloseIt0 extends AutoCloseable {

    @Override
    public void close();

    /**
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which throws
     * no checked exceptions.  Any checked exception is wrapped within an
     * {@link IllegalStateException}.
     * @param autoCloseable An autoCloseable object or lambda.
     * @return A {@code CloseIt0} which wraps any checked exceptions in
     * a {@code IllegalStateException}.
     * @see #toCloseIt0(java.lang.AutoCloseable, java.util.function.Function) 
     */
    public static CloseIt0 wrapException(AutoCloseable autoCloseable) {
        return toCloseIt0(autoCloseable, IllegalStateException::new);
    }

    /**
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which hides
     * any checked exceptions from the compiler.  The checked exception is
     * still thrown, but no compilation error occurs if it is uncaught and not
     * included in the caller's throws clause.  This should be used with great
     * care, as it violates the normal Java {@code throws} contract.  Since
     * calling code will not be expecting a checked exception, unpredictable
     * results may occur.  Consider using {@link #wrapException(java.lang.AutoCloseable) } 
     * which wraps any checked exceptions within an {@link IllegalStateException}.
     * 
     * @param autoCloseable An autoCloseable object or lambda.
     * @return A {@code CloseIt0} which hides any checked exceptions from the
     * compiler.
     * @see #toCloseIt0(java.lang.AutoCloseable, java.util.function.Function) 
     */
    public static CloseIt0 hideException(AutoCloseable autoCloseable) {
        return toCloseIt0(autoCloseable, CloseItHelper::hideException);
    }
    
    /**
     * Create a {@link CloseIt0} from an {@link AutoCloseable} that
     * uses a {@link Function} to map any checked exceptions to
     * a {@link RuntimeException}.
     * @param autoCloseable AutoCloseable object or lambda.
     * @param exceptionMapper Function to map a checked exception to
     * an unchecked exception.  Only called when the exception is checked.  A 
     * {@code null} return value, means to swallow (ignore) the exception, so 
     * {@code ex->null } would ignore (not throw) all checked exceptions (not 
     * recommended).  Another example: using {@code IllegalStateException::new}
     * will cause any checked exception to be wrapped within an 
     * {@link IllegalStateException}.  The {@link #wrapException(java.lang.AutoCloseable) }
     * method does exactly that to produce its return value.
     * 
     * @return A {@code CloseIt0} which uses an {@code exceptionMapper} to
     * map any checked exceptions to unchecked exceptions.
     */
    public static CloseIt0 toCloseIt0(AutoCloseable autoCloseable,
             Function<? super Exception, ? extends RuntimeException> exceptionMapper) {
        Objects.requireNonNull(autoCloseable, "autoCloseable required");
        Objects.requireNonNull(exceptionMapper, "exceptionMapper required");

        return () -> {
            try {
                autoCloseable.close();
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                RuntimeException rex = exceptionMapper.apply(ex);
                if (rex != null) {
                    throw rex;
                }
            }
        };
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
