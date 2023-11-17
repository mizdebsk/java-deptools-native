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
package org.fedoraproject.javadeptools.rpm;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

/**
 * Native pointer to a RPM data structure.
 * 
 * @author Mikolaj Izdebski
 */
class Pointer<T extends NativeDataStructure> extends NativeDataStructure {

    private Class<T> type;

    public Pointer(Class<T> type) {
        super(Arena.ofAuto().allocate(ValueLayout.ADDRESS));
        this.type = type;
    }

    public T dereference() {
        MemorySegment address = ms.get(ValueLayout.ADDRESS, 0);
        if (address.equals(MemorySegment.NULL)) {
            return null;
        }
        try {
            return type.getDeclaredConstructor(MemorySegment.class).newInstance(address);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
