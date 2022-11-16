package org.fedoraproject.javadeptools.fma;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.ResourceScope;
import jdk.incubator.foreign.SymbolLookup;
import jdk.incubator.foreign.ValueLayout;

public class CLibrary {
    private final SymbolLookup lookup;
    private final CLinker clinker;

    protected CLibrary(String... libraries) {
        /*
         * Libraries and fields have to be initialized in this parent
         * constructor before inherited final method handles are initialized
         */
        if (libraries.length == 0) {
            lookup = CLinker.systemLookup();
        } else {
            lookup = SymbolLookup.loaderLookup();
        }
        clinker = CLinker.getInstance();

        for (String library : libraries) {
            loadLibrary(library);
        }
    }

    public static class Layouts {
        private static ValueLayout findLayout(int bitSize) {
            for (ValueLayout layout : Arrays.asList(CLinker.C_CHAR, CLinker.C_SHORT,
                    CLinker.C_INT, CLinker.C_LONG, CLinker.C_LONG_LONG)) {
                if (layout.bitSize() == bitSize) {
                    return layout;
                }
            }

            return null;
        }

        // TODO cleaner solution possible in JDKs > 17
        // static final ValueLayout int32_t = ValueLayout.JAVA_INT;
        // static final ValueLayout int64_t = ValueLayout.JAVA_LONG;
        static final ValueLayout int32_t = findLayout(32);
        static final ValueLayout int64_t = findLayout(64);

        static final ValueLayout size_t = CLinker.C_LONG_LONG;
    }

    public static final MemoryAddress toCStringAddress(String string, ResourceScope scope) {
        if (string == null) {
            return MemoryAddress.NULL;
        } else {
            return CLinker.toCString(string, scope).address();
        }
    }

    private static void loadLibrary(String name) {
        for (String libdirname : System.getProperty("java.library.path").split(":")) {
            try {
                var libpath = Paths.get(libdirname);
                if (Files.isDirectory(libpath)) {
                    var optLib = Files.find(libpath, Integer.MAX_VALUE, (path, attributes) ->
                        attributes.isRegularFile() && path.getFileName().toString().startsWith("lib" + name + ".so")
                    ).findFirst();
                    if (optLib.isPresent()) {
                        System.load(optLib.get().toString());
                        return;
                    }
                }
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
        throw new UnsatisfiedLinkError("Library " + name + " not found in " + System.getProperty("java.library.path"));
    }

    protected final MethodHandle downcallHandle(String symbol, MethodType type, FunctionDescriptor descriptor) {
        return clinker.downcallHandle(lookup.lookup(symbol).get(), type, descriptor);
    }
}
