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
     * be returned.
     * @throws RuntimeException The actual exception thrown is th, but it is
     * made to appear to the compiler as a runtime exception.
     */
    static RuntimeException hideException(Throwable th) throws RuntimeException {
        return hideException(th, RuntimeException.class);
    }
    
    /**
     * A no-op consumer for a throwable.
     * @param th Throwable to consume.
     */
    static void noOp(Throwable th){}
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
