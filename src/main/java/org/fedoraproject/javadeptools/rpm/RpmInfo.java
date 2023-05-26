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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.fedoraproject.javadeptools.fma.Mman;
import org.fedoraproject.javadeptools.fma.Rpmio;
import org.fedoraproject.javadeptools.fma.Rpmlib;
import org.fedoraproject.javadeptools.fma.Unistd;

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

    private static <T> List<T> headerGetList(MemoryAddress h, int tag, Function<MemoryAddress, T> getter) {
        var td = Rpmlib.rpmtdNew();
        try {
            Rpmlib.headerGet(h, tag, td, Rpm.HEADERGET_MINMEM);
            int size = Rpmlib.rpmtdCount(td);
            var result = new ArrayList<T>(size);
            for (int i = 0; i < size; i++) {
                Rpmlib.rpmtdNext(td);
                result.add(getter.apply(td));
            }
            return Collections.unmodifiableList(result);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            Rpmlib.rpmtdFreeData(td);
            Rpmlib.rpmtdFree(td);
        }
    }

    private static List<String> headerGetListString(MemoryAddress h, int tag) {
        return headerGetList(h, tag, (td) -> Rpmlib.rpmtdGetString(td));
    }

    private static List<Long> headerGetListLong(MemoryAddress h, int tag) {
        return headerGetList(h, tag, (td) -> Rpmlib.rpmtdGetUint64(td));
    }

    private static List<Integer> headerGetListInt(MemoryAddress h, int tag) {
        return headerGetList(h, tag, (td) -> Rpmlib.rpmtdGetUint32(td));
    }

    private static List<Short> headerGetListShort(MemoryAddress h, int tag) {
        return headerGetList(h, tag, (td) -> Rpmlib.rpmtdGetUint16(td));
    }

    private static List<Character> headerGetListChar(MemoryAddress h, int tag) {
        return headerGetList(h, tag, (td) -> Rpmlib.rpmtdGetChar(td));
    }

    private static List<Reldep> headerGetListReldep(MemoryAddress h, int nameTag, int flagsTag, int versionTag) {
        var names = headerGetListString(h, nameTag);
        var flagIt = headerGetListInt(h, flagsTag).iterator();
        var versionIt = headerGetListString(h, versionTag).iterator();

        var result = new ArrayList<Reldep>(names.size());

        for (var nameIt = names.iterator(); nameIt.hasNext();) {
            result.add(new Reldep(nameIt.next(), flagIt.next(), versionIt.next()));
        }

        return result;
    }

    public RpmInfo(Path path) throws IOException {
        this(path.toUri().toURL());
    }

    public RpmInfo(URL url) throws IOException {
        MemoryAddress fd;
        Runnable destructor = () -> {};
        if (url.getProtocol().equals("file")) {
            try {
                fd = Rpmio.Fopen(Path.of(url.toURI()).toString(), "r");
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            byte[] bytes;
            try (var is = url.openStream()) {
                bytes = is.readAllBytes();
            }
            var intFD = Mman.memfd_create(url.getPath(), 0);
            destructor = () -> Unistd.close(intFD);
            fd = Rpmio.fdDup(intFD);
            Unistd.write(intFD, bytes);
            Unistd.lseek(intFD, 0, Unistd.SEEK_SET);
        }

        var ts = Rpmlib.rpmtsCreate();
        try {
            if (Rpmio.Ferror(fd) != 0) {
                throw error(url, Rpmio.Fstrerror(fd));
            }
            Rpmlib.rpmtsSetVSFlags(ts, Rpm.RPMVSF_NOHDRCHK | Rpm.RPMVSF_NOSHA1HEADER | Rpm.RPMVSF_NODSAHEADER
                    | Rpm.RPMVSF_NORSAHEADER | Rpm.RPMVSF_NOMD5 | Rpm.RPMVSF_NODSA | Rpm.RPMVSF_NORSA);

            try (ResourceScope headerScope = ResourceScope.newConfinedScope()) {
                MemorySegment ph = MemorySegment.allocateNative(CLinker.C_POINTER, headerScope);
                int rc = Rpmlib.rpmReadPackageFile(ts, fd, MemoryAddress.NULL, ph.address());
                if (rc == Rpm.RPMRC_NOTFOUND) {
                    throw error(url, "Not a RPM file");
                }
                if (rc != Rpm.RPMRC_OK && rc != Rpm.RPMRC_NOTTRUSTED && rc != Rpm.RPMRC_NOKEY) {
                    throw error(url, "Failed to parse RPM header");
                }

                MemoryAddress h = MemoryAddress.ofLong(ph.toLongArray()[0]);
                try {
                    nevra = NEVRAImpl.from(h);
                    buildArchs = headerGetListString(h, Rpm.RPMTAG_BUILDARCHS);
                    exclusiveArch = headerGetListString(h, Rpm.RPMTAG_EXCLUSIVEARCH);
                    provides = headerGetListReldep(h, Rpm.RPMTAG_PROVIDENAME, Rpm.RPMTAG_PROVIDEFLAGS, Rpm.RPMTAG_PROVIDEVERSION);
                    requires = headerGetListReldep(h, Rpm.RPMTAG_REQUIRENAME, Rpm.RPMTAG_REQUIREFLAGS, Rpm.RPMTAG_REQUIREVERSION);
                    conflicts = headerGetListReldep(h, Rpm.RPMTAG_CONFLICTNAME, Rpm.RPMTAG_CONFLICTFLAGS, Rpm.RPMTAG_CONFLICTVERSION);
                    obsoletes = headerGetListReldep(h, Rpm.RPMTAG_OBSOLETENAME, Rpm.RPMTAG_OBSOLETEFLAGS, Rpm.RPMTAG_OBSOLETEVERSION);
                    recommends = headerGetListReldep(h, Rpm.RPMTAG_RECOMMENDNAME, Rpm.RPMTAG_RECOMMENDFLAGS, Rpm.RPMTAG_RECOMMENDVERSION);
                    suggests = headerGetListReldep(h, Rpm.RPMTAG_SUGGESTNAME, Rpm.RPMTAG_SUGGESTFLAGS, Rpm.RPMTAG_SUGGESTVERSION);
                    supplements = headerGetListReldep(h, Rpm.RPMTAG_SUPPLEMENTNAME, Rpm.RPMTAG_SUPPLEMENTFLAGS, Rpm.RPMTAG_SUPPLEMENTVERSION);
                    enhances = headerGetListReldep(h, Rpm.RPMTAG_ENHANCENAME, Rpm.RPMTAG_ENHANCEFLAGS, Rpm.RPMTAG_ENHANCEVERSION);
                    orderWithRequires = headerGetListReldep(h, Rpm.RPMTAG_ORDERNAME, Rpm.RPMTAG_ORDERFLAGS, Rpm.RPMTAG_ORDERVERSION);
                    archiveFormat = Rpmlib.headerGetString(h, Rpm.RPMTAG_PAYLOADFORMAT);
                    compressionMethod = Rpmlib.headerGetString(h, Rpm.RPMTAG_PAYLOADCOMPRESSOR);
                    sourceRPM = Rpmlib.headerGetString(h, Rpm.RPMTAG_SOURCERPM);
                    sourcePackage = Rpmlib.headerGetNumber(h, Rpm.RPMTAG_SOURCEPACKAGE) != 0;
                } finally {
                    Rpmlib.headerFree(h);
                }
                headerSize = Rpmio.Ftell(fd);
            }
        } finally {
            Rpmio.Fclose(fd);
            Rpmlib.rpmtsFree(ts);
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
    private final List<Reldep> provides;
    private final List<Reldep> requires;
    private final List<Reldep> conflicts;
    private final List<Reldep> obsoletes;
    private final List<Reldep> recommends;
    private final List<Reldep> suggests;
    private final List<Reldep> supplements;
    private final List<Reldep> enhances;
    private final List<Reldep> orderWithRequires;
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

    public boolean isBinaryPackage() {
        return !sourcePackage;
    }

    public List<String> getBuildArchs() {
        return buildArchs;
    }

    public List<String> getExclusiveArch() {
        return exclusiveArch;
    }

    public List<Reldep> getProvides() {
        return provides;
    }

    public List<Reldep> getRequires() {
        return requires;
    }

    public List<Reldep> getConflicts() {
        return conflicts;
    }

    public List<Reldep> getObsoletes() {
        return obsoletes;
    }

    public List<Reldep> getRecommends() {
        return recommends;
    }

    public List<Reldep> getSuggests() {
        return suggests;
    }

    public List<Reldep> getSupplements() {
        return supplements;
    }

    public List<Reldep> getEnhances() {
        return enhances;
    }

    public List<Reldep> getOrderWithRequires() {
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
        var result = nevra.toString();
        if (isSourcePackage()) {
            result += ".src";
        }
        return result;
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
