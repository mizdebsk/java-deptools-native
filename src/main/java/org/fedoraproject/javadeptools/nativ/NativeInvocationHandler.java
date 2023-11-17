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
package org.fedoraproject.javadeptools.nativ;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Mikolaj Izdebski
 */
class NativeInvocationHandler implements InvocationHandler {

    private Map<String, MethodHandle> handles = new LinkedHashMap<>();

    private MemoryLayout layout(Method method, Class<?> type) {
        return switch (type) {
        case Class<?> cls when long.class.isAssignableFrom(cls) -> ValueLayout.JAVA_LONG;
        case Class<?> cls when int.class.isAssignableFrom(cls) -> ValueLayout.JAVA_INT;
        case Class<?> cls when String.class.isAssignableFrom(cls) ->
            ValueLayout.ADDRESS.withTargetLayout(MemoryLayout.sequenceLayout(JAVA_BYTE));
        case Class<?> cls when NativeDataStructure.class.isAssignableFrom(cls) -> ValueLayout.ADDRESS;
        default -> throw new IllegalStateException("layout " + type);
        };
    }

    public NativeInvocationHandler(SymbolLookup lookup, Linker linker, Class<?> iface) {

        for (Method method : iface.getDeclaredMethods()) {
            MemoryLayout[] argLayouts = new MemoryLayout[method.getParameterCount()];
            int i = 0;
            for (Class<?> type : method.getParameterTypes()) {
                argLayouts[i++] = layout(method, type);
            }
            MemorySegment methodAddress = lookup.find(method.getName()).get();
            MethodHandle mh;
            if (method.getReturnType().equals(Void.TYPE)) {
                mh = linker.downcallHandle(methodAddress, FunctionDescriptor.ofVoid(argLayouts));
            } else {
                mh = linker.downcallHandle(methodAddress,
                        FunctionDescriptor.of(layout(method, method.getReturnType()), argLayouts));
            }
            handles.put(method.getName(), mh);
        }
    }

    private static Object downConvert(Object obj, Arena arena) {
        return switch (obj) {
        case null -> MemorySegment.NULL;
        case String s -> arena.allocateUtf8String(s);
        case NativeDataStructure p -> p.ms;
        default -> obj;
        };
    }

    private static Object upConvert(Object obj, Class<?> type) throws Throwable {
        if (obj instanceof MemorySegment ms) {
            if (ms.equals(MemorySegment.NULL)) {
                return null;
            }
            return switch (type) {
            case Class<?> cls when String.class.isAssignableFrom(cls) -> ms.getUtf8String(0);
            case Class<?> cls when NativeDataStructure.class.isAssignableFrom(cls) -> {
                Constructor<?> ctr = cls.getDeclaredConstructor();
                ctr.setAccessible(true);
                NativeDataStructure obj2 = (NativeDataStructure) ctr.newInstance();
                obj2.ms = ms;
                yield obj2;
            }
            default -> obj;
            };
        }
        return obj;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        try (var arena = Arena.ofConfined()) {
            if (args == null) {
                args = new Object[0];
            }
            for (int i = 0; i < args.length; i++) {
                args[i] = downConvert(args[i], arena);
            }
            Object ret = handles.get(method.getName()).invokeWithArguments(args);
            return upConvert(ret, method.getReturnType());
        }
    }

}
