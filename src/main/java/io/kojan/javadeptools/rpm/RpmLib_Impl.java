/*-
 * Copyright (c) 2024 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.kojan.javadeptools.rpm;

import io.kojan.javadeptools.nativ.AbstractNativeProxy;
import io.kojan.javadeptools.nativ.Native;
import io.kojan.javadeptools.nativ.NativePointer;
import io.kojan.javadeptools.rpm.Rpm.RpmDS;
import io.kojan.javadeptools.rpm.Rpm.RpmEVR;
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
import java.nio.Buffer;
import java.util.Arrays;

/** Native implementation of RpmLib. */
final class RpmLib_Impl extends AbstractNativeProxy implements RpmLib {
    public RpmLib_Impl(SymbolLookup lookup) {
        super(lookup);
    }

    /**
     * Method stub that invokes native method {@code Fclose}.
     *
     * @param fd RpmFD
     */
    @Override
    public void Fclose(RpmFD fd) {
        try {
            mh_Fclose.invokeExact(downConvertObject(fd));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function Fclose", _t);
        }
    }

    private final MethodHandle mh_Fclose = makeMethodHandle(VOID, "Fclose", OBJ);

    /**
     * Method stub that invokes native method {@code Fdopen}.
     *
     * @param ofd RpmFD
     * @param mode String
     * @return RpmFD
     */
    @Override
    public RpmFD Fdopen(RpmFD ofd, String mode) {
        try (Arena arena = Arena.ofConfined()) {
            return upConvertObject(
                    RpmFD::new,
                    (MemorySegment)
                            mh_Fdopen.invokeExact(
                                    downConvertObject(ofd), downConvertString(mode, arena)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function Fdopen", _t);
        }
    }

    private final MethodHandle mh_Fdopen = makeMethodHandle(OBJ, "Fdopen", OBJ, STR);

    /**
     * Method stub that invokes native method {@code Ferror}.
     *
     * @param fd RpmFD
     * @return int
     */
    @Override
    public int Ferror(RpmFD fd) {
        try {
            return (int) mh_Ferror.invokeExact(downConvertObject(fd));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function Ferror", _t);
        }
    }

    private final MethodHandle mh_Ferror = makeMethodHandle(INT, "Ferror", OBJ);

    /**
     * Method stub that invokes native method {@code Fopen}.
     *
     * @param path String
     * @param mode String
     * @return RpmFD
     */
    @Override
    public RpmFD Fopen(String path, String mode) {
        try (Arena arena = Arena.ofConfined()) {
            return upConvertObject(
                    RpmFD::new,
                    (MemorySegment)
                            mh_Fopen.invokeExact(
                                    downConvertString(path, arena),
                                    downConvertString(mode, arena)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function Fopen", _t);
        }
    }

    private final MethodHandle mh_Fopen = makeMethodHandle(OBJ, "Fopen", STR, STR);

    /**
     * Method stub that invokes native method {@code Fstrerror}.
     *
     * @param fd RpmFD
     * @return String
     */
    @Override
    public String Fstrerror(RpmFD fd) {
        try {
            return upConvertString((MemorySegment) mh_Fstrerror.invokeExact(downConvertObject(fd)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function Fstrerror", _t);
        }
    }

    private final MethodHandle mh_Fstrerror = makeMethodHandle(STR, "Fstrerror", OBJ);

    /**
     * Method stub that invokes native method {@code Ftell}.
     *
     * @param fd RpmFD
     * @return long
     */
    @Override
    public long Ftell(RpmFD fd) {
        try {
            return (long) mh_Ftell.invokeExact(downConvertObject(fd));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function Ftell", _t);
        }
    }

    private final MethodHandle mh_Ftell = makeMethodHandle(LONG, "Ftell", OBJ);

    /**
     * Method stub that invokes native method {@code gnu_dev_major}.
     *
     * @param dev int
     * @return int
     */
    @Override
    public int gnu_dev_major(int dev) {
        try {
            return (int) mh_gnu_dev_major.invokeExact(dev);
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function gnu_dev_major", _t);
        }
    }

    private final MethodHandle mh_gnu_dev_major = makeMethodHandle(INT, "gnu_dev_major", INT);

    /**
     * Method stub that invokes native method {@code gnu_dev_minor}.
     *
     * @param dev int
     * @return int
     */
    @Override
    public int gnu_dev_minor(int dev) {
        try {
            return (int) mh_gnu_dev_minor.invokeExact(dev);
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function gnu_dev_minor", _t);
        }
    }

    private final MethodHandle mh_gnu_dev_minor = makeMethodHandle(INT, "gnu_dev_minor", INT);

    /**
     * Method stub that invokes native method {@code headerFree}.
     *
     * @param h RpmHeader
     */
    @Override
    public void headerFree(RpmHeader h) {
        try {
            mh_headerFree.invokeExact(downConvertObject(h));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function headerFree", _t);
        }
    }

    private final MethodHandle mh_headerFree = makeMethodHandle(VOID, "headerFree", OBJ);

    /**
     * Method stub that invokes native method {@code headerGet}.
     *
     * @param h RpmHeader
     * @param tag int
     * @param td RpmTD
     * @param flags int
     * @return int
     */
    @Override
    public int headerGet(RpmHeader h, int tag, RpmTD td, int flags) {
        try {
            return (int)
                    mh_headerGet.invokeExact(
                            downConvertObject(h), tag, downConvertObject(td), flags);
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function headerGet", _t);
        }
    }

    private final MethodHandle mh_headerGet =
            makeMethodHandle(INT, "headerGet", OBJ, INT, OBJ, INT);

    /**
     * Method stub that invokes native method {@code headerGetNumber}.
     *
     * @param h RpmHeader
     * @param tag int
     * @return long
     */
    @Override
    public long headerGetNumber(RpmHeader h, int tag) {
        try {
            return (long) mh_headerGetNumber.invokeExact(downConvertObject(h), tag);
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function headerGetNumber", _t);
        }
    }

    private final MethodHandle mh_headerGetNumber =
            makeMethodHandle(LONG, "headerGetNumber", OBJ, INT);

    /**
     * Method stub that invokes native method {@code headerGetString}.
     *
     * @param h RpmHeader
     * @param tag int
     * @return String
     */
    @Override
    public String headerGetString(RpmHeader h, int tag) {
        try {
            return upConvertString(
                    (MemorySegment) mh_headerGetString.invokeExact(downConvertObject(h), tag));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function headerGetString", _t);
        }
    }

    private final MethodHandle mh_headerGetString =
            makeMethodHandle(STR, "headerGetString", OBJ, INT);

    /**
     * Method stub that invokes native method {@code rpmReadConfigFiles}.
     *
     * @param file String
     * @param target String
     * @return int
     */
    @Override
    public int rpmReadConfigFiles(String file, String target) {
        try (Arena arena = Arena.ofConfined()) {
            return (int)
                    mh_rpmReadConfigFiles.invokeExact(
                            downConvertString(file, arena), downConvertString(target, arena));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmReadConfigFiles", _t);
        }
    }

    private final MethodHandle mh_rpmReadConfigFiles =
            makeMethodHandle(INT, "rpmReadConfigFiles", STR, STR);

    /**
     * Method stub that invokes native method {@code rpmReadPackageFile}.
     *
     * @param ts RpmTS
     * @param fd RpmFD
     * @param fn String
     * @param hdrp NativePointer
     * @return int
     */
    @Override
    public int rpmReadPackageFile(RpmTS ts, RpmFD fd, String fn, NativePointer hdrp) {
        try (Arena arena = Arena.ofConfined()) {
            return (int)
                    mh_rpmReadPackageFile.invokeExact(
                            downConvertObject(ts),
                            downConvertObject(fd),
                            downConvertString(fn, arena),
                            downConvertObject(hdrp));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmReadPackageFile", _t);
        }
    }

    private final MethodHandle mh_rpmReadPackageFile =
            makeMethodHandle(INT, "rpmReadPackageFile", OBJ, OBJ, STR, OBJ);

    /**
     * Method stub that invokes native method {@code rpmdbFreeIterator}.
     *
     * @param mi RpmMI
     */
    @Override
    public void rpmdbFreeIterator(RpmMI mi) {
        try {
            mh_rpmdbFreeIterator.invokeExact(downConvertObject(mi));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdbFreeIterator", _t);
        }
    }

    private final MethodHandle mh_rpmdbFreeIterator =
            makeMethodHandle(VOID, "rpmdbFreeIterator", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdbNextIterator}.
     *
     * @param mi RpmMI
     * @return RpmHeader
     */
    @Override
    public RpmHeader rpmdbNextIterator(RpmMI mi) {
        try {
            return upConvertObject(
                    RpmHeader::new,
                    (MemorySegment) mh_rpmdbNextIterator.invokeExact(downConvertObject(mi)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdbNextIterator", _t);
        }
    }

    private final MethodHandle mh_rpmdbNextIterator =
            makeMethodHandle(OBJ, "rpmdbNextIterator", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsDNEVR}.
     *
     * @param ds RpmDS
     * @return String
     */
    @Override
    public String rpmdsDNEVR(RpmDS ds) {
        try {
            return upConvertString(
                    (MemorySegment) mh_rpmdsDNEVR.invokeExact(downConvertObject(ds)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsDNEVR", _t);
        }
    }

    private final MethodHandle mh_rpmdsDNEVR = makeMethodHandle(STR, "rpmdsDNEVR", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsEVR}.
     *
     * @param ds RpmDS
     * @return String
     */
    @Override
    public String rpmdsEVR(RpmDS ds) {
        try {
            return upConvertString((MemorySegment) mh_rpmdsEVR.invokeExact(downConvertObject(ds)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsEVR", _t);
        }
    }

    private final MethodHandle mh_rpmdsEVR = makeMethodHandle(STR, "rpmdsEVR", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsFlags}.
     *
     * @param ds RpmDS
     * @return int
     */
    @Override
    public int rpmdsFlags(RpmDS ds) {
        try {
            return (int) mh_rpmdsFlags.invokeExact(downConvertObject(ds));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsFlags", _t);
        }
    }

    private final MethodHandle mh_rpmdsFlags = makeMethodHandle(INT, "rpmdsFlags", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsFree}.
     *
     * @param ds RpmDS
     */
    @Override
    public void rpmdsFree(RpmDS ds) {
        try {
            mh_rpmdsFree.invokeExact(downConvertObject(ds));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsFree", _t);
        }
    }

    private final MethodHandle mh_rpmdsFree = makeMethodHandle(VOID, "rpmdsFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsIsRich}.
     *
     * @param ds RpmDS
     * @return int
     */
    @Override
    public int rpmdsIsRich(RpmDS ds) {
        try {
            return (int) mh_rpmdsIsRich.invokeExact(downConvertObject(ds));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsIsRich", _t);
        }
    }

    private final MethodHandle mh_rpmdsIsRich = makeMethodHandle(INT, "rpmdsIsRich", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsN}.
     *
     * @param ds RpmDS
     * @return String
     */
    @Override
    public String rpmdsN(RpmDS ds) {
        try {
            return upConvertString((MemorySegment) mh_rpmdsN.invokeExact(downConvertObject(ds)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsN", _t);
        }
    }

    private final MethodHandle mh_rpmdsN = makeMethodHandle(STR, "rpmdsN", OBJ);

    /**
     * Method stub that invokes native method {@code rpmdsNew}.
     *
     * @param h RpmHeader
     * @param tagN int
     * @param flags int
     * @return RpmDS
     */
    @Override
    public RpmDS rpmdsNew(RpmHeader h, int tagN, int flags) {
        try {
            return upConvertObject(
                    RpmDS::new,
                    (MemorySegment) mh_rpmdsNew.invokeExact(downConvertObject(h), tagN, flags));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsNew", _t);
        }
    }

    private final MethodHandle mh_rpmdsNew = makeMethodHandle(OBJ, "rpmdsNew", OBJ, INT, INT);

    /**
     * Method stub that invokes native method {@code rpmdsNext}.
     *
     * @param ds RpmDS
     * @return int
     */
    @Override
    public int rpmdsNext(RpmDS ds) {
        try {
            return (int) mh_rpmdsNext.invokeExact(downConvertObject(ds));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmdsNext", _t);
        }
    }

    private final MethodHandle mh_rpmdsNext = makeMethodHandle(INT, "rpmdsNext", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiArchiveClose}.
     *
     * @param fi RpmFI
     */
    @Override
    public void rpmfiArchiveClose(RpmFI fi) {
        try {
            mh_rpmfiArchiveClose.invokeExact(downConvertObject(fi));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiArchiveClose", _t);
        }
    }

    private final MethodHandle mh_rpmfiArchiveClose =
            makeMethodHandle(VOID, "rpmfiArchiveClose", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiArchiveHasContent}.
     *
     * @param fi RpmFI
     * @return int
     */
    @Override
    public int rpmfiArchiveHasContent(RpmFI fi) {
        try {
            return (int) mh_rpmfiArchiveHasContent.invokeExact(downConvertObject(fi));
        } catch (Throwable _t) {
            throw new RuntimeException(
                    "Failed to invoke native function rpmfiArchiveHasContent", _t);
        }
    }

    private final MethodHandle mh_rpmfiArchiveHasContent =
            makeMethodHandle(INT, "rpmfiArchiveHasContent", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiArchiveRead}.
     *
     * @param fi RpmFI
     * @param buf Buffer
     * @param size long
     * @return long
     */
    @Override
    public long rpmfiArchiveRead(RpmFI fi, Buffer buf, long size) {
        try {
            return (long)
                    mh_rpmfiArchiveRead.invokeExact(
                            downConvertObject(fi), downConvertBuffer(buf), size);
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiArchiveRead", _t);
        }
    }

    private final MethodHandle mh_rpmfiArchiveRead =
            makeMethodHandle(LONG, "rpmfiArchiveRead", OBJ, BUFF, LONG);

    /**
     * Method stub that invokes native method {@code rpmfiBN}.
     *
     * @param fi RpmFI
     * @return String
     */
    @Override
    public String rpmfiBN(RpmFI fi) {
        try {
            return upConvertString((MemorySegment) mh_rpmfiBN.invokeExact(downConvertObject(fi)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiBN", _t);
        }
    }

    private final MethodHandle mh_rpmfiBN = makeMethodHandle(STR, "rpmfiBN", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiDN}.
     *
     * @param fi RpmFI
     * @return String
     */
    @Override
    public String rpmfiDN(RpmFI fi) {
        try {
            return upConvertString((MemorySegment) mh_rpmfiDN.invokeExact(downConvertObject(fi)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiDN", _t);
        }
    }

    private final MethodHandle mh_rpmfiDN = makeMethodHandle(STR, "rpmfiDN", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiFInode}.
     *
     * @param fi RpmFI
     * @return int
     */
    @Override
    public int rpmfiFInode(RpmFI fi) {
        try {
            return (int) mh_rpmfiFInode.invokeExact(downConvertObject(fi));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiFInode", _t);
        }
    }

    private final MethodHandle mh_rpmfiFInode = makeMethodHandle(INT, "rpmfiFInode", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiFLink}.
     *
     * @param fi RpmFI
     * @return String
     */
    @Override
    public String rpmfiFLink(RpmFI fi) {
        try {
            return upConvertString(
                    (MemorySegment) mh_rpmfiFLink.invokeExact(downConvertObject(fi)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiFLink", _t);
        }
    }

    private final MethodHandle mh_rpmfiFLink = makeMethodHandle(STR, "rpmfiFLink", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiFMode}.
     *
     * @param fi RpmFI
     * @return int
     */
    @Override
    public int rpmfiFMode(RpmFI fi) {
        try {
            return (int) mh_rpmfiFMode.invokeExact(downConvertObject(fi));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiFMode", _t);
        }
    }

    private final MethodHandle mh_rpmfiFMode = makeMethodHandle(INT, "rpmfiFMode", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiFMtime}.
     *
     * @param fi RpmFI
     * @return int
     */
    @Override
    public int rpmfiFMtime(RpmFI fi) {
        try {
            return (int) mh_rpmfiFMtime.invokeExact(downConvertObject(fi));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiFMtime", _t);
        }
    }

    private final MethodHandle mh_rpmfiFMtime = makeMethodHandle(INT, "rpmfiFMtime", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiFNlink}.
     *
     * @param fi RpmFI
     * @return int
     */
    @Override
    public int rpmfiFNlink(RpmFI fi) {
        try {
            return (int) mh_rpmfiFNlink.invokeExact(downConvertObject(fi));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiFNlink", _t);
        }
    }

    private final MethodHandle mh_rpmfiFNlink = makeMethodHandle(INT, "rpmfiFNlink", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiFRdev}.
     *
     * @param fi RpmFI
     * @return int
     */
    @Override
    public int rpmfiFRdev(RpmFI fi) {
        try {
            return (int) mh_rpmfiFRdev.invokeExact(downConvertObject(fi));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiFRdev", _t);
        }
    }

    private final MethodHandle mh_rpmfiFRdev = makeMethodHandle(INT, "rpmfiFRdev", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiFSize}.
     *
     * @param fi RpmFI
     * @return long
     */
    @Override
    public long rpmfiFSize(RpmFI fi) {
        try {
            return (long) mh_rpmfiFSize.invokeExact(downConvertObject(fi));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiFSize", _t);
        }
    }

    private final MethodHandle mh_rpmfiFSize = makeMethodHandle(LONG, "rpmfiFSize", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiFree}.
     *
     * @param fi RpmFI
     */
    @Override
    public void rpmfiFree(RpmFI fi) {
        try {
            mh_rpmfiFree.invokeExact(downConvertObject(fi));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiFree", _t);
        }
    }

    private final MethodHandle mh_rpmfiFree = makeMethodHandle(VOID, "rpmfiFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfiNewArchiveReader}.
     *
     * @param fd RpmFD
     * @param files RpmFiles
     * @param itype int
     * @return RpmFI
     */
    @Override
    public RpmFI rpmfiNewArchiveReader(RpmFD fd, RpmFiles files, int itype) {
        try {
            return upConvertObject(
                    RpmFI::new,
                    (MemorySegment)
                            mh_rpmfiNewArchiveReader.invokeExact(
                                    downConvertObject(fd), downConvertObject(files), itype));
        } catch (Throwable _t) {
            throw new RuntimeException(
                    "Failed to invoke native function rpmfiNewArchiveReader", _t);
        }
    }

    private final MethodHandle mh_rpmfiNewArchiveReader =
            makeMethodHandle(OBJ, "rpmfiNewArchiveReader", OBJ, OBJ, INT);

    /**
     * Method stub that invokes native method {@code rpmfiNext}.
     *
     * @param fi RpmFI
     * @return int
     */
    @Override
    public int rpmfiNext(RpmFI fi) {
        try {
            return (int) mh_rpmfiNext.invokeExact(downConvertObject(fi));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfiNext", _t);
        }
    }

    private final MethodHandle mh_rpmfiNext = makeMethodHandle(INT, "rpmfiNext", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfilesFree}.
     *
     * @param fi RpmFiles
     */
    @Override
    public void rpmfilesFree(RpmFiles fi) {
        try {
            mh_rpmfilesFree.invokeExact(downConvertObject(fi));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfilesFree", _t);
        }
    }

    private final MethodHandle mh_rpmfilesFree = makeMethodHandle(VOID, "rpmfilesFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmfilesIter}.
     *
     * @param files RpmFiles
     * @param itype int
     * @return RpmFI
     */
    @Override
    public RpmFI rpmfilesIter(RpmFiles files, int itype) {
        try {
            return upConvertObject(
                    RpmFI::new,
                    (MemorySegment) mh_rpmfilesIter.invokeExact(downConvertObject(files), itype));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfilesIter", _t);
        }
    }

    private final MethodHandle mh_rpmfilesIter = makeMethodHandle(OBJ, "rpmfilesIter", OBJ, INT);

    /**
     * Method stub that invokes native method {@code rpmfilesNew}.
     *
     * @param pool RpmStrPool
     * @param h RpmHeader
     * @param tagN int
     * @param flags int
     * @return RpmFiles
     */
    @Override
    public RpmFiles rpmfilesNew(RpmStrPool pool, RpmHeader h, int tagN, int flags) {
        try {
            return upConvertObject(
                    RpmFiles::new,
                    (MemorySegment)
                            mh_rpmfilesNew.invokeExact(
                                    downConvertObject(pool), downConvertObject(h), tagN, flags));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmfilesNew", _t);
        }
    }

    private final MethodHandle mh_rpmfilesNew =
            makeMethodHandle(OBJ, "rpmfilesNew", OBJ, OBJ, INT, INT);

    /**
     * Method stub that invokes native method {@code rpmstrPoolCreate}.
     *
     * @return RpmStrPool
     */
    @Override
    public RpmStrPool rpmstrPoolCreate() {
        try {
            return upConvertObject(
                    RpmStrPool::new, (MemorySegment) mh_rpmstrPoolCreate.invokeExact());
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmstrPoolCreate", _t);
        }
    }

    private final MethodHandle mh_rpmstrPoolCreate = makeMethodHandle(OBJ, "rpmstrPoolCreate");

    /**
     * Method stub that invokes native method {@code rpmstrPoolFree}.
     *
     * @param pool RpmStrPool
     */
    @Override
    public void rpmstrPoolFree(RpmStrPool pool) {
        try {
            mh_rpmstrPoolFree.invokeExact(downConvertObject(pool));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmstrPoolFree", _t);
        }
    }

    private final MethodHandle mh_rpmstrPoolFree = makeMethodHandle(VOID, "rpmstrPoolFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtdCount}.
     *
     * @param td RpmTD
     * @return int
     */
    @Override
    public int rpmtdCount(RpmTD td) {
        try {
            return (int) mh_rpmtdCount.invokeExact(downConvertObject(td));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdCount", _t);
        }
    }

    private final MethodHandle mh_rpmtdCount = makeMethodHandle(INT, "rpmtdCount", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtdFree}.
     *
     * @param td RpmTD
     */
    @Override
    public void rpmtdFree(RpmTD td) {
        try {
            mh_rpmtdFree.invokeExact(downConvertObject(td));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdFree", _t);
        }
    }

    private final MethodHandle mh_rpmtdFree = makeMethodHandle(VOID, "rpmtdFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtdGetNumber}.
     *
     * @param td RpmTD
     * @return long
     */
    @Override
    public long rpmtdGetNumber(RpmTD td) {
        try {
            return (long) mh_rpmtdGetNumber.invokeExact(downConvertObject(td));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdGetNumber", _t);
        }
    }

    private final MethodHandle mh_rpmtdGetNumber = makeMethodHandle(LONG, "rpmtdGetNumber", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtdGetString}.
     *
     * @param td RpmTD
     * @return String
     */
    @Override
    public String rpmtdGetString(RpmTD td) {
        try {
            return upConvertString(
                    (MemorySegment) mh_rpmtdGetString.invokeExact(downConvertObject(td)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdGetString", _t);
        }
    }

    private final MethodHandle mh_rpmtdGetString = makeMethodHandle(STR, "rpmtdGetString", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtdNew}.
     *
     * @return RpmTD
     */
    @Override
    public RpmTD rpmtdNew() {
        try {
            return upConvertObject(RpmTD::new, (MemorySegment) mh_rpmtdNew.invokeExact());
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdNew", _t);
        }
    }

    private final MethodHandle mh_rpmtdNew = makeMethodHandle(OBJ, "rpmtdNew");

    /**
     * Method stub that invokes native method {@code rpmtdNext}.
     *
     * @param td RpmTD
     * @return int
     */
    @Override
    public int rpmtdNext(RpmTD td) {
        try {
            return (int) mh_rpmtdNext.invokeExact(downConvertObject(td));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtdNext", _t);
        }
    }

    private final MethodHandle mh_rpmtdNext = makeMethodHandle(INT, "rpmtdNext", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtsCreate}.
     *
     * @return RpmTS
     */
    @Override
    public RpmTS rpmtsCreate() {
        try {
            return upConvertObject(RpmTS::new, (MemorySegment) mh_rpmtsCreate.invokeExact());
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtsCreate", _t);
        }
    }

    private final MethodHandle mh_rpmtsCreate = makeMethodHandle(OBJ, "rpmtsCreate");

    /**
     * Method stub that invokes native method {@code rpmtsFree}.
     *
     * @param ts RpmTS
     */
    @Override
    public void rpmtsFree(RpmTS ts) {
        try {
            mh_rpmtsFree.invokeExact(downConvertObject(ts));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtsFree", _t);
        }
    }

    private final MethodHandle mh_rpmtsFree = makeMethodHandle(VOID, "rpmtsFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmtsInitIterator}.
     *
     * @param ts RpmTS
     * @param rpmtag int
     * @param keyp String
     * @param keylen long
     * @return RpmMI
     */
    @Override
    public RpmMI rpmtsInitIterator(RpmTS ts, int rpmtag, String keyp, long keylen) {
        try (Arena arena = Arena.ofConfined()) {
            return upConvertObject(
                    RpmMI::new,
                    (MemorySegment)
                            mh_rpmtsInitIterator.invokeExact(
                                    downConvertObject(ts),
                                    rpmtag,
                                    downConvertString(keyp, arena),
                                    keylen));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtsInitIterator", _t);
        }
    }

    private final MethodHandle mh_rpmtsInitIterator =
            makeMethodHandle(OBJ, "rpmtsInitIterator", OBJ, INT, STR, LONG);

    /**
     * Method stub that invokes native method {@code rpmtsSetRootDir}.
     *
     * @param ts RpmTS
     * @param rootDir String
     * @return int
     */
    @Override
    public int rpmtsSetRootDir(RpmTS ts, String rootDir) {
        try (Arena arena = Arena.ofConfined()) {
            return (int)
                    mh_rpmtsSetRootDir.invokeExact(
                            downConvertObject(ts), downConvertString(rootDir, arena));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtsSetRootDir", _t);
        }
    }

    private final MethodHandle mh_rpmtsSetRootDir =
            makeMethodHandle(INT, "rpmtsSetRootDir", OBJ, STR);

    /**
     * Method stub that invokes native method {@code rpmtsSetVSFlags}.
     *
     * @param ts RpmTS
     * @param vsflags int
     */
    @Override
    public void rpmtsSetVSFlags(RpmTS ts, int vsflags) {
        try {
            mh_rpmtsSetVSFlags.invokeExact(downConvertObject(ts), vsflags);
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmtsSetVSFlags", _t);
        }
    }

    private final MethodHandle mh_rpmtsSetVSFlags =
            makeMethodHandle(VOID, "rpmtsSetVSFlags", OBJ, INT);

    /**
     * Method stub that invokes native method {@code rpmverE}.
     *
     * @param rv RpmEVR
     * @return String
     */
    @Override
    public String rpmverE(RpmEVR rv) {
        try {
            return upConvertString((MemorySegment) mh_rpmverE.invokeExact(downConvertObject(rv)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverE", _t);
        }
    }

    private final MethodHandle mh_rpmverE = makeMethodHandle(STR, "rpmverE", OBJ);

    /**
     * Method stub that invokes native method {@code rpmverEVal}.
     *
     * @param rv RpmEVR
     * @return long
     */
    @Override
    public long rpmverEVal(RpmEVR rv) {
        try {
            return (long) mh_rpmverEVal.invokeExact(downConvertObject(rv));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverEVal", _t);
        }
    }

    private final MethodHandle mh_rpmverEVal = makeMethodHandle(LONG, "rpmverEVal", OBJ);

    /**
     * Method stub that invokes native method {@code rpmverFree}.
     *
     * @param rv RpmEVR
     */
    @Override
    public void rpmverFree(RpmEVR rv) {
        try {
            mh_rpmverFree.invokeExact(downConvertObject(rv));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverFree", _t);
        }
    }

    private final MethodHandle mh_rpmverFree = makeMethodHandle(VOID, "rpmverFree", OBJ);

    /**
     * Method stub that invokes native method {@code rpmverParse}.
     *
     * @param evr String
     * @return RpmEVR
     */
    @Override
    public RpmEVR rpmverParse(String evr) {
        try (Arena arena = Arena.ofConfined()) {
            return upConvertObject(
                    RpmEVR::new,
                    (MemorySegment) mh_rpmverParse.invokeExact(downConvertString(evr, arena)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverParse", _t);
        }
    }

    private final MethodHandle mh_rpmverParse = makeMethodHandle(OBJ, "rpmverParse", STR);

    /**
     * Method stub that invokes native method {@code rpmverR}.
     *
     * @param rv RpmEVR
     * @return String
     */
    @Override
    public String rpmverR(RpmEVR rv) {
        try {
            return upConvertString((MemorySegment) mh_rpmverR.invokeExact(downConvertObject(rv)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverR", _t);
        }
    }

    private final MethodHandle mh_rpmverR = makeMethodHandle(STR, "rpmverR", OBJ);

    /**
     * Method stub that invokes native method {@code rpmverV}.
     *
     * @param rv RpmEVR
     * @return String
     */
    @Override
    public String rpmverV(RpmEVR rv) {
        try {
            return upConvertString((MemorySegment) mh_rpmverV.invokeExact(downConvertObject(rv)));
        } catch (Throwable _t) {
            throw new RuntimeException("Failed to invoke native function rpmverV", _t);
        }
    }

    private final MethodHandle mh_rpmverV = makeMethodHandle(STR, "rpmverV", OBJ);

    public static Iterable<FunctionDescriptor> getFunctionLayouts() {
        return Arrays.asList(
                FunctionDescriptor.of(INT, INT),
                FunctionDescriptor.of(INT, OBJ),
                FunctionDescriptor.of(INT, OBJ, INT, OBJ, INT),
                FunctionDescriptor.of(INT, OBJ, OBJ, STR, OBJ),
                FunctionDescriptor.of(INT, OBJ, STR),
                FunctionDescriptor.of(INT, STR, STR),
                FunctionDescriptor.of(LONG, OBJ),
                FunctionDescriptor.of(LONG, OBJ, BUFF, LONG),
                FunctionDescriptor.of(LONG, OBJ, INT),
                FunctionDescriptor.of(OBJ),
                FunctionDescriptor.of(OBJ, OBJ),
                FunctionDescriptor.of(OBJ, OBJ, INT),
                FunctionDescriptor.of(OBJ, OBJ, INT, INT),
                FunctionDescriptor.of(OBJ, OBJ, INT, STR, LONG),
                FunctionDescriptor.of(OBJ, OBJ, OBJ, INT),
                FunctionDescriptor.of(OBJ, OBJ, OBJ, INT, INT),
                FunctionDescriptor.of(OBJ, OBJ, STR),
                FunctionDescriptor.of(OBJ, STR),
                FunctionDescriptor.of(OBJ, STR, STR),
                FunctionDescriptor.of(STR, OBJ),
                FunctionDescriptor.of(STR, OBJ, INT),
                FunctionDescriptor.ofVoid(OBJ),
                FunctionDescriptor.ofVoid(OBJ, INT));
    }
}

/** Trampoline class that contains methods of RpmLib as static methods. */
class RpmLib_Static {
    private static class Lazy {
        static final SymbolLookup LOOKUP = Native.dlopenLookup("librpm.so.10", "librpm.so.9");
        static final RpmLib LIB = new RpmLib_Impl(LOOKUP);
    }

    /**
     * Method stub that invokes native method {@code Fclose}.
     *
     * @param fd RpmFD
     */
    public static final void Fclose(RpmFD fd) {
        Lazy.LIB.Fclose(fd);
    }

    /**
     * Method stub that invokes native method {@code Fdopen}.
     *
     * @param ofd RpmFD
     * @param mode String
     * @return RpmFD
     */
    public static final RpmFD Fdopen(RpmFD ofd, String mode) {
        return Lazy.LIB.Fdopen(ofd, mode);
    }

    /**
     * Method stub that invokes native method {@code Ferror}.
     *
     * @param fd RpmFD
     * @return int
     */
    public static final int Ferror(RpmFD fd) {
        return Lazy.LIB.Ferror(fd);
    }

    /**
     * Method stub that invokes native method {@code Fopen}.
     *
     * @param path String
     * @param mode String
     * @return RpmFD
     */
    public static final RpmFD Fopen(String path, String mode) {
        return Lazy.LIB.Fopen(path, mode);
    }

    /**
     * Method stub that invokes native method {@code Fstrerror}.
     *
     * @param fd RpmFD
     * @return String
     */
    public static final String Fstrerror(RpmFD fd) {
        return Lazy.LIB.Fstrerror(fd);
    }

    /**
     * Method stub that invokes native method {@code Ftell}.
     *
     * @param fd RpmFD
     * @return long
     */
    public static final long Ftell(RpmFD fd) {
        return Lazy.LIB.Ftell(fd);
    }

    /**
     * Method stub that invokes native method {@code gnu_dev_major}.
     *
     * @param dev int
     * @return int
     */
    public static final int gnu_dev_major(int dev) {
        return Lazy.LIB.gnu_dev_major(dev);
    }

    /**
     * Method stub that invokes native method {@code gnu_dev_minor}.
     *
     * @param dev int
     * @return int
     */
    public static final int gnu_dev_minor(int dev) {
        return Lazy.LIB.gnu_dev_minor(dev);
    }

    /**
     * Method stub that invokes native method {@code headerFree}.
     *
     * @param h RpmHeader
     */
    public static final void headerFree(RpmHeader h) {
        Lazy.LIB.headerFree(h);
    }

    /**
     * Method stub that invokes native method {@code headerGet}.
     *
     * @param h RpmHeader
     * @param tag int
     * @param td RpmTD
     * @param flags int
     * @return int
     */
    public static final int headerGet(RpmHeader h, int tag, RpmTD td, int flags) {
        return Lazy.LIB.headerGet(h, tag, td, flags);
    }

    /**
     * Method stub that invokes native method {@code headerGetNumber}.
     *
     * @param h RpmHeader
     * @param tag int
     * @return long
     */
    public static final long headerGetNumber(RpmHeader h, int tag) {
        return Lazy.LIB.headerGetNumber(h, tag);
    }

    /**
     * Method stub that invokes native method {@code headerGetString}.
     *
     * @param h RpmHeader
     * @param tag int
     * @return String
     */
    public static final String headerGetString(RpmHeader h, int tag) {
        return Lazy.LIB.headerGetString(h, tag);
    }

    /**
     * Method stub that invokes native method {@code rpmReadConfigFiles}.
     *
     * @param file String
     * @param target String
     * @return int
     */
    public static final int rpmReadConfigFiles(String file, String target) {
        return Lazy.LIB.rpmReadConfigFiles(file, target);
    }

    /**
     * Method stub that invokes native method {@code rpmReadPackageFile}.
     *
     * @param ts RpmTS
     * @param fd RpmFD
     * @param fn String
     * @param hdrp NativePointer
     * @return int
     */
    public static final int rpmReadPackageFile(RpmTS ts, RpmFD fd, String fn, NativePointer hdrp) {
        return Lazy.LIB.rpmReadPackageFile(ts, fd, fn, hdrp);
    }

    /**
     * Method stub that invokes native method {@code rpmdbFreeIterator}.
     *
     * @param mi RpmMI
     */
    public static final void rpmdbFreeIterator(RpmMI mi) {
        Lazy.LIB.rpmdbFreeIterator(mi);
    }

    /**
     * Method stub that invokes native method {@code rpmdbNextIterator}.
     *
     * @param mi RpmMI
     * @return RpmHeader
     */
    public static final RpmHeader rpmdbNextIterator(RpmMI mi) {
        return Lazy.LIB.rpmdbNextIterator(mi);
    }

    /**
     * Method stub that invokes native method {@code rpmdsDNEVR}.
     *
     * @param ds RpmDS
     * @return String
     */
    public static final String rpmdsDNEVR(RpmDS ds) {
        return Lazy.LIB.rpmdsDNEVR(ds);
    }

    /**
     * Method stub that invokes native method {@code rpmdsEVR}.
     *
     * @param ds RpmDS
     * @return String
     */
    public static final String rpmdsEVR(RpmDS ds) {
        return Lazy.LIB.rpmdsEVR(ds);
    }

    /**
     * Method stub that invokes native method {@code rpmdsFlags}.
     *
     * @param ds RpmDS
     * @return int
     */
    public static final int rpmdsFlags(RpmDS ds) {
        return Lazy.LIB.rpmdsFlags(ds);
    }

    /**
     * Method stub that invokes native method {@code rpmdsFree}.
     *
     * @param ds RpmDS
     */
    public static final void rpmdsFree(RpmDS ds) {
        Lazy.LIB.rpmdsFree(ds);
    }

    /**
     * Method stub that invokes native method {@code rpmdsIsRich}.
     *
     * @param ds RpmDS
     * @return int
     */
    public static final int rpmdsIsRich(RpmDS ds) {
        return Lazy.LIB.rpmdsIsRich(ds);
    }

    /**
     * Method stub that invokes native method {@code rpmdsN}.
     *
     * @param ds RpmDS
     * @return String
     */
    public static final String rpmdsN(RpmDS ds) {
        return Lazy.LIB.rpmdsN(ds);
    }

    /**
     * Method stub that invokes native method {@code rpmdsNew}.
     *
     * @param h RpmHeader
     * @param tagN int
     * @param flags int
     * @return RpmDS
     */
    public static final RpmDS rpmdsNew(RpmHeader h, int tagN, int flags) {
        return Lazy.LIB.rpmdsNew(h, tagN, flags);
    }

    /**
     * Method stub that invokes native method {@code rpmdsNext}.
     *
     * @param ds RpmDS
     * @return int
     */
    public static final int rpmdsNext(RpmDS ds) {
        return Lazy.LIB.rpmdsNext(ds);
    }

    /**
     * Method stub that invokes native method {@code rpmfiArchiveClose}.
     *
     * @param fi RpmFI
     */
    public static final void rpmfiArchiveClose(RpmFI fi) {
        Lazy.LIB.rpmfiArchiveClose(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiArchiveHasContent}.
     *
     * @param fi RpmFI
     * @return int
     */
    public static final int rpmfiArchiveHasContent(RpmFI fi) {
        return Lazy.LIB.rpmfiArchiveHasContent(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiArchiveRead}.
     *
     * @param fi RpmFI
     * @param buf Buffer
     * @param size long
     * @return long
     */
    public static final long rpmfiArchiveRead(RpmFI fi, Buffer buf, long size) {
        return Lazy.LIB.rpmfiArchiveRead(fi, buf, size);
    }

    /**
     * Method stub that invokes native method {@code rpmfiBN}.
     *
     * @param fi RpmFI
     * @return String
     */
    public static final String rpmfiBN(RpmFI fi) {
        return Lazy.LIB.rpmfiBN(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiDN}.
     *
     * @param fi RpmFI
     * @return String
     */
    public static final String rpmfiDN(RpmFI fi) {
        return Lazy.LIB.rpmfiDN(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiFInode}.
     *
     * @param fi RpmFI
     * @return int
     */
    public static final int rpmfiFInode(RpmFI fi) {
        return Lazy.LIB.rpmfiFInode(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiFLink}.
     *
     * @param fi RpmFI
     * @return String
     */
    public static final String rpmfiFLink(RpmFI fi) {
        return Lazy.LIB.rpmfiFLink(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiFMode}.
     *
     * @param fi RpmFI
     * @return int
     */
    public static final int rpmfiFMode(RpmFI fi) {
        return Lazy.LIB.rpmfiFMode(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiFMtime}.
     *
     * @param fi RpmFI
     * @return int
     */
    public static final int rpmfiFMtime(RpmFI fi) {
        return Lazy.LIB.rpmfiFMtime(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiFNlink}.
     *
     * @param fi RpmFI
     * @return int
     */
    public static final int rpmfiFNlink(RpmFI fi) {
        return Lazy.LIB.rpmfiFNlink(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiFRdev}.
     *
     * @param fi RpmFI
     * @return int
     */
    public static final int rpmfiFRdev(RpmFI fi) {
        return Lazy.LIB.rpmfiFRdev(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiFSize}.
     *
     * @param fi RpmFI
     * @return long
     */
    public static final long rpmfiFSize(RpmFI fi) {
        return Lazy.LIB.rpmfiFSize(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiFree}.
     *
     * @param fi RpmFI
     */
    public static final void rpmfiFree(RpmFI fi) {
        Lazy.LIB.rpmfiFree(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfiNewArchiveReader}.
     *
     * @param fd RpmFD
     * @param files RpmFiles
     * @param itype int
     * @return RpmFI
     */
    public static final RpmFI rpmfiNewArchiveReader(RpmFD fd, RpmFiles files, int itype) {
        return Lazy.LIB.rpmfiNewArchiveReader(fd, files, itype);
    }

    /**
     * Method stub that invokes native method {@code rpmfiNext}.
     *
     * @param fi RpmFI
     * @return int
     */
    public static final int rpmfiNext(RpmFI fi) {
        return Lazy.LIB.rpmfiNext(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfilesFree}.
     *
     * @param fi RpmFiles
     */
    public static final void rpmfilesFree(RpmFiles fi) {
        Lazy.LIB.rpmfilesFree(fi);
    }

    /**
     * Method stub that invokes native method {@code rpmfilesIter}.
     *
     * @param files RpmFiles
     * @param itype int
     * @return RpmFI
     */
    public static final RpmFI rpmfilesIter(RpmFiles files, int itype) {
        return Lazy.LIB.rpmfilesIter(files, itype);
    }

    /**
     * Method stub that invokes native method {@code rpmfilesNew}.
     *
     * @param pool RpmStrPool
     * @param h RpmHeader
     * @param tagN int
     * @param flags int
     * @return RpmFiles
     */
    public static final RpmFiles rpmfilesNew(RpmStrPool pool, RpmHeader h, int tagN, int flags) {
        return Lazy.LIB.rpmfilesNew(pool, h, tagN, flags);
    }

    /**
     * Method stub that invokes native method {@code rpmstrPoolCreate}.
     *
     * @return RpmStrPool
     */
    public static final RpmStrPool rpmstrPoolCreate() {
        return Lazy.LIB.rpmstrPoolCreate();
    }

    /**
     * Method stub that invokes native method {@code rpmstrPoolFree}.
     *
     * @param pool RpmStrPool
     */
    public static final void rpmstrPoolFree(RpmStrPool pool) {
        Lazy.LIB.rpmstrPoolFree(pool);
    }

    /**
     * Method stub that invokes native method {@code rpmtdCount}.
     *
     * @param td RpmTD
     * @return int
     */
    public static final int rpmtdCount(RpmTD td) {
        return Lazy.LIB.rpmtdCount(td);
    }

    /**
     * Method stub that invokes native method {@code rpmtdFree}.
     *
     * @param td RpmTD
     */
    public static final void rpmtdFree(RpmTD td) {
        Lazy.LIB.rpmtdFree(td);
    }

    /**
     * Method stub that invokes native method {@code rpmtdGetNumber}.
     *
     * @param td RpmTD
     * @return long
     */
    public static final long rpmtdGetNumber(RpmTD td) {
        return Lazy.LIB.rpmtdGetNumber(td);
    }

    /**
     * Method stub that invokes native method {@code rpmtdGetString}.
     *
     * @param td RpmTD
     * @return String
     */
    public static final String rpmtdGetString(RpmTD td) {
        return Lazy.LIB.rpmtdGetString(td);
    }

    /**
     * Method stub that invokes native method {@code rpmtdNew}.
     *
     * @return RpmTD
     */
    public static final RpmTD rpmtdNew() {
        return Lazy.LIB.rpmtdNew();
    }

    /**
     * Method stub that invokes native method {@code rpmtdNext}.
     *
     * @param td RpmTD
     * @return int
     */
    public static final int rpmtdNext(RpmTD td) {
        return Lazy.LIB.rpmtdNext(td);
    }

    /**
     * Method stub that invokes native method {@code rpmtsCreate}.
     *
     * @return RpmTS
     */
    public static final RpmTS rpmtsCreate() {
        return Lazy.LIB.rpmtsCreate();
    }

    /**
     * Method stub that invokes native method {@code rpmtsFree}.
     *
     * @param ts RpmTS
     */
    public static final void rpmtsFree(RpmTS ts) {
        Lazy.LIB.rpmtsFree(ts);
    }

    /**
     * Method stub that invokes native method {@code rpmtsInitIterator}.
     *
     * @param ts RpmTS
     * @param rpmtag int
     * @param keyp String
     * @param keylen long
     * @return RpmMI
     */
    public static final RpmMI rpmtsInitIterator(RpmTS ts, int rpmtag, String keyp, long keylen) {
        return Lazy.LIB.rpmtsInitIterator(ts, rpmtag, keyp, keylen);
    }

    /**
     * Method stub that invokes native method {@code rpmtsSetRootDir}.
     *
     * @param ts RpmTS
     * @param rootDir String
     * @return int
     */
    public static final int rpmtsSetRootDir(RpmTS ts, String rootDir) {
        return Lazy.LIB.rpmtsSetRootDir(ts, rootDir);
    }

    /**
     * Method stub that invokes native method {@code rpmtsSetVSFlags}.
     *
     * @param ts RpmTS
     * @param vsflags int
     */
    public static final void rpmtsSetVSFlags(RpmTS ts, int vsflags) {
        Lazy.LIB.rpmtsSetVSFlags(ts, vsflags);
    }

    /**
     * Method stub that invokes native method {@code rpmverE}.
     *
     * @param rv RpmEVR
     * @return String
     */
    public static final String rpmverE(RpmEVR rv) {
        return Lazy.LIB.rpmverE(rv);
    }

    /**
     * Method stub that invokes native method {@code rpmverEVal}.
     *
     * @param rv RpmEVR
     * @return long
     */
    public static final long rpmverEVal(RpmEVR rv) {
        return Lazy.LIB.rpmverEVal(rv);
    }

    /**
     * Method stub that invokes native method {@code rpmverFree}.
     *
     * @param rv RpmEVR
     */
    public static final void rpmverFree(RpmEVR rv) {
        Lazy.LIB.rpmverFree(rv);
    }

    /**
     * Method stub that invokes native method {@code rpmverParse}.
     *
     * @param evr String
     * @return RpmEVR
     */
    public static final RpmEVR rpmverParse(String evr) {
        return Lazy.LIB.rpmverParse(evr);
    }

    /**
     * Method stub that invokes native method {@code rpmverR}.
     *
     * @param rv RpmEVR
     * @return String
     */
    public static final String rpmverR(RpmEVR rv) {
        return Lazy.LIB.rpmverR(rv);
    }

    /**
     * Method stub that invokes native method {@code rpmverV}.
     *
     * @param rv RpmEVR
     * @return String
     */
    public static final String rpmverV(RpmEVR rv) {
        return Lazy.LIB.rpmverV(rv);
    }
}
