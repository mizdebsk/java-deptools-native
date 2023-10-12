package org.fedoraproject.javadeptools.fma;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

public class Rpmio extends CLibrary {
    private static class Lazy {
        static final Rpmio INSTANCE = new Rpmio();
    }

    private Rpmio() {
        super("rpmio");
    }

    private final MethodHandle fdDup = downcallHandle("fdDup",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
    private final MethodHandle Fopen = downcallHandle("Fopen",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
    private final MethodHandle Fclose = downcallHandle("Fclose",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
    private final MethodHandle Ftell = downcallHandle("Ftell",
            FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
    private final MethodHandle Ferror = downcallHandle("Ferror",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
    private final MethodHandle Fstrerror = downcallHandle("Fstrerror",
            FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS));

    public static final MemorySegment fdDup(int fd) {
        try {
            return (MemorySegment) Lazy.INSTANCE.fdDup.invokeExact(fd);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemorySegment Fopen(String filepath, String mode) {
        try (var filepathArena = Arena.openConfined();
             var modeArena = Arena.openConfined()) {
            return (MemorySegment) Lazy.INSTANCE.Fopen.invokeExact(
                    CLibrary.toCString(filepath, filepathArena),
                    CLibrary.toCString(mode, modeArena));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final void Fclose(MemorySegment ts) {
        try {
            Lazy.INSTANCE.Fclose.invokeExact(ts);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final long Ftell(MemorySegment fd) {
        try {
            return (long) Lazy.INSTANCE.Ftell.invokeExact(fd);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int Ferror(MemorySegment fd) {
        try {
            return (int) Lazy.INSTANCE.Ferror.invokeExact(fd);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final String Fstrerror(MemorySegment fd) {
        try {
            return CLibrary.toJavaString((MemorySegment) Lazy.INSTANCE.Fstrerror.invokeExact(fd));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }
}
