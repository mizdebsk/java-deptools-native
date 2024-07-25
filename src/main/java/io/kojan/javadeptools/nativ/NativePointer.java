/*-
 * Copyright (c) 2023 Red Hat, Inc.
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
package io.kojan.javadeptools.nativ;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.function.Supplier;

/**
 * Native pointer to a native data structure.
 * 
 * @author Mikolaj Izdebski
 */
public class NativePointer<T extends NativeDataStructure> extends NativeDataStructure {

    private final Supplier<T> ctr;

    public NativePointer(Supplier<T> ctr) {
        this.ctr = ctr;
        ms = Arena.ofAuto().allocate(ValueLayout.ADDRESS);
    }

    public T dereference() {
        MemorySegment address = ms.get(ValueLayout.ADDRESS, 0);
        if (address.equals(MemorySegment.NULL)) {
            return null;
        }
        T obj = ctr.get();
        obj.ms = address;
        return obj;
    }
}
