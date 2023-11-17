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
import java.util.Optional;

/**
 * @author Mikolaj Izdebski
 */
class NativeInvocationHandler implements InvocationHandler {

    private static interface DownConverter {
        Object convert(Object obj, Arena arena) throws Throwable;

        static DownConverter forType(Class<?> type) {
            return switch (type) {
            case Class<?> cls when String.class.isAssignableFrom(cls) ->
                (obj, arena) -> arena.allocateUtf8String((String) obj);
            case Class<?> cls when NativeDataStructure.class.isAssignableFrom(cls) ->
                (obj, arena) -> ((NativeDataStructure) obj).ms;
            default -> (obj, arena) -> obj;
            };
        }
    }

    private static interface UpConverter {
        Object convert(Object obj) throws Throwable;

        static UpConverter forType(Class<?> type) throws ReflectiveOperationException {
            return switch (type) {
            case Class<?> cls when String.class.isAssignableFrom(cls) ->
                ((UpConverter) ms -> ((MemorySegment) ms).getUtf8String(0));
            case Class<?> cls when NativeDataStructure.class.isAssignableFrom(cls) -> new NativeUpConverter(cls);
            default -> ((UpConverter) obj -> obj);
            };
        }
    }

    private static class NativeUpConverter implements UpConverter {
        private Constructor<?> ctr;

        NativeUpConverter(Class<?> type) throws ReflectiveOperationException {
            ctr = type.getDeclaredConstructor();
            ctr.setAccessible(true);
        }

        public NativeDataStructure convert(Object obj) throws Throwable {
            NativeDataStructure nativ = (NativeDataStructure) ctr.newInstance();
            nativ.ms = (MemorySegment) obj;
            return nativ;
        }

    }

    private static class Stub {
        final MethodHandle mh;
        final DownConverter[] argConvs;
        final UpConverter retConv;

        Stub(MethodHandle mh, DownConverter[] argConvs, UpConverter retConv) {
            this.mh = mh;
            this.argConvs = argConvs;
            this.retConv = retConv;
        }

        Object invoke(Object[] args, Arena arena) throws Throwable {
            for (int i = 0; i < argConvs.length; i++) {
                if (args[i] == null) {
                    args[i] = MemorySegment.NULL;
                } else {
                    args[i] = argConvs[i].convert(args[i], arena);
                }
            }
            Object ret = mh.invokeWithArguments(args);
            if (MemorySegment.NULL.equals(ret)) {
                return null;
            }
            return retConv.convert(ret);
        }

    }

    private Map<Method, Stub> stubs = new LinkedHashMap<>();

    private static MemoryLayout selectLayout(Class<?> type) {
        return switch (type) {
        case Class<?> cls when String.class.isAssignableFrom(cls) ->
            ValueLayout.ADDRESS.withTargetLayout(MemoryLayout.sequenceLayout(ValueLayout.JAVA_BYTE));
        case Class<?> cls when NativeDataStructure.class.isAssignableFrom(cls) -> ValueLayout.ADDRESS;
        case Class<?> cls when long.class.isAssignableFrom(cls) -> ValueLayout.JAVA_LONG;
        case Class<?> cls when int.class.isAssignableFrom(cls) -> ValueLayout.JAVA_INT;
        default -> throw new IllegalStateException("data type is not supported: " + type);
        };
    }

    public NativeInvocationHandler(SymbolLookup lookup, Linker linker, Class<?> iface)
            throws ReflectiveOperationException {

        for (Method method : iface.getDeclaredMethods()) {
            MemoryLayout[] argLayouts = new MemoryLayout[method.getParameterCount()];
            DownConverter[] argConvs = new DownConverter[method.getParameterCount()];
            int i = 0;
            for (Class<?> type : method.getParameterTypes()) {
                argLayouts[i] = selectLayout(type);
                argConvs[i] = DownConverter.forType(type);
                i++;
            }
            Optional<MemorySegment> methodAddress = lookup.find(method.getName());
            if (methodAddress.isEmpty()) {
                throw new RuntimeException("Native method was not bound: " + method.getName());
            }
            MethodHandle mh;
            if (method.getReturnType().equals(Void.TYPE)) {
                mh = linker.downcallHandle(methodAddress.get(), FunctionDescriptor.ofVoid(argLayouts));
            } else {
                mh = linker.downcallHandle(methodAddress.get(),
                        FunctionDescriptor.of(selectLayout(method.getReturnType()), argLayouts));
            }

            UpConverter retConv = UpConverter.forType(method.getReturnType());
            Stub stub = new Stub(mh, argConvs, retConv);
            stubs.put(method, stub);
        }
    }

    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        try (var arena = Arena.ofConfined()) {
            if (args == null) {
                args = new Object[0];
            }
            Stub stub = stubs.get(method);
            if (stub == null) {
                throw new IllegalStateException("No stub was bound for method " + method);
            }
            return stub.invoke(args, arena);
        }
    }

}
