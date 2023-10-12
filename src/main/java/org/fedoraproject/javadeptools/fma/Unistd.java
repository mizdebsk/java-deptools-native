package org.fedoraproject.javadeptools.fma;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

public class Unistd extends CLibrary {
    private static class Lazy {
        static final Unistd INSTANCE = new Unistd();
    }

    public static final int SEEK_SET = 0;
    public static final int SEEK_CUR = 1;
    public static final int SEEK_END = 2;

    private final MethodHandle strlen = downcallHandle("strlen",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
    private final MethodHandle close = downcallHandle("close",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
    private final MethodHandle lseek = downcallHandle("lseek",
            FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
    private final MethodHandle write = downcallHandle("write",
            FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));

    public static final long strlen(MemorySegment string) {
        try {
            return (long) Lazy.INSTANCE.close.invokeExact(string);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

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
        try (var arena = Arena.openConfined()) {
            var segment = arena.allocate(bytes.length);
            var bbuf = segment.asByteBuffer();
            bbuf.put(bytes);
            return (long) Lazy.INSTANCE.write.invokeExact(fd, segment, segment.byteSize());
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }
}
