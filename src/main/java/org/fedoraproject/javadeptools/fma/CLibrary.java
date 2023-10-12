package org.fedoraproject.javadeptools.fma;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CLibrary {
    private final SymbolLookup lookup;
    private final Linker linker;

    protected CLibrary(String... libraries) {
        linker = Linker.nativeLinker();

        /*
         * Libraries and fields have to be initialized in this parent
         * constructor before inherited final method handles are initialized
         */
        if (libraries.length == 0) {
            lookup = linker.defaultLookup();
        } else {
            lookup = SymbolLookup.loaderLookup();
        }

        for (String library : libraries) {
            loadLibrary(library);
        }
    }

    public static final MemorySegment toCString(String string, Arena arena) {
        if (string == null) {
            return MemorySegment.NULL;
        } else {
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(string);
            var segment = arena.allocate(buffer.capacity() + 1);
            segment.asByteBuffer().put(buffer);
            return segment;
        }
     }

    protected static final String toJavaString(MemorySegment segment) {
        if (segment.equals(MemorySegment.NULL)) {
            return null;
        } else {
            // segment = MemorySegment.ofAddress(segment.address(), Unistd.strlen(segment) + 1);
            return segment.getUtf8String(0);
        }
    }

    private static void loadLibrary(String name) {
        for (var libdirname : System.getProperty("java.library.path").split(":")) {
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

    protected final MethodHandle downcallHandle(String symbol, FunctionDescriptor descriptor) {
        return linker.downcallHandle(lookup.find(symbol).get(), descriptor);
    }
}
