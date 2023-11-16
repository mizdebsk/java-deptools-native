/*-
 * Copyright (c) 2020-2023 Red Hat, Inc.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Mikolaj Izdebski
 */
public class RpmInfo {
    private static List<String> headerGetList(RpmHeader h, int tag) {
        RpmTD td = rpmtdNew();
        headerGet(h, tag, td, HEADERGET_MINMEM);
        try {
            int size = rpmtdCount(td);
            String[] list = new String[size];
            for (int i = 0; i < size; i++) {
                rpmtdNext(td);
                list[i] = rpmtdGetString(td);
            }
            return Collections.unmodifiableList(Arrays.asList(list));
        } finally {
            rpmtdFree(td);
        }
    }

    private static Optional<Long> headerGetOptionalNumber(RpmHeader h, int tag) {
        RpmTD td = rpmtdNew();
        headerGet(h, tag, td, HEADERGET_MINMEM);
        try {
            if (rpmtdNext(td) == 0) {
                return Optional.of(rpmtdGetNumber(td));
            }
            return Optional.empty();
        } finally {
            rpmtdFree(td);
        }
    }

    private static List<RpmDependency> dependencyList(RpmHeader h, int tag) {
        List<RpmDependency> list = new ArrayList<>();
        RpmDS ds = rpmdsNew(h, tag, 0);
        while (rpmdsNext(ds) >= 0) {
            list.add(new RpmDependency(ds));
        }
        rpmdsFree(ds);
        return list;
    }

    RpmInfo(RpmHeader h) {
        name = headerGetString(h, RPMTAG_NAME);
        epoch = headerGetOptionalNumber(h, RPMTAG_EPOCH);
        version = headerGetString(h, RPMTAG_VERSION);
        release = headerGetString(h, RPMTAG_RELEASE);
        arch = headerGetString(h, RPMTAG_ARCH);
        license = headerGetString(h, RPMTAG_LICENSE);
        sourceRPM = headerGetString(h, RPMTAG_SOURCERPM);
        exclusiveArch = headerGetList(h, RPMTAG_EXCLUSIVEARCH);
        buildArchs = headerGetList(h, RPMTAG_BUILDARCHS);
        provides = dependencyList(h, RPMTAG_PROVIDENAME);
        requires = dependencyList(h, RPMTAG_REQUIRENAME);
        conflicts = dependencyList(h, RPMTAG_CONFLICTNAME);
        obsoletes = dependencyList(h, RPMTAG_OBSOLETENAME);
        recommends = dependencyList(h, RPMTAG_RECOMMENDNAME);
        suggests = dependencyList(h, RPMTAG_SUGGESTNAME);
        supplements = dependencyList(h, RPMTAG_SUPPLEMENTNAME);
        enhances = dependencyList(h, RPMTAG_ENHANCENAME);
        orderWithRequires = dependencyList(h, RPMTAG_ORDERNAME);
        archiveFormat = headerGetString(h, RPMTAG_PAYLOADFORMAT);
        compressionMethod = headerGetString(h, RPMTAG_PAYLOADCOMPRESSOR);
        sourcePackage = headerGetNumber(h, RPMTAG_SOURCEPACKAGE) != 0;

        StringBuilder sb = new StringBuilder();
        sb.append(name).append('-');
        if (epoch.isPresent())
            sb.append(epoch.get() + ":");
        sb.append(version).append('-').append(release);
        sb.append('.').append(arch);
        nevra = sb.toString();
    }

    private final String name;
    private final Optional<Long> epoch;
    private final String version;
    private final String release;
    private final String arch;
    private final String nevra;
    private final boolean sourcePackage;
    private final String license;
    private final String sourceRPM;
    private final List<String> exclusiveArch;
    private final List<String> buildArchs;
    private final List<RpmDependency> provides;
    private final List<RpmDependency> requires;
    private final List<RpmDependency> conflicts;
    private final List<RpmDependency> obsoletes;
    private final List<RpmDependency> recommends;
    private final List<RpmDependency> suggests;
    private final List<RpmDependency> supplements;
    private final List<RpmDependency> enhances;
    private final List<RpmDependency> orderWithRequires;
    private final String archiveFormat;
    private final String compressionMethod;

    public String getLicense() {
        return license;
    }

    public String getSourceRPM() {
        return sourceRPM;
    }

    public List<String> getExclusiveArch() {
        return exclusiveArch;
    }

    public List<String> getBuildArchs() {
        return buildArchs;
    }

    public String getName() {
        return name;
    }

    public Optional<Long> getEpoch() {
        return epoch;
    }

    public String getVersion() {
        return version;
    }

    public String getRelease() {
        return release;
    }

    public String getArch() {
        return arch;
    }

    public boolean isSourcePackage() {
        return sourcePackage;
    }

    public List<RpmDependency> getProvides() {
        return provides;
    }

    public List<RpmDependency> getRequires() {
        return requires;
    }

    public List<RpmDependency> getConflicts() {
        return conflicts;
    }

    public List<RpmDependency> getObsoletes() {
        return obsoletes;
    }

    public List<RpmDependency> getRecommends() {
        return recommends;
    }

    public List<RpmDependency> getSuggests() {
        return suggests;
    }

    public List<RpmDependency> getSupplements() {
        return supplements;
    }

    public List<RpmDependency> getEnhances() {
        return enhances;
    }

    public List<RpmDependency> getOrderWithRequires() {
        return orderWithRequires;
    }

    public String getArchiveFormat() {
        return archiveFormat != null ? archiveFormat : "cpio";
    }

    public String getCompressionMethod() {
        return compressionMethod != null ? compressionMethod : "gzip";
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
