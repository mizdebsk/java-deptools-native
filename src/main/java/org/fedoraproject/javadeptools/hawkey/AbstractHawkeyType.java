/*-
 * Copyright (c) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fedoraproject.javadeptools.hawkey;

import static org.fedoraproject.javadeptools.hawkey.HawkeyUtils.assumeNotNull;

import com.sun.jna.Pointer;

/**
 * @author Mikolaj Izdebski
 */
abstract class AbstractHawkeyType implements AutoCloseable {

    private Pointer self;

    abstract void free();

    AbstractHawkeyType(Pointer ref) throws HawkeyException {
        assumeNotNull(ref);
        this.self = ref;
    }

    /**
     * Release native resources used by this object. Calling {@link #close()} on
     * already closed object has no effect. Any other methods must not be called
     * after object is closed.
     */
    @Override
    public final void close() {
        if (self != null) {
            free();
            self = null;
        }
    }

    /**
     * Release native resources used by this object. Called by garbage
     * collector. Users should use {@link #close()} instead.
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    final Pointer self() {
        if (self == null) {
            String name = getClass().getSimpleName();
            throw new IllegalStateException(name + " is already closed");
        }
        return self;
    }
}
