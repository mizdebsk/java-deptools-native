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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * @author Mikolaj Izdebski
 */
public class Native {

    private static final String INVOCATION_HANDLER_CLASS = "io.kojan.javadeptools.nativ.NativeInvocationHandler";
    private static final String NATIVE_POINTER_IMPL_CLASS = "io.kojan.javadeptools.nativ.NativePointerImpl";

    public static <T> T load(Class<T> type, String lib) {
        try {
            Class<?> implClass = Native.class.getClassLoader().loadClass(INVOCATION_HANDLER_CLASS);
            InvocationHandler ih = (InvocationHandler) implClass.getDeclaredConstructor(Class.class, String.class)
                    .newInstance(type, lib);
            return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, ih));
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException re) {
                throw re;
            }
            throw new RuntimeException(e);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T load(Class<T> type) {
        return load(type, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends NativeDataStructure> NativePointer<T> newPointer(Class<T> type) {
        try {
            Class<?> implClass = Native.class.getClassLoader().loadClass(NATIVE_POINTER_IMPL_CLASS);
            return (NativePointer<T>) implClass.getDeclaredConstructor(Class.class).newInstance(type);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException re) {
                throw re;
            }
            throw new RuntimeException(e);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
