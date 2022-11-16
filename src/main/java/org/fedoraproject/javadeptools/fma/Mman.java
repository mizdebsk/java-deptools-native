package org.fedoraproject.javadeptools.fma;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.ResourceScope;

public class Mman extends CLibrary {
    private static class Lazy {
        static final Mman INSTANCE = new Mman();
    }

    private final MethodHandle memfd_create = downcallHandle("memfd_create",
            MethodType.methodType(int.class, MemoryAddress.class, int.class),
            FunctionDescriptor.of(CLinker.C_INT, CLinker.C_POINTER, CLinker.C_INT));

    public static final int memfd_create(String name, int flags) {
        try (var nameScope = ResourceScope.newConfinedScope()) {
            return (int) Lazy.INSTANCE.memfd_create.invokeExact(
                    CLibrary.toCStringAddress(name, nameScope), flags);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }
}
