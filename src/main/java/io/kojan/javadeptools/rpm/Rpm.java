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

import java.nio.Buffer;

import io.kojan.javadeptools.nativ.NativeObject;
import io.kojan.javadeptools.nativ.NativePointer;

/**
 * @author Mikolaj Izdebski
 */
final class Rpm extends RpmLib_Static {

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
    static final int RPMFI_ITER_READ_ARCHIVE = 3;

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

    static interface RpmLib {
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
        int rpmfiFInode(RpmFI fi);
        int rpmfiFMode(RpmFI fi);
        int rpmfiFNlink(RpmFI fi);
        int rpmfiFMtime(RpmFI fi);
        long rpmfiFSize(RpmFI fi);
        int rpmfiFRdev(RpmFI fi);
        String rpmfiFLink(RpmFI fi);
        RpmFI rpmfiNewArchiveReader(RpmFD fd, RpmFiles files, int itype);
        void rpmfiArchiveClose(RpmFI fi);
        int rpmfiArchiveHasContent(RpmFI fi);
        long rpmfiArchiveRead(RpmFI fi, Buffer buf, long size);
        RpmFD Fopen(String path, String mode);
        RpmFD Fdopen(RpmFD ofd, String mode);
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
        int gnu_dev_major(int dev);
        int gnu_dev_minor(int dev);
    }
}
