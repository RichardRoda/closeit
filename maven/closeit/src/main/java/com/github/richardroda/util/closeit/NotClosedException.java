
package com.github.richardroda.util.closeit;

/**
 * Exception which is thrown by a {@link CloseIt0} created using
 * {@link CloseIt0#wrapException(java.lang.AutoCloseable) } when a checked
 * exception occurs, or {@link CloseIt0#wrapAllException(java.lang.AutoCloseable) }
 * when any exception occurs, or {@link CloseIt0#wrapAllThrowable(java.lang.AutoCloseable) }
 * when any throwable occurs.  This class is final and defines its constructor
 * as package private to provide some guarantee that this exception can only
 * originate from a close method of a decorated {@code AutoClosable}.  If another
 * kind of exception needs to be caught, consider using a {@code multi-catch}
 * with this exception and the other exceptions that need to be handled.
 * 
 * @author Richard Roda
 * @see CloseIt0#wrapException(java.lang.AutoCloseable) 
 * @see CloseIt0#wrapAllException(java.lang.AutoCloseable) 
 * @see CloseIt0#wrapAllThrowable(java.lang.AutoCloseable) 
 */
public final class NotClosedException extends IllegalStateException {

    /**
     * Package priave constructor guarantees this class is only created
     * by the {@link CloseIt0} class.
     * @param cause The Exception or Throwable that occurred in the close method.
     */
    NotClosedException(Throwable cause) {
        super(cause);
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
