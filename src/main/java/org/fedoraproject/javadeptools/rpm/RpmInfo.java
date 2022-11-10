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

import static org.fedoraproject.javadeptools.ffi.Rpm.HEADERGET_MINMEM;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMRC_NOKEY;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMRC_NOTFOUND;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMRC_NOTTRUSTED;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMRC_OK;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_BUILDARCHS;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_CONFLICTNAME;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_ENHANCENAME;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_EXCLUSIVEARCH;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_OBSOLETENAME;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_ORDERNAME;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_PAYLOADCOMPRESSOR;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_PAYLOADFORMAT;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_PROVIDENAME;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_RECOMMENDNAME;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_REQUIRENAME;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_SOURCEPACKAGE;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_SOURCERPM;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_SUGGESTNAME;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMTAG_SUPPLEMENTNAME;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMVSF_NODSA;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMVSF_NODSAHEADER;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMVSF_NOHDRCHK;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMVSF_NOMD5;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMVSF_NORSA;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMVSF_NORSAHEADER;
import static org.fedoraproject.javadeptools.ffi.Rpm.RPMVSF_NOSHA1HEADER;
import static org.fedoraproject.javadeptools.ffi.Rpmio.Fclose;
import static org.fedoraproject.javadeptools.ffi.Rpmio.Ferror;
import static org.fedoraproject.javadeptools.ffi.Rpmio.Fopen;
import static org.fedoraproject.javadeptools.ffi.Rpmio.Fstrerror;
import static org.fedoraproject.javadeptools.ffi.Rpmio.Ftell;
import static org.fedoraproject.javadeptools.ffi.Rpmio.fdDup;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.headerFree;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.headerGet;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.headerGetNumber;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.headerGetString;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.rpmReadPackageFile;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.rpmtdCount;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.rpmtdFree;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.rpmtdFreeData;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.rpmtdGetString;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.rpmtdNew;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.rpmtdNext;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.rpmtsCreate;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.rpmtsFree;
import static org.fedoraproject.javadeptools.ffi.Rpmlib.rpmtsSetVSFlags;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fedoraproject.javadeptools.ffi.Mman;
import org.fedoraproject.javadeptools.ffi.Unistd;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.ResourceScope;

/**
 * @author Mikolaj Izdebski
 */
public class RpmInfo implements NEVRA {
    private static IOException error(URL url, String message) throws IOException {
        throw new IOException("Unable to open RPM file " + url + ": " + message);
    }

    private static List<String> headerGetList(MemoryAddress h, int tag) {
        var td = rpmtdNew();
        try {
            headerGet(h, tag, td, HEADERGET_MINMEM);
            int size = rpmtdCount(td);
            String[] list = new String[size];
            for (int i = 0; i < size; i++) {
                rpmtdNext(td);
                list[i] = rpmtdGetString(td);
            }
            return Collections.unmodifiableList(Arrays.asList(list));
        } finally {
            rpmtdFreeData(td);
            rpmtdFree(td);
        }
    }

    public RpmInfo(Path path) throws IOException {
        this(path.toUri().toURL());
    }

    public RpmInfo(URL url) throws IOException {
        MemoryAddress fd;
        Runnable destructor = () -> {};
        if (url.getProtocol().equals("file")) {
            fd = Fopen(url.getPath(), "r");
        } else {
            byte[] bytes;
            try (var is = url.openStream()) {
                bytes = is.readAllBytes();
            }
            var intFD = Mman.memfd_create(url.getPath(), 0);
            destructor = () -> Unistd.close(intFD);
            fd = fdDup(intFD);
            Unistd.write(intFD, bytes);
            Unistd.lseek(intFD, 0, Unistd.SEEK_SET);
        }

        var ts = rpmtsCreate();
        try {
            if (Ferror(fd) != 0) {
                throw error(url, Fstrerror(fd));
            }
            rpmtsSetVSFlags(ts, RPMVSF_NOHDRCHK | RPMVSF_NOSHA1HEADER | RPMVSF_NODSAHEADER
                    | RPMVSF_NORSAHEADER | RPMVSF_NOMD5 | RPMVSF_NODSA | RPMVSF_NORSA);

            try (ResourceScope headerScope = ResourceScope.newConfinedScope()) {
                MemorySegment ph = MemorySegment.allocateNative(CLinker.C_POINTER, headerScope);
                int rc = rpmReadPackageFile(ts, fd, MemoryAddress.NULL, ph.address());
                if (rc == RPMRC_NOTFOUND) {
                    throw error(url, "Not a RPM file");
                }
                if (rc != RPMRC_OK && rc != RPMRC_NOTTRUSTED && rc != RPMRC_NOKEY) {
                    throw error(url, "Failed to parse RPM header");
                }

                MemoryAddress h = MemoryAddress.ofLong(ph.toLongArray()[0]);
                try {
                    nevra = NEVRAImpl.from(h);
                    buildArchs = headerGetList(h, RPMTAG_BUILDARCHS);
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
                    sourceRPM = headerGetString(h, RPMTAG_SOURCERPM);
                    sourcePackage = headerGetNumber(h, RPMTAG_SOURCEPACKAGE) != 0;
                } finally {
                    headerFree(h);
                }
                headerSize = Ftell(fd);
            }
        } finally {
            Fclose(fd);
            rpmtsFree(ts);
            destructor.run();
        }
    }

    /**
     * Constructor which does not throw checked exceptions.
     * @param path Path to .rpm file.
     * @return The constructed RpmInfo.
     */
    static RpmInfo create(URL url) {
        try {
            return new RpmInfo(url);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private final NEVRAImpl nevra;
    private final boolean sourcePackage;
    private final List<String> buildArchs;
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
    private final String sourceRPM;
    private final long headerSize;

    @Override
    public String getName() {
        return nevra.getName();
    }

    @Override
    public int getEpoch() {
        return nevra.getEpoch();
    }

    @Override
    public String getVersion() {
        return nevra.getVersion();
    }

    @Override
    public String getRelease() {
        return nevra.getRelease();
    }

    @Override
    public String getArch() {
        return nevra.getArch();
    }

    public String getSourceRPM() {
        return sourceRPM;
    }

    public String getPackageName() {
        if (isSourcePackage()) {
            return getName();
        } else {
            String result = getSourceRPM();
            result = result.substring(0, result.lastIndexOf('-'));
            result = result.substring(0, result.lastIndexOf('-'));

            if (result.isEmpty()) {
                throw new RuntimeException("Could not read package name for source RPM: " + getSourceRPM());
            }

            return result;
        }
    }

    public boolean isSourcePackage() {
        return sourcePackage;
    }

    public List<String> getBuildArchs() {
        return buildArchs;
    }

    public List<String> getExclusiveArch() {
        return exclusiveArch;
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

    public static void main(String[] args) throws Exception {
        var url = new URL("https://kojipkgs.fedoraproject.org//packages/lucene/9.2.0/3.fc36/noarch/lucene-analysis-common-9.2.0-3.fc36.noarch.rpm");
        var rpmi = new RpmInfo(url);
        System.out.println(rpmi.getRequires());
    }
}
