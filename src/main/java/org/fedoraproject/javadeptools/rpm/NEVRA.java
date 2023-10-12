/*-
 * Copyright (c) 2016 Red Hat, Inc.
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

import java.lang.foreign.MemorySegment;

import org.fedoraproject.javadeptools.fma.Rpmlib;

/**
 * @author Mikolaj Izdebski
 */
public interface NEVRA {
    String getName();
    Integer getEpoch();
    String getVersion();
    String getRelease();
    String getArch();

    public static NEVRA create(String name, Integer epoch, String version, String release, String arch) {
        return new NEVRAImpl(name, epoch, version, release, arch);
    }
}

class NEVRAImpl implements NEVRA {
    private final String name;
    private final Integer epoch;
    private final String version;
    private final String release;
    private final String arch;
    private final String nevra;

    NEVRAImpl(String name, Integer epoch, String version, String release, String arch) {
        this.name = name;
        this.epoch = epoch;
        this.version = version;
        this.release = release;
        this.arch = arch;

        StringBuilder sb = new StringBuilder();
        sb.append(name).append('-');
        if (epoch != null && epoch > 0)
            sb.append(epoch + ":");
        sb.append(version).append('-').append(release);
        sb.append('.').append(arch);
        this.nevra = sb.toString();
    }

    static NEVRAImpl from(MemorySegment h) {
        String name = Rpmlib.headerGetString(h, Rpm.RPMTAG_NAME);
        Integer epoch = null;
        String version = Rpmlib.headerGetString(h, Rpm.RPMTAG_VERSION);
        String release = Rpmlib.headerGetString(h, Rpm.RPMTAG_RELEASE);
        String arch = Rpmlib.headerGetString(h, Rpm.RPMTAG_ARCH);

        var td = Rpmlib.rpmtdNew();
        try {
            if (Rpmlib.headerGet(h, Rpm.RPMTAG_EPOCH, td, Rpm.HEADERGET_EXT) != 0) {
                if (Rpmlib.rpmtdCount(td) == 1) {
                    epoch = Integer.valueOf((int) Rpmlib.rpmtdGetNumber(td));
                }
            }
        } finally {
            Rpmlib.rpmtdFreeData(td);
            Rpmlib.rpmtdFree(td);
        }

        return new NEVRAImpl(name, epoch, version, release, arch);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getEpoch() {
        return epoch;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getRelease() {
        return release;
    }

    @Override
    public String getArch() {
        return arch;
    }

    @Override
    public String toString() {
        return nevra;
    }

    @Override
    public int hashCode() {
        return nevra.hashCode();
    }

    private boolean equals(NEVRA other) {
        return other != null &&
                name.equals(other.getName())
                && epoch == other.getEpoch()
                && version.equals(other.getVersion())
                && release.equals(other.getRelease())
                && arch.equals(other.getArch());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NEVRA && equals((NEVRA) obj);
    }
}
