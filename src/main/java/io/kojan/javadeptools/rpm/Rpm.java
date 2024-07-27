/*-
 * Copyright (c) 2012-2024 Red Hat, Inc.
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

import java.util.List;

import io.kojan.javadeptools.nativ.Native;
import io.kojan.javadeptools.nativ.NativeObject;
import io.kojan.javadeptools.nativ.NativePointer;

/**
 * @author Mikolaj Izdebski
 */
final class Rpm {

    static final int RPMRC_OK = 0;
    static final int RPMRC_NOTFOUND = 1;
    static final int RPMRC_FAIL = 2;
    static final int RPMRC_NOTTRUSTED = 3;
    static final int RPMRC_NOKEY = 4;

    static final int RPMVSF_NOHDRCHK = 1 << 0;
    static final int RPMVSF_NOSHA1HEADER = 1 << 8;
    static final int RPMVSF_NODSAHEADER = 1 << 10;
    static final int RPMVSF_NORSAHEADER = 1 << 11;
    static final int RPMVSF_NOMD5 = 1 << 17;
    static final int RPMVSF_NODSA = 1 << 18;
    static final int RPMVSF_NORSA = 1 << 19;

    static final int RPMTAG_NAME = 1000;
    static final int RPMTAG_VERSION = 1001;
    static final int RPMTAG_RELEASE = 1002;
    static final int RPMTAG_EPOCH = 1003;
    static final int RPMTAG_LICENSE = 1014;
    static final int RPMTAG_ARCH = 1022;
    static final int RPMTAG_SOURCERPM = 1044;
    static final int RPMTAG_PROVIDENAME = 1047;
    static final int RPMTAG_REQUIRENAME = 1049;
    static final int RPMTAG_CONFLICTNAME = 1054;
    static final int RPMTAG_EXCLUSIVEARCH = 1061;
    static final int RPMTAG_BUILDARCHS = 1089;
    static final int RPMTAG_OBSOLETENAME = 1090;
    static final int RPMTAG_SOURCEPACKAGE = 1106;
    static final int RPMTAG_PAYLOADCOMPRESSOR = 1125;
    static final int RPMTAG_PAYLOADFORMAT = 1124;
    static final int RPMTAG_ORDERNAME = 5035;
    static final int RPMTAG_RECOMMENDNAME = 5046;
    static final int RPMTAG_SUGGESTNAME = 5049;
    static final int RPMTAG_SUPPLEMENTNAME = 5052;
    static final int RPMTAG_ENHANCENAME = 5055;

    static final int HEADERGET_MINMEM = 1 << 0;

    static final int RPMDBI_INSTFILENAMES = 5040;

    static final int RPMFI_KEEPHEADER = 1 << 0;
    static final int RPMFI_ITER_FWD = 0;

    static class RpmDS extends NativeObject {}
    static class RpmEVR extends NativeObject {}
    static class RpmFD extends NativeObject {}
    static class RpmHeader extends NativeObject {}
    static class RpmMI extends NativeObject {}
    static class RpmTD extends NativeObject {}
    static class RpmTS extends NativeObject {}
    static class RpmStrPool extends NativeObject {}
    static class RpmFiles extends NativeObject {}
    static class RpmFI extends NativeObject {}

