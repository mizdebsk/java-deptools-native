package io.kojan.javadeptools.nativ;

import io.kojan.javadeptools.nativ.DynamicLinker.DynamicLibrary;
import io.kojan.javadeptools.nativ.DynamicLinker.DynamicSymbol;
import io.kojan.javadeptools.nativ.DynamicLinker.LibDL;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;

/**
 * Native implementation of LibDL.
 */
final class LibDL_Impl extends AbstractNativeProxy implements LibDL {
    public LibDL_Impl(SymbolLookup lookup) {
        super(lookup);
    }

    /**
     * Method stub that invokes native method {@code dlopen}.
     * @param filename String
     * @param flags int
     * @return DynamicLibrary
     */
    @Override
    public DynamicLibrary dlopen(String filename, int flags) {
        try (Arena arena = Arena.ofConfined()) {
            return upConvertObject(DynamicLibrary::new, (MemorySegment)mh_dlopen.invokeExact(
                    downConvertString(filename, arena),
                    flags
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function dlopen", _t);
        }
    }

    private final MethodHandle mh_dlopen = makeMethodHandle(OBJ, "dlopen", STR, INT);

    /**
     * Method stub that invokes native method {@code dlsym}.
     * @param handle DynamicLibrary
     * @param symbol String
     * @return DynamicSymbol
     */
    @Override
    public DynamicSymbol dlsym(DynamicLibrary handle, String symbol) {
        try (Arena arena = Arena.ofConfined()) {
            return upConvertObject(DynamicSymbol::new, (MemorySegment)mh_dlsym.invokeExact(
                    downConvertObject(handle),
                    downConvertString(symbol, arena)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function dlsym", _t);
        }
    }

    private final MethodHandle mh_dlsym = makeMethodHandle(OBJ, "dlsym", OBJ, STR);

    public static Iterable<FunctionDescriptor> getFunctionLayouts() {
        return Arrays.asList(
                FunctionDescriptor.of(OBJ, OBJ, STR),
                FunctionDescriptor.of(OBJ, STR, INT)
        );
    }
}

/**
 * Trampoline class that contains methods of LibDL as static methods.
 */
class LibDL_Static {
    private static class Lazy {
        static final SymbolLookup LOOKUP = Native.jvmDefaultLookup();
        static final LibDL LIB = new LibDL_Impl(LOOKUP);
    }

    /**
     * Method stub that invokes native method {@code dlopen}.
     * @param filename String
     * @param flags int
     * @return DynamicLibrary
     */
    public static final DynamicLibrary dlopen(String filename, int flags) {
        return Lazy.LIB.dlopen(filename, flags);
    }

    /**
     * Method stub that invokes native method {@code dlsym}.
     * @param handle DynamicLibrary
     * @param symbol String
     * @return DynamicSymbol
     */
    public static final DynamicSymbol dlsym(DynamicLibrary handle, String symbol) {
        return Lazy.LIB.dlsym(handle, symbol);
    }
}
