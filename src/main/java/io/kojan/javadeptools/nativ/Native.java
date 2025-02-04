/*-
 * Copyright (c) 2024-2025 Red Hat, Inc.
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

import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;

/**
 * @author Mikolaj Izdebski
 */
public class Native {

    protected static final Linker LINKER = Linker.nativeLinker();

    public static SymbolLookup jvmDefaultLookup() {
        return LINKER.defaultLookup();
    }

    public static SymbolLookup dlsymDefaultLookup() {
        return new DynamicLinker();
    }

    public static SymbolLookup dlopenLookup(String lib0, String... libs) {
        try {
            return new DynamicLinker(lib0);
        } catch (RuntimeException e0) {
            for (String lib : libs) {
                try {
                    return new DynamicLinker(lib);
                } catch (RuntimeException e) {
                }
            }
            throw e0;
        }
    }
}