    private static interface RpmLib {
        int rpmReadConfigFiles(String file, String target);
        RpmTS rpmtsCreate();
        void rpmtsFree(RpmTS ts);
        int rpmtsSetRootDir(RpmTS ts, String rootDir);
        RpmMI rpmtsInitIterator(RpmTS ts, int rpmtag, String keyp, long keylen);
        void rpmtsSetVSFlags(RpmTS ts, int vsflags);
        RpmHeader rpmdbNextIterator(RpmMI mi);
        void rpmdbFreeIterator(RpmMI mi);
        int rpmReadPackageFile(RpmTS ts, RpmFD fd, String fn, NativePointer hdrp);
        void headerFree(RpmHeader h);
        int headerGet(RpmHeader h, int tag, RpmTD td, int flags);
        String headerGetString(RpmHeader h, int tag);
        long headerGetNumber(RpmHeader h, int tag);
        RpmTD rpmtdNew();
        int rpmtdCount(RpmTD td);
        int rpmtdNext(RpmTD td);
        String rpmtdGetString(RpmTD td);
        long rpmtdGetNumber(RpmTD td);
        void rpmtdFree(RpmTD td);
        RpmDS rpmdsNew(RpmHeader h, int tagN, int flags);
        void rpmdsFree(RpmDS ds);
        int rpmdsNext(RpmDS ds);
        String rpmdsDNEVR(RpmDS ds);
        String rpmdsN(RpmDS ds);
        String rpmdsEVR(RpmDS ds);
        int rpmdsFlags(RpmDS ds);
        int rpmdsIsRich(RpmDS ds);
        RpmStrPool rpmstrPoolCreate();
        void rpmstrPoolFree(RpmStrPool pool);
        RpmFiles rpmfilesNew(RpmStrPool pool, RpmHeader h, int tagN, int flags);
        void rpmfilesFree(RpmFiles fi);
        RpmFI rpmfilesIter(RpmFiles files, int itype);
        void rpmfiFree(RpmFI fi);
        int rpmfiNext(RpmFI fi);
        String rpmfiBN(RpmFI fi);
        String rpmfiDN(RpmFI fi);
    }

    private static interface RpmIO {
        RpmFD Fopen(String path, String mode);
        void Fclose(RpmFD fd);
        long Ftell(RpmFD fd);
        int Ferror(RpmFD fd);
        String Fstrerror(RpmFD fd);
        RpmEVR rpmverParse(String evr);
        void rpmverFree(RpmEVR rv);
        long rpmverEVal(RpmEVR rv);
        String rpmverE(RpmEVR rv);
        String rpmverV(RpmEVR rv);
        String rpmverR(RpmEVR rv);
    }

    private static <T> T loadAny(Class<T> type, String... libs) {
        T result = null;
        Exception cause = null;
        for (String lib : libs) {
            try {
                result = Native.load(type, lib);
            } catch (Exception ex) {
                cause = ex;
                continue;
            }
            break;
        }
        if (result == null) {
            throw new RuntimeException("None of the libraries " + List.of(libs) + " found", cause);
        }
        return result;
    }

    private static class LazyIO {
        static final RpmIO RPMIO = loadAny(RpmIO.class, "librpmio.so.10", "librpmio.so.9");
    }

    static final RpmFD Fopen(String path, String mode) {
        return LazyIO.RPMIO.Fopen(path, mode);
    }

    static final void Fclose(RpmFD fd) {
        LazyIO.RPMIO.Fclose(fd);
    }

    static final long Ftell(RpmFD fd) {
        return LazyIO.RPMIO.Ftell(fd);
    }

    static final int Ferror(RpmFD fd) {
        return LazyIO.RPMIO.Ferror(fd);
    }

    static final String Fstrerror(RpmFD fd) {
        return LazyIO.RPMIO.Fstrerror(fd);
    }

    static final RpmEVR rpmverParse(String evr) {
        return LazyIO.RPMIO.rpmverParse(evr);
    }

    static final void rpmverFree(RpmEVR rv) {
        LazyIO.RPMIO.rpmverFree(rv);
    }

    static final long rpmverEVal(RpmEVR rv) {
        return LazyIO.RPMIO.rpmverEVal(rv);
    }

    static final String rpmverE(RpmEVR rv) {
        return LazyIO.RPMIO.rpmverE(rv);
    }

    static final String rpmverV(RpmEVR rv) {
        return LazyIO.RPMIO.rpmverV(rv);
    }

    static final String rpmverR(RpmEVR rv) {
        return LazyIO.RPMIO.rpmverR(rv);
    }

    private static class Lazy {
        static final RpmLib RPM = loadAny(RpmLib.class, "librpm.so.10", "librpm.so.9");
    }

    static final int rpmReadConfigFiles(String file, String target) {
        return Lazy.RPM.rpmReadConfigFiles(file, target);
    }

    static final RpmTS rpmtsCreate() {
        return Lazy.RPM.rpmtsCreate();
    }

    static final void rpmtsFree(RpmTS ts) {
        Lazy.RPM.rpmtsFree(ts);
    }

    static final int rpmtsSetRootDir(RpmTS ts, String rootDir) {
        return Lazy.RPM.rpmtsSetRootDir(ts, rootDir);
    }

