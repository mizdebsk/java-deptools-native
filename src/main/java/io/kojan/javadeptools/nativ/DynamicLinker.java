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

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.util.Optional;

/**
 * @author Mikolaj Izdebski
 */
public class DynamicLinker implements SymbolLookup {

    public static class DynamicLibrary extends NativeObject {}
    public static class DynamicSymbol extends NativeObject {}

    public static interface LibDL {
        DynamicLibrary dlopen(String filename, int flags);
        DynamicSymbol dlsym(DynamicLibrary handle, String symbol);
    }

    private static final int RTLD_LAZY = 1;

    private static final DynamicLibrary RTLD_DEFAULT = null;

    private static class Lazy {
        static final LibDL DL = Native.load(LibDL.class, Native.jvmDefaultLookup());
    }

    private static final DynamicLibrary dlopen(String filename, int flags) {
        return Lazy.DL.dlopen(filename, flags);
    }

    private static final DynamicSymbol dlsym(DynamicLibrary handle, String symbol) {
        return Lazy.DL.dlsym(handle, symbol);
    }

    private final DynamicLibrary handle;

    public DynamicLinker() {
        handle = RTLD_DEFAULT;
    }

    public DynamicLinker(String lib) {
        handle = dlopen(lib, RTLD_LAZY);
        if (handle == null) {
            throw new RuntimeException("Unable to dlopen native library: " + lib);
        }
    }

    @Override
    public Optional<MemorySegment> find(String name) {
        DynamicSymbol sym = dlsym(handle, name);
        if (sym == null) {
            return Optional.empty();
        }
        return Optional.of(sym.getMemorySegment());
    }
}
