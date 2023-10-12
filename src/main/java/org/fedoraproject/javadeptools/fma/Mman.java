package org.fedoraproject.javadeptools.fma;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

public class Mman extends CLibrary {
    private static class Lazy {
        static final Mman INSTANCE = new Mman();
    }

    private final MethodHandle memfd_create = downcallHandle("memfd_create",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    public static final int memfd_create(String name, int flags) {
        try (var nameArena = Arena.openConfined()) {
            return (int) Lazy.INSTANCE.memfd_create.invokeExact(
                    CLibrary.toCString(name, nameArena), flags);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }
}
