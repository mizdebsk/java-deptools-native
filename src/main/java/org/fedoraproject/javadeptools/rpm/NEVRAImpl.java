/*-
 * Copyright (c) 2022 Red Hat, Inc.
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

import org.fedoraproject.javadeptools.fma.Rpmlib;

import jdk.incubator.foreign.MemoryAddress;

/**
 * @author Mikolaj Izdebski
 */
public class NEVRAImpl implements NEVRA {
    private final String name;
    private final int epoch;
    private final String version;
    private final String release;
    private final String arch;
    private final String nevra;

    NEVRAImpl(String name, int epoch, String version, String release, String arch) {
        this.name = name;
        this.epoch = epoch;
        this.version = version;
        this.release = release;
        this.arch = arch;

        StringBuilder sb = new StringBuilder();
        sb.append(name).append('-');
        if (epoch > 0)
            sb.append(epoch + ":");
        sb.append(version).append('-').append(release);
        sb.append('.').append(arch);
        this.nevra = sb.toString();
    }

    static NEVRAImpl from(MemoryAddress h) {
        String name = Rpmlib.headerGetString(h, Rpm.RPMTAG_NAME);
        int epoch = (int) Rpmlib.headerGetNumber(h, Rpm.RPMTAG_EPOCH);
        String version = Rpmlib.headerGetString(h, Rpm.RPMTAG_VERSION);
        String release = Rpmlib.headerGetString(h, Rpm.RPMTAG_RELEASE);
        String arch = Rpmlib.headerGetString(h, Rpm.RPMTAG_ARCH);
        return new NEVRAImpl(name, epoch, version, release, arch);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getEpoch() {
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
