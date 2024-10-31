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

import java.lang.foreign.SymbolLookup;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Mikolaj Izdebski
 */
public class NativeReflector {
    public static <T> T dynamicGlue(Class<T> type, SymbolLookup lookup) {
        InvocationHandler ih = new NativeInvocationHandler(type, lookup);
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, ih));
    }
}
