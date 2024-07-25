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
import java.lang.reflect.Proxy;

/**
 * @author Mikolaj Izdebski
 */
public class Native {

    public static <T> T load(Class<T> type, String lib) {
        InvocationHandler ih = new NativeInvocationHandler(type, lib);
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, ih));
    }

    public static <T> T load(Class<T> type) {
        return load(type, null);
    }

    public static <T extends NativeDataStructure> NativePointer<T> newPointer(Class<T> type) {
        return (NativePointer<T>) new NativePointerImpl<>(type);
    }
}
