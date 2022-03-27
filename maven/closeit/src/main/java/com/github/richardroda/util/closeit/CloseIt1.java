package com.github.richardroda.util.closeit;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Functional Interface to allow try-with-resources to be used with any lambda
 * expression that throws a single checked exception.  It also has static utility
 * methods to convert an {@code AutoCloseable} into this interface by
 * specifying how exceptions should be wrapped into the specified checked exception.
 * The static utility methods use the <a href="https://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>
 * to enhance the provided {@code AutoClosable} with the wrapping behavior that
 * occurs when the {@code close()} method throws an exception.
 *
 * @param <E> Checked exception thrown by the close method.
 * 
 * @author Richard Roda
 */
@FunctionalInterface
public interface CloseIt1<E extends Exception> extends CloseIt2<E, RuntimeException> {

    void closeIt() throws E;

    /**
     * Create a {@code CloseIt1} from a {@link AutoCloseable} by converting any
     * checked exception thrown and converting it to the exception specified by
     * the type variable of the {@code CloseIt1} interface using the supplied
     * {@code exceptionMapper} {@link Function}.  Unchecked exceptions are
     * not processed.
     *
     * @param <E> Checked exception thrown by the close method.
     * @param autoCloseable AutoCloseable object or lambda.
     * @param exceptionMapper Function to map an exception to the exception type
     * specified by the {@code CloseIt1} interface.
     * @return A {@code CloseIt1} that uses the specified
     * {@code exceptionMapper} to map any exceptions to the exception type
     * specified by the {@code CloseIt1} interface.
     * @see com.github.richardroda.util.closeit.CloseIt0#wrapException(java.lang.AutoCloseable)
     */
    static <E extends Exception> CloseIt1<E> wrapException(AutoCloseable autoCloseable,
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
     * @param <E> Checked exception thrown by the close method.
     * @param autoCloseable AutoCloseable object or lambda.
     * @param exceptionMapper Function to map an exception to the exception type
     * specified by the {@code CloseIt1} interface.
     * @return A {@code CloseIt1} that uses the specified
     * {@code exceptionMapper} to map any exceptions to the exception type
     * specified by the {@code CloseIt1} interface.
     * @see com.github.richardroda.util.closeit.CloseIt0#wrapAllException(java.lang.AutoCloseable)
     */
    static <E extends Exception> CloseIt1<E> wrapAllException(AutoCloseable autoCloseable,
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
     * @param <E> Checked exception thrown by the close method.
     * @param autoCloseable AutoCloseable object or lambda.
     * @param exceptionMapper Function to map a throwable to the exception type
     * specified by the {@code CloseIt1} interface.
     * @return A {@code CloseIt1} that uses the specified
     * {@code exceptionMapper} to map any throwables to the exception type
     * specified by the {@code CloseIt1} interface.
     * @see com.github.richardroda.util.closeit.CloseIt0#wrapAllThrowable(java.lang.AutoCloseable)
     */
    static <E extends Exception> CloseIt1<E> wrapAllThrowable(AutoCloseable autoCloseable,
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

    
    /**
     * Decorate the lambda with one that processes any {@link Throwable} exception 
     * that is thrown when the {@link #close()} method is called with a
     * predicate and rethrows the exception when the predicate is {@code true}.  If the 
     * exception should always be rethrown, use
     * {@link #rethrow(com.github.richardroda.util.closeit.CloseIt1, java.util.function.Consumer) }.
     * If the exception should never be rethrown, use {@link CloseIt0#consumeAllThrowable(java.lang.AutoCloseable, java.util.function.Consumer)
     * } or {@link CloseIt0#ignoreAllThrowable(java.lang.AutoCloseable) }.
     *
     * @return A decorated lambda that processes any {@link Throwable} that
     * occurs with a predicate that causes the exception to be rethrown when it
     * returns {@code true}, and ignored otherwise.
     * @param <E> Checked exception thrown by the close method.
     * @param closeIt The closeIt lambda. Must not be {@code null}.
     * @param when Predicate to process exceptions. Must not be {@code null}.
     * Exception is rethrown when {@code true} is returned, and ignored
     * otherwise.  It is acceptable for the predicate to have side effects
     * like those typically found in a {@link java.util.function.Consumer}.
     * @see #rethrow(com.github.richardroda.util.closeit.CloseIt1,
     * java.util.function.Consumer)
     * @see com.github.richardroda.util.closeit.CloseIt0#consumeAllThrowable(java.lang.AutoCloseable,
     * java.util.function.Consumer)
     * @see com.github.richardroda.util.closeit.CloseIt0#consumeAllException(java.lang.AutoCloseable,
     * java.util.function.Consumer)
     * @see com.github.richardroda.util.closeit.CloseIt0#consumeException(java.lang.AutoCloseable,
     * java.util.function.Consumer)
     * @see com.github.richardroda.util.closeit.CloseIt0#ignoreAllThrowable(java.lang.AutoCloseable) 
     * @see com.github.richardroda.util.closeit.CloseIt0#ignoreAllException(java.lang.AutoCloseable) 
     * @see com.github.richardroda.util.closeit.CloseIt0#ignoreException(java.lang.AutoCloseable) 
     */
    static <E extends Exception>
            CloseIt1<E> rethrowWhen(CloseIt1<? extends E> closeIt, Predicate<? super Throwable> when) {
        Objects.requireNonNull(closeIt, "closeIt required");
        Objects.requireNonNull(when, "when required");
        return () -> {
            try {
                closeIt.close();
            } catch (Throwable th) {
                if (when.test(th)) {
                    throw th;
                }
            }
        };
    }

    /**
     * Decorate the lambda with one that processes any {@link Throwable} exception 
     * that is thrown when the {@link #close()} method is called with a
     * consumer and rethrows the exception.
     *
     * @return A decorated lambda that processes any {@link Throwable} that
     * occurs with a consumer and then rethrows it.
     * @param <E> Checked exception thrown by the close method.
     * @param closeIt The closeIt lambda. Must not be {@code null}.
     * @param exConsumer Consumer to process exceptions. Must not be
     * {@code null}.
     * @see #rethrowWhen(com.github.richardroda.util.closeit.CloseIt1, java.util.function.Predicate) 
     */
    static <E extends Exception>
            CloseIt1<E> rethrow(CloseIt1<? extends E> closeIt, Consumer<? super Throwable> exConsumer) {
        Objects.requireNonNull(closeIt, "closeIt required");
        Objects.requireNonNull(exConsumer, "exConsumer required");
        return () -> {
            try {
                closeIt.close();
            } catch (Throwable th) {
                exConsumer.accept(th);
                throw th;
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
