package org.fedoraproject.javadeptools.ffi;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.ResourceScope;

public class Rpmio extends CLibrary {
    private static class Lazy {
        static final Rpmio INSTANCE = new Rpmio();
    }

    private Rpmio() {
        super("rpmio");
    }

    private final MethodHandle fdDup = downcallHandle("fdDup",
            MethodType.methodType(MemoryAddress.class, int.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_INT));
    private final MethodHandle Fopen = downcallHandle("Fopen",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle Fclose = downcallHandle("Fclose",
            MethodType.methodType(void.class, MemoryAddress.class),
            FunctionDescriptor.ofVoid(CLinker.C_POINTER));
    private final MethodHandle Ftell = downcallHandle("Ftell",
            MethodType.methodType(long.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_LONG, CLinker.C_POINTER));
    private final MethodHandle Ferror = downcallHandle("Ferror",
            MethodType.methodType(int.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_INT, CLinker.C_POINTER));
    private final MethodHandle Fstrerror = downcallHandle("Fstrerror",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER));

    public static final MemoryAddress fdDup(int fd) {
        try {
            return (MemoryAddress) Lazy.INSTANCE.fdDup.invokeExact(fd);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemoryAddress Fopen(String filepath, String mode) {
        try (var filepathScope = ResourceScope.newConfinedScope();
             var modeScope = ResourceScope.newConfinedScope()) {
            return (MemoryAddress) Lazy.INSTANCE.Fopen.invokeExact(
                    CLibrary.toCStringAddress(filepath, filepathScope),
                    CLibrary.toCStringAddress(mode, modeScope));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final void Fclose(MemoryAddress ts) {
        try {
            Lazy.INSTANCE.Fclose.invokeExact(ts);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final long Ftell(MemoryAddress fd) {
        try {
            return (long) Lazy.INSTANCE.Ftell.invokeExact(fd);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int Ferror(MemoryAddress fd) {
        try {
            return (int) Lazy.INSTANCE.Ferror.invokeExact(fd);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final String Fstrerror(MemoryAddress fd) {
        try {
            return CLinker.toJavaString((MemoryAddress) Lazy.INSTANCE.Fstrerror.invokeExact(fd));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }
}
