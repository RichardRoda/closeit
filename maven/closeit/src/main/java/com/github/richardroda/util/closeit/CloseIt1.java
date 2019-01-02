package com.github.richardroda.util.closeit;

import java.util.Objects;
import java.util.function.Function;

/**
 * Functional Interface to allow try-with-resources to be used with any lambda
 * expression that throws a single checked exception.  It also has static utility
 * methods to convert an {@code AutoCloseable} into this interface by
 * specifying how exceptions should be wrapped into the specified checked exception.
 * The static utility methods use the <a href="https://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>
 * to enhance the provided {@code AutoClosable} with the wrapping behavior that
 * occurs when the {@code close()} method throws an exception.
 *
 * @author Richard Roda
 */
@FunctionalInterface
public interface CloseIt1<E extends Exception> extends AutoCloseable {

    @Override
    default void close() throws E {
        closeIt();
    }

    void closeIt() throws E;

    /**
     * Create a {@code CloseIt1} from a {@link AutoCloseable} by converting any
     * checked exception thrown and converting it to the exception specified by
     * the type variable of the {@code CloseIt1} interface using the supplied
     * {@code exceptionMapper} {@link Function}.  Unchecked exceptions are
     * not processed.
     *
     * @param autoCloseable AutoCloseable object or lambda.
     * @param exceptionMapper Function to map an exception to the exception type
     * specified by the {@code CloseIt1} interface.
     * @return A {@code CloseIt1} that uses the specified
     * {@code exceptionMapper} to map any exceptions to the exception type
     * specified by the {@code CloseIt1} interface.
     * @see CloseIt0#wrapException(java.lang.AutoCloseable)
     */
    public static <E extends Exception> CloseIt1<E> wrapException(AutoCloseable autoCloseable,
            Function<? super Exception, ? extends E> exceptionMapper) {
        Objects.requireNonNull(autoCloseable, "autoCloseable required");
        Objects.requireNonNull(exceptionMapper, "exceptionMapper required");

        return () -> {
            try {
                autoCloseable.close();
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                E rex = exceptionMapper.apply(ex);
                if (rex != null) {
                    throw rex;
                }
            }
        };
    }

    /**
     * Create a {@code CloseIt1} from a {@link AutoCloseable} by converting any
     * exception thrown and converting it to the exception specified by the type
     * variable of the {@code CloseIt1} interface using the supplied
     * {@code exceptionMapper} {@link Function}.
     *
     * @param autoCloseable AutoCloseable object or lambda.
     * @param exceptionMapper Function to map an exception to the exception type
     * specified by the {@code CloseIt1} interface.
     * @return A {@code CloseIt1} that uses the specified
     * {@code exceptionMapper} to map any exceptions to the exception type
     * specified by the {@code CloseIt1} interface.
     * @see CloseIt0#wrapAllException(java.lang.AutoCloseable)
     */
    public static <E extends Exception> CloseIt1<E> wrapAllException(AutoCloseable autoCloseable,
            Function<? super Exception, ? extends E> exceptionMapper) {
        Objects.requireNonNull(autoCloseable, "autoCloseable required");
        Objects.requireNonNull(exceptionMapper, "exceptionMapper required");

        return () -> {
            try {
                autoCloseable.close();
            } catch (Exception ex) {
                E rex = exceptionMapper.apply(ex);
                if (rex != null) {
                    throw rex;
                }
            }
        };
    }

    /**
     * Create a {@code CloseIt1} from a {@link AutoCloseable} by converting any
     * throwable thrown and converting it to the exception specified by the type
     * variable of the {@code CloseIt1} interface using the supplied
     * {@code exceptionMapper} {@link Function}.
     *
     * @param autoCloseable AutoCloseable object or lambda.
     * @param exceptionMapper Function to map a throwable to the exception type
     * specified by the {@code CloseIt1} interface.
     * @return A {@code CloseIt1} that uses the specified
     * {@code exceptionMapper} to map any throwables to the exception type
     * specified by the {@code CloseIt1} interface.
     * @see CloseIt0#wrapAllThrowable(java.lang.AutoCloseable)
     */
    public static <E extends Exception> CloseIt1<E> wrapAllThrowable(AutoCloseable autoCloseable,
            Function<? super Throwable, ? extends E> exceptionMapper) {
        Objects.requireNonNull(autoCloseable, "autoCloseable required");
        Objects.requireNonNull(exceptionMapper, "exceptionMapper required");

        return () -> {
            try {
                autoCloseable.close();
            } catch (Throwable ex) {
                E rex = exceptionMapper.apply(ex);
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
