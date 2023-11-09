/*-
 * Copyright (c) 2020 Red Hat, Inc.
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
package org.fedoraproject.javadeptools.rpm;

import static org.fedoraproject.javadeptools.rpm.Rpm.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * @author Mikolaj Izdebski
 */
public class RpmInfo {
    private static IOException error(Path path, String message) throws IOException {
        throw new IOException("Unable to open RPM file " + path + ": " + message);
    }

    private static List<String> headerGetList(Pointer h, int tag) {
        Pointer ptd = new Memory(1024);
        headerGet(h, tag, ptd, HEADERGET_MINMEM);
        try {
            int size = rpmtdCount(ptd);
            String[] list = new String[size];
            for (int i = 0; i < size; i++) {
                rpmtdNext(ptd);
                list[i] = rpmtdGetString(ptd);
            }
            return Collections.unmodifiableList(Arrays.asList(list));
        } finally {
            rpmtdFreeData(ptd);
        }
    }

    public RpmInfo(Path path) throws IOException {
        Pointer ts = rpmtsCreate();
        Pointer fd = Fopen(path.toString(), "r");
        try {
            if (Ferror(fd))
                throw error(path, Fstrerror(fd));
            rpmtsSetVSFlags(ts, RPMVSF_NOHDRCHK | RPMVSF_NOSHA1HEADER | RPMVSF_NODSAHEADER
                    | RPMVSF_NORSAHEADER | RPMVSF_NOMD5 | RPMVSF_NODSA | RPMVSF_NORSA);
            Pointer ph = new Memory(Native.POINTER_SIZE);
            int rc = rpmReadPackageFile(ts, fd, null, ph);
            if (rc == RPMRC_NOTFOUND)
                throw error(path, "Not a RPM file");
            if (rc != RPMRC_OK && rc != RPMRC_NOTTRUSTED && rc != RPMRC_NOKEY)
                throw error(path, "Failed to parse RPM header");
            Pointer h = ph.getPointer(0);
            try {
                nevra = new NEVRA(h);
                exclusiveArch = headerGetList(h, RPMTAG_EXCLUSIVEARCH);
                provides = headerGetList(h, RPMTAG_PROVIDENAME);
                requires = headerGetList(h, RPMTAG_REQUIRENAME);
                conflicts = headerGetList(h, RPMTAG_CONFLICTNAME);
                obsoletes = headerGetList(h, RPMTAG_OBSOLETENAME);
                recommends = headerGetList(h, RPMTAG_RECOMMENDNAME);
                suggests = headerGetList(h, RPMTAG_SUGGESTNAME);
                supplements = headerGetList(h, RPMTAG_SUPPLEMENTNAME);
                enhances = headerGetList(h, RPMTAG_ENHANCENAME);
                orderWithRequires = headerGetList(h, RPMTAG_ORDERNAME);
                archiveFormat = headerGetString(h, RPMTAG_PAYLOADFORMAT);
                compressionMethod = headerGetString(h, RPMTAG_PAYLOADCOMPRESSOR);
                sourcePackage = headerGetNumber(h, RPMTAG_SOURCEPACKAGE) != 0;
            } finally {
                headerFree(h);
            }
            headerSize = Ftell(fd);
        } finally {
            Fclose(fd);
            rpmtsFree(ts);
        }
    }

    private final NEVRA nevra;
    private final boolean sourcePackage;
    private final List<String> exclusiveArch;
    private final List<String> provides;
    private final List<String> requires;
    private final List<String> conflicts;
    private final List<String> obsoletes;
    private final List<String> recommends;
    private final List<String> suggests;
    private final List<String> supplements;
    private final List<String> enhances;
    private final List<String> orderWithRequires;
    private final String archiveFormat;
    private final String compressionMethod;
    private final long headerSize;

    public NEVRA getNEVRA() {
        return nevra;
    }

    public List<String> getExclusiveArch() {
        return exclusiveArch;
    }

    public String getName() {
        return nevra.getName();
    }

    public int getEpoch() {
        return nevra.getEpoch();
    }

    public String getVersion() {
        return nevra.getVersion();
    }

    public String getRelease() {
        return nevra.getRelease();
    }

    public String getArch() {
        return nevra.getArch();
    }

    public boolean isSourcePackage() {
        return sourcePackage;
    }

    public List<String> getProvides() {
        return provides;
    }

    public List<String> getRequires() {
        return requires;
    }

    public List<String> getConflicts() {
        return conflicts;
    }

    public List<String> getObsoletes() {
        return obsoletes;
    }

    public List<String> getRecommends() {
        return recommends;
    }

    public List<String> getSuggests() {
        return suggests;
    }

    public List<String> getSupplements() {
        return supplements;
    }

    public List<String> getEnhances() {
        return enhances;
    }

    public List<String> getOrderWithRequires() {
        return orderWithRequires;
    }

    public String getArchiveFormat() {
        return archiveFormat != null ? archiveFormat : "cpio";
    }

    public String getCompressionMethod() {
        return compressionMethod != null ? compressionMethod : "gzip";
    }

    public long getHeaderSize() {
        return headerSize;
    }

    @Override
    public String toString() {
        return nevra.toString();
    }

    @Override
    public int hashCode() {
        return nevra.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RpmInfo && ((RpmInfo) obj).nevra.equals(nevra);
    }
}
