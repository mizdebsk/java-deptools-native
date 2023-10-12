package org.fedoraproject.javadeptools.fma;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

public class Rpmlib extends CLibrary {
    private static class Lazy {
        static final Rpmlib INSTANCE = new Rpmlib();
    }

    private Rpmlib() {
        super("rpm");
    }

    private final MethodHandle rpmtsCreate = downcallHandle("rpmtsCreate",
            FunctionDescriptor.of(ValueLayout.ADDRESS));
    private final MethodHandle rpmtsFree = downcallHandle("rpmtsFree",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
    private final MethodHandle rpmtsSetVSFlags = downcallHandle("rpmtsSetVSFlags",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
    private final MethodHandle rpmtsSetRootDir = downcallHandle("rpmtsSetRootDir",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
    private final MethodHandle rpmtsInitIterator = downcallHandle("rpmtsInitIterator",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
    private final MethodHandle rpmdbFreeIterator = downcallHandle("rpmdbFreeIterator",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
    private final MethodHandle rpmdbNextIterator = downcallHandle("rpmdbNextIterator",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
    private final MethodHandle rpmReadPackageFile = downcallHandle("rpmReadPackageFile",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
    private final MethodHandle rpmReadConfigFiles = downcallHandle("rpmReadConfigFiles",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
    private final MethodHandle headerFree = downcallHandle("headerFree",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
    private final MethodHandle headerGet = downcallHandle("headerGet",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
    private final MethodHandle headerGetString = downcallHandle("headerGetString",
            FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
    private final MethodHandle headerGetNumber = downcallHandle("headerGetNumber",
            FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
    private final MethodHandle rpmtdNew = downcallHandle("rpmtdNew",
            FunctionDescriptor.of(ValueLayout.ADDRESS));
    private final MethodHandle rpmtdFree = downcallHandle("rpmtdFree",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
    private final MethodHandle rpmtdFreeData = downcallHandle("rpmtdFreeData",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
    private final MethodHandle rpmtdCount = downcallHandle("rpmtdCount",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
    private final MethodHandle rpmtdNext = downcallHandle("rpmtdNext",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
    private final MethodHandle rpmtdGetChar = downcallHandle("rpmtdGetChar",
            FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS));
    private final MethodHandle rpmtdGetUint16 = downcallHandle("rpmtdGetUint16",
            FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS));
    private final MethodHandle rpmtdGetUint32 = downcallHandle("rpmtdGetUint32",
            FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS));
    private final MethodHandle rpmtdGetUint64 = downcallHandle("rpmtdGetUint64",
            FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS));
    private final MethodHandle rpmtdGetNumber = downcallHandle("rpmtdGetNumber",
            FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
    private final MethodHandle rpmtdGetString = downcallHandle("rpmtdGetString",
            FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS));

    public static final MemorySegment rpmtsCreate() {
        try {
            return (MemorySegment) Lazy.INSTANCE.rpmtsCreate.invokeExact();
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final void rpmtsFree(MemorySegment ts) {
        try {
            Lazy.INSTANCE.rpmtsFree.invokeExact(ts);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmtsSetVSFlags(MemorySegment ts, int flags) {
        try {
            return (int) Lazy.INSTANCE.rpmtsSetVSFlags.invokeExact(ts, flags);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmtsSetRootDir(MemorySegment ts, String rootDir) {
        try (var arena = Arena.openConfined()) {
            return (int) Lazy.INSTANCE.rpmtsSetRootDir.invokeExact(
                    ts,
                    CLibrary.toCString(rootDir, arena));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemorySegment rpmtsInitIterator(MemorySegment ts, int rpmtag, String keyp, long keylen) {
        try (var arena = Arena.openConfined()) {
            return (MemorySegment) Lazy.INSTANCE.rpmtsInitIterator.invokeExact(
                    ts,
                    rpmtag,
                    CLibrary.toCString(keyp, arena),
                    keylen);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemorySegment rpmdbFreeIterator(MemorySegment mi) {
        try {
            return (MemorySegment) Lazy.INSTANCE.rpmdbFreeIterator.invokeExact(mi);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemorySegment rpmdbNextIterator(MemorySegment mi) {
        try {
            return (MemorySegment) Lazy.INSTANCE.rpmdbNextIterator.invokeExact(mi);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmReadPackageFile(MemorySegment ts, MemorySegment fd, MemorySegment fn, MemorySegment hdr) {
        try {
            return (int) Lazy.INSTANCE.rpmReadPackageFile.invokeExact(ts, fd, fn, hdr);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmReadConfigFiles(String file, String target) {
        try (var fileArena = Arena.openConfined();
             var targetArena = Arena.openConfined()) {
            return (int) Lazy.INSTANCE.rpmReadConfigFiles.invokeExact(
                    CLibrary.toCString(file, fileArena),
                    CLibrary.toCString(target, targetArena));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemorySegment headerFree(MemorySegment hdr) {
        try {
            return (MemorySegment) Lazy.INSTANCE.headerFree.invokeExact(hdr);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int headerGet(MemorySegment hdr, int tag, MemorySegment td, int flags) {
        try {
            return (int) Lazy.INSTANCE.headerGet.invokeExact(hdr, tag, td, flags);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final String headerGetString(MemorySegment hdr, int tag) {
        try {
            return CLibrary.toJavaString((MemorySegment) Lazy.INSTANCE.headerGetString.invokeExact(hdr, tag));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final long headerGetNumber(MemorySegment hdr, int tag) {
        try {
            return (long) Lazy.INSTANCE.headerGetNumber.invokeExact(hdr, tag);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemorySegment rpmtdNew() {
        try {
            return (MemorySegment) Lazy.INSTANCE.rpmtdNew.invokeExact();
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemorySegment rpmtdFree(MemorySegment td) {
        try {
            return (MemorySegment) Lazy.INSTANCE.rpmtdFree.invokeExact(td);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final void rpmtdFreeData(MemorySegment td) {
        try {
            Lazy.INSTANCE.rpmtdFreeData.invokeExact(td);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmtdCount(MemorySegment td) {
        try {
            return (int) Lazy.INSTANCE.rpmtdCount.invokeExact(td);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmtdNext(MemorySegment td) {
        try {
            return (int) Lazy.INSTANCE.rpmtdNext.invokeExact(td);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final char rpmtdGetChar(MemorySegment td) {
        try {
            return (char) ((MemorySegment) Lazy.INSTANCE.rpmtdGetChar.invokeExact(td)).get(ValueLayout.JAVA_BYTE, 0);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final short rpmtdGetUint16(MemorySegment td) {
        try {
            return ((MemorySegment) Lazy.INSTANCE.rpmtdGetUint16.invokeExact(td)).get(ValueLayout.JAVA_SHORT, 0);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmtdGetUint32(MemorySegment td) {
        try {
            return ((MemorySegment) Lazy.INSTANCE.rpmtdGetUint32.invokeExact(td)).get(ValueLayout.JAVA_INT, 0);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final long rpmtdGetUint64(MemorySegment td) {
        try {
            return ((MemorySegment) Lazy.INSTANCE.rpmtdGetUint64.invokeExact(td)).get(ValueLayout.JAVA_INT, 0);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final long rpmtdGetNumber(MemorySegment td) {
        try {
            return ((long) Lazy.INSTANCE.rpmtdGetNumber.invokeExact(td));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final String rpmtdGetString(MemorySegment td) {
        try {
            return CLibrary.toJavaString((MemorySegment) Lazy.INSTANCE.rpmtdGetString.invokeExact(td));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }
}
