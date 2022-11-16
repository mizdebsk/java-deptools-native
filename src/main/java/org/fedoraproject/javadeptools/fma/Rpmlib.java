package org.fedoraproject.javadeptools.fma;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.ResourceScope;

public class Rpmlib extends CLibrary {
    private static class Lazy {
        static final Rpmlib INSTANCE = new Rpmlib();
    }

    private Rpmlib() {
        super("rpm");
    }

    private final MethodHandle rpmtsCreate = downcallHandle("rpmtsCreate",
            MethodType.methodType(MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER));
    private final MethodHandle rpmtsFree = downcallHandle("rpmtsFree",
            MethodType.methodType(void.class, MemoryAddress.class),
            FunctionDescriptor.ofVoid(CLinker.C_POINTER));
    private final MethodHandle rpmtsSetVSFlags = downcallHandle("rpmtsSetVSFlags",
            MethodType.methodType(int.class, MemoryAddress.class, int.class),
            FunctionDescriptor.of(CLinker.C_INT, CLinker.C_POINTER, CLinker.C_INT));
    private final MethodHandle rpmtsSetRootDir = downcallHandle("rpmtsSetRootDir",
            MethodType.methodType(int.class, MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_INT, CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle rpmtsInitIterator = downcallHandle("rpmtsInitIterator",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class, int.class, MemoryAddress.class, long.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER, Layouts.int32_t, CLinker.C_POINTER, Layouts.size_t));
    private final MethodHandle rpmdbFreeIterator = downcallHandle("rpmdbFreeIterator",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle rpmdbNextIterator = downcallHandle("rpmdbNextIterator",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle rpmReadPackageFile = downcallHandle("rpmReadPackageFile",
            MethodType.methodType(int.class, MemoryAddress.class, MemoryAddress.class, MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_INT, CLinker.C_POINTER, CLinker.C_POINTER, CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle rpmReadConfigFiles = downcallHandle("rpmReadConfigFiles",
            MethodType.methodType(int.class, MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_INT, CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle headerFree = downcallHandle("headerFree",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle headerGet = downcallHandle("headerGet",
            MethodType.methodType(int.class, MemoryAddress.class, int.class, MemoryAddress.class, int.class),
            FunctionDescriptor.of(CLinker.C_INT, CLinker.C_POINTER, CLinker.C_INT, CLinker.C_POINTER, CLinker.C_INT));
    private final MethodHandle headerGetString = downcallHandle("headerGetString",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class, int.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER, CLinker.C_INT));
    private final MethodHandle headerGetNumber = downcallHandle("headerGetNumber",
            MethodType.methodType(long.class, MemoryAddress.class, int.class),
            FunctionDescriptor.of(Layouts.int64_t, CLinker.C_POINTER, CLinker.C_INT));
    private final MethodHandle rpmtdNew = downcallHandle("rpmtdNew",
            MethodType.methodType(MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER));
    private final MethodHandle rpmtdFree = downcallHandle("rpmtdFree",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle rpmtdFreeData = downcallHandle("rpmtdFreeData",
            MethodType.methodType(void.class, MemoryAddress.class),
            FunctionDescriptor.ofVoid(CLinker.C_POINTER));
    private final MethodHandle rpmtdCount = downcallHandle("rpmtdCount",
            MethodType.methodType(int.class, MemoryAddress.class),
            FunctionDescriptor.of(Layouts.int32_t, CLinker.C_POINTER));
    private final MethodHandle rpmtdNext = downcallHandle("rpmtdNext",
            MethodType.methodType(int.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_INT, CLinker.C_POINTER));
    private final MethodHandle rpmtdGetChar = downcallHandle("rpmtdGetChar",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle rpmtdGetUint16 = downcallHandle("rpmtdGetUint16",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle rpmtdGetUint32 = downcallHandle("rpmtdGetUint32",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle rpmtdGetUint64 = downcallHandle("rpmtdGetUint64",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER));
    private final MethodHandle rpmtdGetString = downcallHandle("rpmtdGetString",
            MethodType.methodType(MemoryAddress.class, MemoryAddress.class),
            FunctionDescriptor.of(CLinker.C_POINTER, CLinker.C_POINTER));

    public static final MemoryAddress rpmtsCreate() {
        try {
            return (MemoryAddress) Lazy.INSTANCE.rpmtsCreate.invokeExact();
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final void rpmtsFree(MemoryAddress ts) {
        try {
            Lazy.INSTANCE.rpmtsFree.invokeExact(ts);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmtsSetVSFlags(MemoryAddress ts, int flags) {
        try {
            return (int) Lazy.INSTANCE.rpmtsSetVSFlags.invokeExact(ts, flags);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmtsSetRootDir(MemoryAddress ts, String rootDir) {
        try (var rootDirScope = ResourceScope.newConfinedScope()) {
            return (int) Lazy.INSTANCE.rpmtsSetRootDir.invokeExact(
                    ts,
                    CLibrary.toCStringAddress(rootDir, rootDirScope));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemoryAddress rpmtsInitIterator(MemoryAddress ts, int rpmtag, String keyp, long keylen) {
        try (var keypScope = ResourceScope.newConfinedScope()) {
            return (MemoryAddress) Lazy.INSTANCE.rpmtsInitIterator.invokeExact(
                    ts,
                    rpmtag,
                    CLibrary.toCStringAddress(keyp, keypScope),
                    keylen);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemoryAddress rpmdbFreeIterator(MemoryAddress mi) {
        try {
            return (MemoryAddress) Lazy.INSTANCE.rpmdbFreeIterator.invokeExact(mi);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemoryAddress rpmdbNextIterator(MemoryAddress mi) {
        try {
            return (MemoryAddress) Lazy.INSTANCE.rpmdbNextIterator.invokeExact(mi);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmReadPackageFile(MemoryAddress ts, MemoryAddress fd, MemoryAddress fn, MemoryAddress hdr) {
        try {
            return (int) Lazy.INSTANCE.rpmReadPackageFile.invokeExact(ts, fd, fn, hdr);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmReadConfigFiles(String file, String target) {
        try (var fileScope = ResourceScope.newConfinedScope();
             var targetScope = ResourceScope.newConfinedScope()) {
            return (int) Lazy.INSTANCE.rpmReadConfigFiles.invokeExact(
                    CLibrary.toCStringAddress(file, fileScope),
                    CLibrary.toCStringAddress(target, targetScope));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemoryAddress headerFree(MemoryAddress hdr) {
        try {
            return (MemoryAddress) Lazy.INSTANCE.headerFree.invokeExact(hdr);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int headerGet(MemoryAddress hdr, int tag, MemoryAddress td, int flags) {
        try {
            return (int) Lazy.INSTANCE.headerGet.invokeExact(hdr, tag, td, flags);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final String headerGetString(MemoryAddress hdr, int tag) {
        try {
            var cstring = (MemoryAddress) Lazy.INSTANCE.headerGetString.invokeExact(hdr, tag);
            if (cstring.equals(MemoryAddress.NULL)) {
                return null;
            } else {
                return CLinker.toJavaString(cstring);
            }
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final long headerGetNumber(MemoryAddress hdr, int tag) {
        try {
            return (long) Lazy.INSTANCE.headerGetNumber.invokeExact(hdr, tag);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemoryAddress rpmtdNew() {
        try {
            return (MemoryAddress) Lazy.INSTANCE.rpmtdNew.invokeExact();
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final MemoryAddress rpmtdFree(MemoryAddress td) {
        try {
            return (MemoryAddress) Lazy.INSTANCE.rpmtdFree.invokeExact(td);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final void rpmtdFreeData(MemoryAddress td) {
        try {
            Lazy.INSTANCE.rpmtdFreeData.invokeExact(td);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmtdCount(MemoryAddress td) {
        try {
            return (int) Lazy.INSTANCE.rpmtdCount.invokeExact(td);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmtdNext(MemoryAddress td) {
        try {
            return (int) Lazy.INSTANCE.rpmtdNext.invokeExact(td);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final char rpmtdGetChar(MemoryAddress td) {
        try {
            return ((MemoryAddress) Lazy.INSTANCE.rpmtdGetChar.invokeExact(td)).asSegment(1, ResourceScope.globalScope()).toCharArray()[0];
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final short rpmtdGetUint16(MemoryAddress td) {
        try {
            return ((MemoryAddress) Lazy.INSTANCE.rpmtdGetUint16.invokeExact(td)).asSegment(CLinker.C_SHORT.bitSize() / CLinker.C_CHAR.bitSize(), ResourceScope.globalScope()).toShortArray()[0];
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final int rpmtdGetUint32(MemoryAddress td) {
        try {
            return ((MemoryAddress) Lazy.INSTANCE.rpmtdGetUint32.invokeExact(td)).asSegment(Layouts.int32_t.bitSize() / CLinker.C_CHAR.bitSize(), ResourceScope.globalScope()).toIntArray()[0];
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final long rpmtdGetUint64(MemoryAddress td) {
        try {
            return ((MemoryAddress) Lazy.INSTANCE.rpmtdGetUint64.invokeExact(td)).asSegment(Layouts.int64_t.bitSize() / CLinker.C_CHAR.bitSize(), ResourceScope.globalScope()).toLongArray()[0];
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public static final String rpmtdGetString(MemoryAddress td) {
        try {
            return CLinker.toJavaString((MemoryAddress) Lazy.INSTANCE.rpmtdGetString.invokeExact(td));
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }
}
