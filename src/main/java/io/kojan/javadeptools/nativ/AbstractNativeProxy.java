/*-
 * Copyright (c) 2024 Red Hat, Inc.
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
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Abstract base class for all generated native proxies.
 * 
 * @author Mikolaj Izdebski
 */
public abstract class AbstractNativeProxy extends Native {

    protected static final ValueLayout VOID = null;
    protected static final ValueLayout STR = ValueLayout.ADDRESS
            .withTargetLayout(MemoryLayout.sequenceLayout(Long.MAX_VALUE, ValueLayout.JAVA_BYTE));
    protected static final ValueLayout OBJ = ValueLayout.ADDRESS;
    protected static final ValueLayout LONG = ValueLayout.JAVA_LONG;
    protected static final ValueLayout INT = ValueLayout.JAVA_INT;

    private final SymbolLookup lookup;

    protected AbstractNativeProxy(SymbolLookup lookup) {
        this.lookup = lookup;
    }

    protected static String upConvertString(MemorySegment ms) {
        if (MemorySegment.NULL.equals(ms)) {
            return null;
        }
        return ms.getString(0);
    }

    protected static MemorySegment downConvertString(String str, Arena arena) {
        if (str == null) {
            return MemorySegment.NULL;
        }
        return arena.allocateFrom(str);
    }

    protected static <T extends NativeObject> T upConvertObject(Supplier<T> ctr, MemorySegment ms) {
        if (MemorySegment.NULL.equals(ms)) {
            return null;
        }
        T obj = ctr.get();
        obj.setMemorySegment(ms);
        return obj;
    }

    protected static MemorySegment downConvertObject(NativeObject obj) {
        if (obj == null) {
            return MemorySegment.NULL;
        }
        return obj.getMemorySegment();
    }

    protected MemorySegment lookup(String symbol) {
        Optional<MemorySegment> optionalMethodAddress = lookup.find(symbol);
        if (optionalMethodAddress.isEmpty()) {
            throw new RuntimeException("Native method was not bound: " + symbol);
        }
        return optionalMethodAddress.get();
    }

    protected MethodHandle makeMethodHandle(ValueLayout retLayout, String symbol, ValueLayout... paramLayouts) {
        MemorySegment methodAddress = lookup(symbol);
        FunctionDescriptor functionDescriptor;
        if (retLayout == null) {
            functionDescriptor = FunctionDescriptor.ofVoid(paramLayouts);
        } else {
            functionDescriptor = FunctionDescriptor.of(retLayout, paramLayouts);
        }
        return LINKER.downcallHandle(methodAddress, functionDescriptor);

    }
}
