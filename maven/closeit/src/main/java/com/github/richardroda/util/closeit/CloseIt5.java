package com.github.richardroda.util.closeit;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Functional Interface to allow try-with-resources to be used with any lambda
 * expression that throws five checked exceptions.
 *
 * @author Richard Roda
 */
@FunctionalInterface
public interface CloseIt5<E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception> extends AutoCloseable {

    @Override
    default void close() throws E1, E2, E3, E4, E5 {
        closeIt();
    }

    void closeIt() throws E1, E2, E3, E4, E5;

    /**
     * Decorate the lambda with one that processes any {@link Throwable} with a
     * predicate. The exception is rethrown when the {@code when} predicate
     * returns {@code true}, otherwise it is not rethrown and effectively
     * ignored. If the exception should always be rethrown, use
     * {@link #rethrow(com.github.richardroda.util.closeit.CloseIt5, java.util.function.Consumer) }.
     * If the exception should never be rethrown, use {@link CloseIt0#consumeAllThrowable(java.lang.AutoCloseable, java.util.function.Consumer)
     * }.
     *
     * @return A decorated lambda that processes any {@link Throwable} that
     * occurs with a predicate that causes the exception to be rethrown when it
     * returns {@code true}, and ignored otherwise.
     * @param <E1> Checked Exception Type
     * @param <E2> Checked Exception Type
     * @param <E3> Checked Exception Type
     * @param <E4> Checked Exception Type
     * @param <E5> Checked Exception Type
     * @param closeIt The closeIt lambda. Must not be {@code null}.
     * @param when Predicate to process exceptions. Must not be {@code null}.
     * Exception is rethrown when {@code true} is returned, and ignored
     * otherwise.
     * @see #rethrow(com.github.richardroda.util.closeit.CloseIt5,
     * java.util.function.Consumer)
     * @see CloseIt0#consumeAllThrowable(java.lang.AutoCloseable,
     * java.util.function.Consumer)
     * @see CloseIt0#consumeAllException(java.lang.AutoCloseable,
     * java.util.function.Consumer)
     * @see CloseIt0#consumeException(java.lang.AutoCloseable,
     * java.util.function.Consumer)
     */
    static <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception>
            CloseIt5<E1, E2, E3, E4, E5> rethrowWhen(CloseIt5<? extends E1, ? extends E2, ? extends E3, ? extends E4, ? extends E5> closeIt, Predicate<? super Throwable> when) {
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
     * Decorate the lambda with one that processes any {@link Throwable} with a
     * consumer and then rethrows it.
     *
     * @return A decorated lambda that processes any {@link Throwable} that
     * occurs with a consumer and then rethrows it.
     * @param <E1> Checked Exception Type
     * @param <E2> Checked Exception Type
     * @param <E3> Checked Exception Type
     * @param <E4> Checked Exception Type
     * @param <E5> Checked Exception Type
     * @param closeIt The closeIt lambda. Must not be {@code null}.
     * @param exConsumer Consumer to process exceptions. Must not be
     * {@code null}.
     * @see #rethrowWhen(com.github.richardroda.util.closeit.CloseIt5, java.util.function.Predicate) 
     */
    static <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception>
            CloseIt5<E1, E2, E3, E4, E5> rethrow(CloseIt5<? extends E1, ? extends E2, ? extends E3, ? extends E4, ? extends E5> closeIt, Consumer<? super Throwable> exConsumer) {
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
