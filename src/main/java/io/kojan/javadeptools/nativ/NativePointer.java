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
public class NativePointer extends NativeObject {

    public NativePointer() {
        setMemorySegment(Arena.ofAuto().allocate(ValueLayout.ADDRESS));
    }

    public <T extends NativeObject> T dereference(Supplier<T> ctr) {
        MemorySegment address = getMemorySegment().get(ValueLayout.ADDRESS, 0);
        if (address.equals(MemorySegment.NULL)) {
            return null;
        }
        T obj = ctr.get();
        obj.setMemorySegment(address);
        return obj;
    }
}
