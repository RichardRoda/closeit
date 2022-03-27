package com.github.richardroda.util.closeit;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Functional Interface to allow try-with-resources to be used with any lambda
 * expression that throws two checked exceptions.
 * 
 * @param <E1> Checked exception thrown by the close method.
 * @param <E2> Checked exception thrown by the close method.
 * 
 * @author Richard Roda
 */
@FunctionalInterface
public interface CloseIt2<E1 extends Exception, 
        E2 extends Exception> extends CloseIt3<E1, E2, RuntimeException> {

    void closeIt() throws E1, E2;
    
    /**
     * Decorate the lambda with one that processes any {@link Throwable} exception 
     * that is thrown when the {@link #close()} method is called with a
     * predicate and rethrows the exception when the predicate is {@code true}.  If the 
     * exception should always be rethrown, use
     * {@link #rethrow(com.github.richardroda.util.closeit.CloseIt2, java.util.function.Consumer) }.
     * If the exception should never be rethrown, use {@link CloseIt0#consumeAllThrowable(java.lang.AutoCloseable, java.util.function.Consumer)
     * } or {@link CloseIt0#ignoreAllThrowable(java.lang.AutoCloseable) }.
     *
     * @return A decorated lambda that processes any {@link Throwable} that
     * occurs with a predicate that causes the exception to be rethrown when it
     * returns {@code true}, and ignored otherwise.
     * @param <E1> Checked exception thrown by the close method.
     * @param <E2> Checked exception thrown by the close method.
     * @param closeIt The closeIt lambda. Must not be {@code null}.
     * @param when Predicate to process exceptions. Must not be {@code null}.
     * Exception is rethrown when {@code true} is returned, and ignored
     * otherwise.  It is acceptable for the predicate to have side effects
     * like those typically found in a {@link java.util.function.Consumer}.
     * @see #rethrow(com.github.richardroda.util.closeit.CloseIt2,
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
    static <E1 extends Exception, E2 extends Exception>
            CloseIt2<E1, E2> rethrowWhen(CloseIt2<? extends E1, ? extends E2> closeIt, Predicate<? super Throwable> when) {
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
     * @param <E1> Checked exception thrown by the close method.
     * @param <E2> Checked exception thrown by the close method.
     * @param closeIt The closeIt lambda. Must not be {@code null}.
     * @param exConsumer Consumer to process exceptions. Must not be
     * {@code null}.
     * @see #rethrowWhen(com.github.richardroda.util.closeit.CloseIt2, java.util.function.Predicate) 
     */
    static <E1 extends Exception, E2 extends Exception>
            CloseIt2<E1, E2> rethrow(CloseIt2<? extends E1, ? extends E2> closeIt, Consumer<? super Throwable> exConsumer) {
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
