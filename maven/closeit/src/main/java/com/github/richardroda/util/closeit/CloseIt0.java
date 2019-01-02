package com.github.richardroda.util.closeit;

import java.util.Objects;
import java.util.function.Consumer;
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

    /**
     * Close the underlying object.  May also be decorated (wrapped) with
     * behavior to process the close exception.
     * 
     * @throws NotClosedException Thrown when this is created using
     * {@link #wrapException(java.lang.AutoCloseable) } and a checked exception
     * occurs, or {@link #wrapAllException(java.lang.AutoCloseable) } and any
     * exception occurs, or {@link #wrapAllThrowable(java.lang.AutoCloseable) }
     * and any {@link Throwable} occurs.
     */
    @Override
    public void close() throws NotClosedException;

    /**
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which throws
     * no checked exceptions by wrapping any checked exceptions to a 
     * {@link NotClosedException}.
     * @param autoCloseable An autoCloseable object or lambda.
     * @return A {@code CloseIt0} which wraps any checked exceptions in
     * a {@code NotClosedException}.
     * @see #toCloseIt0(java.lang.AutoCloseable, java.util.function.Function)
     * @see CloseIt1#wrapException(java.lang.AutoCloseable, java.util.function.Function) 
     */
    public static CloseIt0 wrapException(AutoCloseable autoCloseable) {
        return toCloseIt0(autoCloseable, NotClosedException::new);
    }

    /**
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which throws
     * no checked exceptions by wrapping all exceptions (including runtime exceptions)
     * to a {@link NotClosedException}.
     * @param autoCloseable An autoCloseable object or lambda.
     * @return A {@code CloseIt0} which wraps all exceptions in
     * a {@code NotClosedException}.
     * @see #toCloseIt0AllException(java.lang.AutoCloseable, java.util.function.Function) 
     * @see CloseIt1#wrapAllException(java.lang.AutoCloseable, java.util.function.Function) 
     */
    public static CloseIt0 wrapAllException(AutoCloseable autoCloseable) {
        return toCloseIt0AllException(autoCloseable, NotClosedException::new);
    }

    /**
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which throws
     * no checked exceptions by wrapping all throwables to a 
     * {@link NotClosedException}.
     * @param autoCloseable An autoCloseable object or lambda.
     * @return A {@code CloseIt0} which wraps all {@link Throwable} in
     * a {@code NotClosedException}.
     * @see #toCloseIt0AllThrowable(java.lang.AutoCloseable, java.util.function.Function) 
     * @see CloseIt1#wrapAllThrowable(java.lang.AutoCloseable, java.util.function.Function) 
     */
    public static CloseIt0 wrapAllThrowable(AutoCloseable autoCloseable) {
        return toCloseIt0AllThrowable(autoCloseable, NotClosedException::new);
    }

    /**
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which hides
     * any checked exceptions from the compiler.  The checked exception is
     * still thrown, but no compilation error occurs if it is uncaught and not
     * included in the caller's throws clause.  This should be used with great
     * care, as it violates the normal Java {@code throws} contract.  Since
     * calling code will not be expecting a checked exception, unpredictable
     * results may occur.  Consider using {@link #wrapException(java.lang.AutoCloseable) } 
     * which wraps any checked exceptions within an {@link NotClosedException}.
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
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which ignores
     * all checked exceptions.  Unchecked exceptions are re-thrown.
     * @param autoCloseable An autoCloseable object or lambda.
     * @return A {@code CloseIt0} which ignores (does not rethrow) any checked
     * exception.
     */
    public static CloseIt0 ignoreException(AutoCloseable autoCloseable) {
        return consumeException(autoCloseable, CloseItHelper::noOp);
    }
    
    /**
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which consumes
     * all checked exceptions.  Unchecked exceptions are re-thrown.
     * @param autoCloseable An autoCloseable object or lambda.
     * @param exConsumer Consumer to apply an operation to the processed exception
     * @return A {@code CloseIt0} which consumes (does not rethrow) any checked
     * exception, but accepts them with the provided consumer.
     */
    public static CloseIt0 consumeException(AutoCloseable autoCloseable
        , Consumer<? super Exception> exConsumer) {
        Objects.requireNonNull(autoCloseable, "autoCloseable required");
        Objects.requireNonNull(exConsumer, "exConsumer required");
        return () -> {
          try {
              autoCloseable.close();
          } catch (RuntimeException ex) {
              throw ex;
          } catch (Exception ex) {
              exConsumer.accept(ex);
          }
        };
    }
    
    /**
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which ignores
     * all exceptions (including runtime exceptions).
     * @param autoCloseable An autoCloseable object or lambda.
     * @return A {@code CloseIt0} which ignores (does not rethrow) any
     * exception.
     */
    public static CloseIt0 ignoreAllException(AutoCloseable autoCloseable) {
        return consumeAllException(autoCloseable, CloseItHelper::noOp);
    }
    
    /**
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which consumes
     * all exceptions (including runtime exceptions).
     * @param autoCloseable An autoCloseable object or lambda.
     * @param exConsumer Consumer to apply an operation to the processed exception
     * @return A {@code CloseIt0} which consumes (does not rethrow) any
     * exception, but accepts them with the provided consumer.
     */
    public static CloseIt0 consumeAllException(AutoCloseable autoCloseable
        , Consumer<? super Exception> exConsumer) {
        Objects.requireNonNull(autoCloseable, "autoCloseable required");
        Objects.requireNonNull(exConsumer, "exConsumer required");
        return () -> {
          try {
              autoCloseable.close();
          } catch (Exception ex) {
              exConsumer.accept(ex);
          }
        };
    }
    
    /**
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which ignores
     * all throwables (including checked, unchecked exceptions and errors).
     * @param autoCloseable An autoCloseable object or lambda.
     * @return A {@code CloseIt0} which ignores (does not rethrow) any throwable.
     */
    public static CloseIt0 ignoreAllThrowable(AutoCloseable autoCloseable) {
        return consumeAllThrowable(autoCloseable, CloseItHelper::noOp);
    }
    
    /**
     * Convert an {@link AutoCloseable} into a {@link CloseIt0} which consumes
     * all throwables (including checked, unchecked exceptions and errors).
     * @param autoCloseable An autoCloseable object or lambda.
     * @param exConsumer Consumer to apply an operation to the processed throwable
     * @return A {@code CloseIt0} which consumes (does not rethrow) any throwable
     * exception, but accepts them with the provided consumer.
     */
    public static CloseIt0 consumeAllThrowable(AutoCloseable autoCloseable
        , Consumer<? super Throwable> exConsumer) {
        Objects.requireNonNull(autoCloseable, "autoCloseable required");
        Objects.requireNonNull(exConsumer, "exConsumer required");
        return () -> {
          try {
              autoCloseable.close();
          } catch (Throwable ex) {
              exConsumer.accept(ex);
          }
        };
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
     * {@link IllegalStateException}.
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

    /**
     * Create a {@link CloseIt0} from an {@link AutoCloseable} that
     * uses a {@link Function} to map all exceptions (checked and unchecked) to
     * a {@link RuntimeException}.
     * @param autoCloseable AutoCloseable object or lambda.
     * @param exceptionMapper Function to map all exceptions (both checked and
     * unchecked).  A {@code null} return value, means to swallow (ignore) the
     * exception, so 
     * {@code ex->null } would ignore (not throw) all exceptions (not 
     * recommended).  Another example: using {@code IllegalStateException::new}
     * will cause all exceptions to be wrapped within an 
     * {@link IllegalStateException}. 
     * 
     * @return A {@code CloseIt0} which uses an {@code exceptionMapper} to
     * map all exceptions to an unchecked exception.
     */
    public static CloseIt0 toCloseIt0AllException(AutoCloseable autoCloseable,
             Function<? super Exception, ? extends RuntimeException> exceptionMapper) {
        Objects.requireNonNull(autoCloseable, "autoCloseable required");
        Objects.requireNonNull(exceptionMapper, "exceptionMapper required");

        return () -> {
            try {
                autoCloseable.close();
            } catch (Exception ex) {
                RuntimeException rex = exceptionMapper.apply(ex);
                if (rex != null) {
                    throw rex;
                }
            }
        };
    }    

    /**
     * Create a {@link CloseIt0} from an {@link AutoCloseable} that
     * uses a {@link Function} to map all throwables (checked, unchecked, 
     * and errors) to a {@link RuntimeException}.
     * @param autoCloseable AutoCloseable object or lambda.
     * @param exceptionMapper Function to map all throwables.
     * A {@code null} return value, means to swallow (ignore) the
     * throwable, so {@code ex->null } would ignore (not throw) all throwables
     * (not recommended).  Another example: using {@code IllegalStateException::new}
     * will cause all exceptions to be wrapped within an 
     * {@link IllegalStateException}. 
     * 
     * @return A {@code CloseIt0} which uses an {@code exceptionMapper} to
     * map all throwables to an unchecked exception.
     */
    public static CloseIt0 toCloseIt0AllThrowable(AutoCloseable autoCloseable,
             Function<? super Throwable, ? extends RuntimeException> exceptionMapper) {
        Objects.requireNonNull(autoCloseable, "autoCloseable required");
        Objects.requireNonNull(exceptionMapper, "exceptionMapper required");

        return () -> {
            try {
                autoCloseable.close();
            } catch (Throwable ex) {
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