    static final RpmMI rpmtsInitIterator(RpmTS ts, int rpmtag, String keyp, long keylen) {
        return Lazy.RPM.rpmtsInitIterator(ts, rpmtag, keyp, keylen);
    }

    static final void rpmtsSetVSFlags(RpmTS ts, int vsflags) {
        Lazy.RPM.rpmtsSetVSFlags(ts, vsflags);
    }

    static final RpmHeader rpmdbNextIterator(RpmMI mi) {
        return Lazy.RPM.rpmdbNextIterator(mi);
    }

    static final void rpmdbFreeIterator(RpmMI mi) {
        Lazy.RPM.rpmdbFreeIterator(mi);
    }

    static final int rpmReadPackageFile(RpmTS ts, RpmFD fd, String fn, NativePointer hdrp) {
        return Lazy.RPM.rpmReadPackageFile(ts, fd, fn, hdrp);
    }

    static final void headerFree(RpmHeader h) {
        Lazy.RPM.headerFree(h);
    }

    static final int headerGet(RpmHeader h, int tag, RpmTD td, int flags) {
        return Lazy.RPM.headerGet(h, tag, td, flags);
    }

    static final String headerGetString(RpmHeader h, int tag) {
        return Lazy.RPM.headerGetString(h, tag);
    }

    static final long headerGetNumber(RpmHeader h, int tag) {
        return Lazy.RPM.headerGetNumber(h, tag);
    }

    static final RpmTD rpmtdNew() {
        return Lazy.RPM.rpmtdNew();
    }

    static final int rpmtdCount(RpmTD td) {
        return Lazy.RPM.rpmtdCount(td);
    }

    static final int rpmtdNext(RpmTD td) {
        return Lazy.RPM.rpmtdNext(td);
    }

    static final String rpmtdGetString(RpmTD td) {
        return Lazy.RPM.rpmtdGetString(td);
    }

    static final long rpmtdGetNumber(RpmTD td) {
        return Lazy.RPM.rpmtdGetNumber(td);
    }

    static final void rpmtdFree(RpmTD td) {
        Lazy.RPM.rpmtdFree(td);
    }

    static final RpmDS rpmdsNew(RpmHeader h, int tagN, int flags) {
        return Lazy.RPM.rpmdsNew(h, tagN, flags);
    }

    static final void rpmdsFree(RpmDS ds) {
        Lazy.RPM.rpmdsFree(ds);
    }

    static final int rpmdsNext(RpmDS ds) {
        return Lazy.RPM.rpmdsNext(ds);
    }

    static final String rpmdsDNEVR(RpmDS ds) {
        return Lazy.RPM.rpmdsDNEVR(ds);
    }

    static final String rpmdsN(RpmDS ds) {
        return Lazy.RPM.rpmdsN(ds);
    }

    static final String rpmdsEVR(RpmDS ds) {
        return Lazy.RPM.rpmdsEVR(ds);
    }

    static final int rpmdsFlags(RpmDS ds) {
        return Lazy.RPM.rpmdsFlags(ds);
    }

    static final int rpmdsIsRich(RpmDS ds) {
        return Lazy.RPM.rpmdsIsRich(ds);
    }

    static final RpmStrPool rpmstrPoolCreate() {
        return Lazy.RPM.rpmstrPoolCreate();
    }

    static final void rpmstrPoolFree(RpmStrPool pool) {
        Lazy.RPM.rpmstrPoolFree(pool);
    }

    static final RpmFiles rpmfilesNew(RpmStrPool pool, RpmHeader h, int tagN, int flags) {
        return Lazy.RPM.rpmfilesNew(pool, h, tagN, flags);
    }

    static final void rpmfilesFree(RpmFiles fi) {
        Lazy.RPM.rpmfilesFree(fi);
    }

    static final RpmFI rpmfilesIter(RpmFiles files, int itype) {
        return Lazy.RPM.rpmfilesIter(files, itype);
    }

    static final void rpmfiFree(RpmFI fi) {
        Lazy.RPM.rpmfiFree(fi);
    }

    static final int rpmfiNext(RpmFI fi) {
        return Lazy.RPM.rpmfiNext(fi);
    }

    static final String rpmfiBN(RpmFI fi) {
        return Lazy.RPM.rpmfiBN(fi);
    }

    static final String rpmfiDN(RpmFI fi) {
        return Lazy.RPM.rpmfiDN(fi);
    }
}
