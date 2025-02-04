/*-
 * Copyright (c) 2023-2025 Red Hat, Inc.
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

import static io.kojan.javadeptools.rpm.Rpm.*;

/**
 * RPM compound version (EVR) that consists of epoch, version and release.
 *
 * @author Mikolaj Izdebski
 */
public class RpmVersion {
    private final String evr;
    private final Long epoch;
    private final String version;
    private final String release;

    /**
     * Constructs {@link RpmVersion} by parsing RPM EVR string.
     *
     * @param evr RPM EVR (epoch-version-release) string
     */
    public RpmVersion(String evr) {
        this.evr = evr;
        RpmEVR v = rpmverParse(evr);
        this.epoch = rpmverE(v) != null ? rpmverEVal(v) : null;
        this.version = rpmverV(v);
        this.release = rpmverR(v);
        rpmverFree(v);
    }

    /**
     * Returns epoch part of RPM version.
     *
     * @return epoch part of RPM version
     */
    public Long getEpoch() {
        return epoch;
    }

    /**
     * Returns version part of RPM version.
     *
     * @return version part of RPM version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns release part of RPM version.
     *
     * @return release part of RPM version
     */
    public String getRelease() {
        return release;
    }

    @Override
    public String toString() {
        return evr;
    }

    @Override
    public int hashCode() {
        return evr.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RpmVersion && ((RpmVersion) obj).evr.equals(evr);
    }
}
