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

/**
 * @author Mikolaj Izdebski
 */
class DynamicLinker {

    private static interface LibDL {
        DynamicLibrary dlopen(String filename, int flags);

        DynamicSymbol dlsym(DynamicLibrary handle, String symbol);
    }

    static final int RTLD_LAZY = 1;

    private static class Lazy {
        static final LibDL DL = Native.load(LibDL.class);
    }

    static final DynamicLibrary dlopen(String filename, int flags) {
        return Lazy.DL.dlopen(filename, flags);
    }

    static final DynamicSymbol dlsym(DynamicLibrary handle, String symbol) {
        return Lazy.DL.dlsym(handle, symbol);
    }
}
