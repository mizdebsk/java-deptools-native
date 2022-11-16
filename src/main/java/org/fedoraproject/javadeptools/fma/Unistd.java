package org.fedoraproject.javadeptools.fma;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.ResourceScope;

public class Unistd extends CLibrary {
    private static class Lazy {
        static final Unistd INSTANCE = new Unistd();
    }

    public static final int SEEK_SET = 0;
    public static final int SEEK_CUR = 1;
    public static final int SEEK_END = 2;

    private final MethodHandle close = downcallHandle("close",
            MethodType.methodType(int.class, int.class),
            FunctionDescriptor.of(CLinker.C_INT, CLinker.C_INT));
    private final MethodHandle lseek = downcallHandle("lseek",
            MethodType.methodType(long.class, int.class, long.class, int.class),
            FunctionDescriptor.of(CLinker.C_LONG_LONG, CLinker.C_INT, CLinker.C_LONG_LONG, CLinker.C_INT));
    private final MethodHandle write = downcallHandle("write",
            MethodType.methodType(long.class, int.class, MemoryAddress.class, long.class),
            FunctionDescriptor.of(CLinker.C_LONG_LONG, CLinker.C_INT, CLinker.C_POINTER, CLinker.C_LONG_LONG));

    public static final long close(int fd) {
        try {
            return (int) Lazy.INSTANCE.close.invokeExact(fd);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final long lseek(int fd, long offset, int whence) {
        try {
            return (long) Lazy.INSTANCE.lseek.invokeExact(fd, offset, whence);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final long write(int fd, byte[] bytes) {
        try (var scope = ResourceScope.newConfinedScope()) {
            var segment = MemorySegment.allocateNative(bytes.length, scope);
            var bbuf = segment.asByteBuffer();
            bbuf.put(bytes);
            return (long) Lazy.INSTANCE.write.invokeExact(fd, segment.address(), segment.byteSize());
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }
}
