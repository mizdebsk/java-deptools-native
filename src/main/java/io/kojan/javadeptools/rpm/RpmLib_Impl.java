package io.kojan.javadeptools.rpm;

import io.kojan.javadeptools.nativ.AbstractNativeProxy;
import io.kojan.javadeptools.nativ.NativePointer;
import io.kojan.javadeptools.rpm.Rpm.RpmDS;
import io.kojan.javadeptools.rpm.Rpm.RpmFD;
import io.kojan.javadeptools.rpm.Rpm.RpmFI;
import io.kojan.javadeptools.rpm.Rpm.RpmFiles;
import io.kojan.javadeptools.rpm.Rpm.RpmHeader;
import io.kojan.javadeptools.rpm.Rpm.RpmLib;
import io.kojan.javadeptools.rpm.Rpm.RpmMI;
import io.kojan.javadeptools.rpm.Rpm.RpmStrPool;
import io.kojan.javadeptools.rpm.Rpm.RpmTD;
import io.kojan.javadeptools.rpm.Rpm.RpmTS;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;

/**
 * Native implementation of RpmLib.
 */
final class RpmLib_Impl extends AbstractNativeProxy implements RpmLib {
    public RpmLib_Impl(SymbolLookup lookup) {
        super(lookup);
    }

    /**
     * Method stub that invokes native method {@code headerFree}.
     * @param h RpmHeader
     */
    @Override
    public void headerFree(RpmHeader h) {
        try {
            mh_headerFree.invokeExact(
                    downConvertObject(h)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function headerFree", _t);
        }
    }

    private final MethodHandle mh_headerFree = makeMethodHandle(VOID, "headerFree", OBJ);

    /**
     * Method stub that invokes native method {@code headerGet}.
     * @param h RpmHeader
     * @param tag int
     * @param td RpmTD
     * @param flags int
     * @return int
     */
    @Override
    public int headerGet(RpmHeader h, int tag, RpmTD td, int flags) {
        try {
            return (int)mh_headerGet.invokeExact(
                    downConvertObject(h),
                    tag,
                    downConvertObject(td),
                    flags
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function headerGet", _t);
        }
    }

    private final MethodHandle mh_headerGet = makeMethodHandle(INT, "headerGet", OBJ, INT, OBJ, INT);

    /**
     * Method stub that invokes native method {@code headerGetNumber}.
     * @param h RpmHeader
     * @param tag int
     * @return long
     */
    @Override
    public long headerGetNumber(RpmHeader h, int tag) {
        try {
            return (long)mh_headerGetNumber.invokeExact(
                    downConvertObject(h),
                    tag
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function headerGetNumber", _t);
        }
    }

    private final MethodHandle mh_headerGetNumber = makeMethodHandle(LONG, "headerGetNumber", OBJ, INT);

    /**
     * Method stub that invokes native method {@code headerGetString}.
     * @param h RpmHeader
     * @param tag int
     * @return String
     */
    @Override
    public String headerGetString(RpmHeader h, int tag) {
        try {
            return upConvertString((MemorySegment)mh_headerGetString.invokeExact(
                    downConvertObject(h),
                    tag
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function headerGetString", _t);
        }
    }

    private final MethodHandle mh_headerGetString = makeMethodHandle(STR, "headerGetString", OBJ, INT);

    /**
     * Method stub that invokes native method {@code rpmReadConfigFiles}.
     * @param file String
     * @param target String
     * @return int
     */
    @Override
    public int rpmReadConfigFiles(String file, String target) {
        try (Arena arena = Arena.ofConfined()) {
            return (int)mh_rpmReadConfigFiles.invokeExact(
                    downConvertString(file, arena),
                    downConvertString(target, arena)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmReadConfigFiles", _t);
        }
    }

    private final MethodHandle mh_rpmReadConfigFiles = makeMethodHandle(INT, "rpmReadConfigFiles", STR, STR);

    /**
     * Method stub that invokes native method {@code rpmReadPackageFile}.
     * @param ts RpmTS
     * @param fd RpmFD
     * @param fn String
     * @param hdrp NativePointer
     * @return int
     */
    @Override
    public int rpmReadPackageFile(RpmTS ts, RpmFD fd, String fn, NativePointer hdrp) {
        try (Arena arena = Arena.ofConfined()) {
            return (int)mh_rpmReadPackageFile.invokeExact(
                    downConvertObject(ts),
                    downConvertObject(fd),
                    downConvertString(fn, arena),
                    downConvertObject(hdrp)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmReadPackageFile", _t);
        }
    }

    private final MethodHandle mh_rpmReadPackageFile = makeMethodHandle(INT, "rpmReadPackageFile", OBJ, OBJ, STR, OBJ);

    /**
     * Method stub that invokes native method {@code rpmdbFreeIterator}.
     * @param mi RpmMI
     */
    @Override
    public void rpmdbFreeIterator(RpmMI mi) {
        try {
            mh_rpmdbFreeIterator.invokeExact(
                    downConvertObject(mi)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdbFreeIterator", _t);
        }
    }

    private final MethodHandle mh_rpmdbFreeIterator = makeMethodHandle(VOID, "rpmdbFreeIterator", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdbNextIterator}.
     * @param mi RpmMI
     * @return RpmHeader
     */
    @Override
    public RpmHeader rpmdbNextIterator(RpmMI mi) {
        try {
            return upConvertObject(RpmHeader::new, (MemorySegment)mh_rpmdbNextIterator.invokeExact(
                    downConvertObject(mi)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdbNextIterator", _t);
        }
    }

    private final MethodHandle mh_rpmdbNextIterator = makeMethodHandle(OBJ, "rpmdbNextIterator", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsDNEVR}.
     * @param ds RpmDS
     * @return String
     */
    @Override
    public String rpmdsDNEVR(RpmDS ds) {
        try {
            return upConvertString((MemorySegment)mh_rpmdsDNEVR.invokeExact(
                    downConvertObject(ds)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsDNEVR", _t);
        }
    }

    private final MethodHandle mh_rpmdsDNEVR = makeMethodHandle(STR, "rpmdsDNEVR", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsEVR}.
     * @param ds RpmDS
     * @return String
     */
    @Override
    public String rpmdsEVR(RpmDS ds) {
        try {
            return upConvertString((MemorySegment)mh_rpmdsEVR.invokeExact(
                    downConvertObject(ds)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsEVR", _t);
        }
    }

    private final MethodHandle mh_rpmdsEVR = makeMethodHandle(STR, "rpmdsEVR", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsFlags}.
     * @param ds RpmDS
     * @return int
     */
    @Override
    public int rpmdsFlags(RpmDS ds) {
        try {
            return (int)mh_rpmdsFlags.invokeExact(
                    downConvertObject(ds)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsFlags", _t);
        }
    }

    private final MethodHandle mh_rpmdsFlags = makeMethodHandle(INT, "rpmdsFlags", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsFree}.
     * @param ds RpmDS
     */
    @Override
    public void rpmdsFree(RpmDS ds) {
        try {
            mh_rpmdsFree.invokeExact(
                    downConvertObject(ds)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsFree", _t);
        }
    }

    private final MethodHandle mh_rpmdsFree = makeMethodHandle(VOID, "rpmdsFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsIsRich}.
     * @param ds RpmDS
     * @return int
     */
    @Override
    public int rpmdsIsRich(RpmDS ds) {
        try {
            return (int)mh_rpmdsIsRich.invokeExact(
                    downConvertObject(ds)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsIsRich", _t);
        }
    }

    private final MethodHandle mh_rpmdsIsRich = makeMethodHandle(INT, "rpmdsIsRich", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsN}.
     * @param ds RpmDS
     * @return String
     */
    @Override
    public String rpmdsN(RpmDS ds) {
        try {
            return upConvertString((MemorySegment)mh_rpmdsN.invokeExact(
                    downConvertObject(ds)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsN", _t);
        }
    }

    private final MethodHandle mh_rpmdsN = makeMethodHandle(STR, "rpmdsN", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsNew}.
     * @param h RpmHeader
     * @param tagN int
     * @param flags int
     * @return RpmDS
     */
    @Override
    public RpmDS rpmdsNew(RpmHeader h, int tagN, int flags) {
        try {
            return upConvertObject(RpmDS::new, (MemorySegment)mh_rpmdsNew.invokeExact(
                    downConvertObject(h),
                    tagN,
                    flags
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsNew", _t);
        }
    }

    private final MethodHandle mh_rpmdsNew = makeMethodHandle(OBJ, "rpmdsNew", OBJ, INT, INT);

    /**
     * Method stub that invokes native method {@code rpmdsNext}.
     * @param ds RpmDS
     * @return int
     */
    @Override
    public int rpmdsNext(RpmDS ds) {
        try {
            return (int)mh_rpmdsNext.invokeExact(
                    downConvertObject(ds)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsNext", _t);
        }
    }

    private final MethodHandle mh_rpmdsNext = makeMethodHandle(INT, "rpmdsNext", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiBN}.
     * @param fi RpmFI
     * @return String
     */
    @Override
    public String rpmfiBN(RpmFI fi) {
        try {
            return upConvertString((MemorySegment)mh_rpmfiBN.invokeExact(
                    downConvertObject(fi)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiBN", _t);
        }
    }

    private final MethodHandle mh_rpmfiBN = makeMethodHandle(STR, "rpmfiBN", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiDN}.
     * @param fi RpmFI
     * @return String
     */
    @Override
    public String rpmfiDN(RpmFI fi) {
        try {
            return upConvertString((MemorySegment)mh_rpmfiDN.invokeExact(
                    downConvertObject(fi)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiDN", _t);
        }
    }

    private final MethodHandle mh_rpmfiDN = makeMethodHandle(STR, "rpmfiDN", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiFree}.
     * @param fi RpmFI
     */
    @Override
    public void rpmfiFree(RpmFI fi) {
        try {
            mh_rpmfiFree.invokeExact(
                    downConvertObject(fi)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiFree", _t);
        }
    }

    private final MethodHandle mh_rpmfiFree = makeMethodHandle(VOID, "rpmfiFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiNext}.
     * @param fi RpmFI
     * @return int
     */
    @Override
    public int rpmfiNext(RpmFI fi) {
        try {
            return (int)mh_rpmfiNext.invokeExact(
                    downConvertObject(fi)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiNext", _t);
        }
    }

    private final MethodHandle mh_rpmfiNext = makeMethodHandle(INT, "rpmfiNext", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfilesFree}.
     * @param fi RpmFiles
     */
    @Override
    public void rpmfilesFree(RpmFiles fi) {
        try {
            mh_rpmfilesFree.invokeExact(
                    downConvertObject(fi)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfilesFree", _t);
        }
    }

    private final MethodHandle mh_rpmfilesFree = makeMethodHandle(VOID, "rpmfilesFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfilesIter}.
     * @param files RpmFiles
     * @param itype int
     * @return RpmFI
     */
    @Override
    public RpmFI rpmfilesIter(RpmFiles files, int itype) {
        try {
            return upConvertObject(RpmFI::new, (MemorySegment)mh_rpmfilesIter.invokeExact(
                    downConvertObject(files),
                    itype
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfilesIter", _t);
        }
    }

    private final MethodHandle mh_rpmfilesIter = makeMethodHandle(OBJ, "rpmfilesIter", OBJ, INT);

    /**
     * Method stub that invokes native method {@code rpmfilesNew}.
     * @param pool RpmStrPool
     * @param h RpmHeader
     * @param tagN int
     * @param flags int
     * @return RpmFiles
     */
    @Override
    public RpmFiles rpmfilesNew(RpmStrPool pool, RpmHeader h, int tagN, int flags) {
        try {
            return upConvertObject(RpmFiles::new, (MemorySegment)mh_rpmfilesNew.invokeExact(
                    downConvertObject(pool),
                    downConvertObject(h),
                    tagN,
                    flags
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfilesNew", _t);
        }
    }

    private final MethodHandle mh_rpmfilesNew = makeMethodHandle(OBJ, "rpmfilesNew", OBJ, OBJ, INT, INT);

    /**
     * Method stub that invokes native method {@code rpmstrPoolCreate}.
     * @return RpmStrPool
     */
    @Override
    public RpmStrPool rpmstrPoolCreate() {
        try {
            return upConvertObject(RpmStrPool::new, (MemorySegment)mh_rpmstrPoolCreate.invokeExact(
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmstrPoolCreate", _t);
        }
    }

    private final MethodHandle mh_rpmstrPoolCreate = makeMethodHandle(OBJ, "rpmstrPoolCreate");

    /**
     * Method stub that invokes native method {@code rpmstrPoolFree}.
     * @param pool RpmStrPool
     */
    @Override
    public void rpmstrPoolFree(RpmStrPool pool) {
        try {
            mh_rpmstrPoolFree.invokeExact(
                    downConvertObject(pool)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmstrPoolFree", _t);
        }
    }

    private final MethodHandle mh_rpmstrPoolFree = makeMethodHandle(VOID, "rpmstrPoolFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtdCount}.
     * @param td RpmTD
     * @return int
     */
    @Override
    public int rpmtdCount(RpmTD td) {
        try {
            return (int)mh_rpmtdCount.invokeExact(
                    downConvertObject(td)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdCount", _t);
        }
    }

    private final MethodHandle mh_rpmtdCount = makeMethodHandle(INT, "rpmtdCount", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtdFree}.
     * @param td RpmTD
     */
    @Override
    public void rpmtdFree(RpmTD td) {
        try {
            mh_rpmtdFree.invokeExact(
                    downConvertObject(td)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdFree", _t);
        }
    }

    private final MethodHandle mh_rpmtdFree = makeMethodHandle(VOID, "rpmtdFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtdGetNumber}.
     * @param td RpmTD
     * @return long
     */
    @Override
    public long rpmtdGetNumber(RpmTD td) {
        try {
            return (long)mh_rpmtdGetNumber.invokeExact(
                    downConvertObject(td)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdGetNumber", _t);
        }
    }

    private final MethodHandle mh_rpmtdGetNumber = makeMethodHandle(LONG, "rpmtdGetNumber", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtdGetString}.
     * @param td RpmTD
     * @return String
     */
    @Override
    public String rpmtdGetString(RpmTD td) {
        try {
            return upConvertString((MemorySegment)mh_rpmtdGetString.invokeExact(
                    downConvertObject(td)
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdGetString", _t);
        }
    }

    private final MethodHandle mh_rpmtdGetString = makeMethodHandle(STR, "rpmtdGetString", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtdNew}.
     * @return RpmTD
     */
    @Override
    public RpmTD rpmtdNew() {
        try {
            return upConvertObject(RpmTD::new, (MemorySegment)mh_rpmtdNew.invokeExact(
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdNew", _t);
        }
    }

    private final MethodHandle mh_rpmtdNew = makeMethodHandle(OBJ, "rpmtdNew");

    /**
     * Method stub that invokes native method {@code rpmtdNext}.
     * @param td RpmTD
     * @return int
     */
    @Override
    public int rpmtdNext(RpmTD td) {
        try {
            return (int)mh_rpmtdNext.invokeExact(
                    downConvertObject(td)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdNext", _t);
        }
    }

    private final MethodHandle mh_rpmtdNext = makeMethodHandle(INT, "rpmtdNext", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtsCreate}.
     * @return RpmTS
     */
    @Override
    public RpmTS rpmtsCreate() {
        try {
            return upConvertObject(RpmTS::new, (MemorySegment)mh_rpmtsCreate.invokeExact(
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtsCreate", _t);
        }
    }

    private final MethodHandle mh_rpmtsCreate = makeMethodHandle(OBJ, "rpmtsCreate");

    /**
     * Method stub that invokes native method {@code rpmtsFree}.
     * @param ts RpmTS
     */
    @Override
    public void rpmtsFree(RpmTS ts) {
        try {
            mh_rpmtsFree.invokeExact(
                    downConvertObject(ts)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtsFree", _t);
        }
    }

    private final MethodHandle mh_rpmtsFree = makeMethodHandle(VOID, "rpmtsFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtsInitIterator}.
     * @param ts RpmTS
     * @param rpmtag int
     * @param keyp String
     * @param keylen long
     * @return RpmMI
     */
    @Override
    public RpmMI rpmtsInitIterator(RpmTS ts, int rpmtag, String keyp, long keylen) {
        try (Arena arena = Arena.ofConfined()) {
            return upConvertObject(RpmMI::new, (MemorySegment)mh_rpmtsInitIterator.invokeExact(
                    downConvertObject(ts),
                    rpmtag,
                    downConvertString(keyp, arena),
                    keylen
            ));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtsInitIterator", _t);
        }
    }

    private final MethodHandle mh_rpmtsInitIterator = makeMethodHandle(OBJ, "rpmtsInitIterator", OBJ, INT, STR, LONG);

    /**
     * Method stub that invokes native method {@code rpmtsSetRootDir}.
     * @param ts RpmTS
     * @param rootDir String
     * @return int
     */
    @Override
    public int rpmtsSetRootDir(RpmTS ts, String rootDir) {
        try (Arena arena = Arena.ofConfined()) {
            return (int)mh_rpmtsSetRootDir.invokeExact(
                    downConvertObject(ts),
                    downConvertString(rootDir, arena)
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtsSetRootDir", _t);
        }
    }

    private final MethodHandle mh_rpmtsSetRootDir = makeMethodHandle(INT, "rpmtsSetRootDir", OBJ, STR);

    /**
     * Method stub that invokes native method {@code rpmtsSetVSFlags}.
     * @param ts RpmTS
     * @param vsflags int
     */
    @Override
    public void rpmtsSetVSFlags(RpmTS ts, int vsflags) {
        try {
            mh_rpmtsSetVSFlags.invokeExact(
                    downConvertObject(ts),
                    vsflags
            );
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtsSetVSFlags", _t);
        }
    }

    private final MethodHandle mh_rpmtsSetVSFlags = makeMethodHandle(VOID, "rpmtsSetVSFlags", OBJ, INT);

    public static Iterable<FunctionDescriptor> getFunctionLayouts() {
        return Arrays.asList(
                FunctionDescriptor.of(INT, OBJ),
                FunctionDescriptor.of(INT, OBJ, INT, OBJ, INT),
                FunctionDescriptor.of(INT, OBJ, OBJ, STR, OBJ),
                FunctionDescriptor.of(INT, OBJ, STR),
                FunctionDescriptor.of(INT, STR, STR),
                FunctionDescriptor.of(LONG, OBJ),
                FunctionDescriptor.of(LONG, OBJ, INT),
                FunctionDescriptor.of(OBJ),
                FunctionDescriptor.of(OBJ, OBJ),
                FunctionDescriptor.of(OBJ, OBJ, INT),
                FunctionDescriptor.of(OBJ, OBJ, INT, INT),
                FunctionDescriptor.of(OBJ, OBJ, INT, STR, LONG),
                FunctionDescriptor.of(OBJ, OBJ, OBJ, INT, INT),
                FunctionDescriptor.of(STR, OBJ),
                FunctionDescriptor.of(STR, OBJ, INT),
                FunctionDescriptor.ofVoid(OBJ),
                FunctionDescriptor.ofVoid(OBJ, INT)
        );
    }
}
