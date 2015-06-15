/*-
 * Copyright (c) 2015 Red Hat, Inc.
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
package org.fedoraproject.javadeptools.hawkey;

import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_package_get_arch;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_package_get_downloadsize;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_package_get_epoch;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_package_get_files;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_package_get_installsize;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_package_get_location;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_package_get_name;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_package_get_nevra;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_package_get_release;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_package_get_version;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_stringarray_free;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_stringarray_length;
import static org.fedoraproject.javadeptools.hawkey.HawkeyUtils.assumeNotNull;
import static org.fedoraproject.javadeptools.hawkey.HawkeyUtils.str;

import com.sun.jna.Pointer;

/**
 * Immutable bean holding selected information about one package.
 * 
 * @author Mikolaj Izdebski
 */
public class PackageInfo {
    private final String nevra;
    private final String location;
    private final String name;
    private final int epoch;
    private final String version;
    private final String release;
    private final String arch;
    private final long downloadSize;
    private final long installSize;
    private final int fileCount;

    PackageInfo(Pointer self) throws HawkeyException {
        nevra = str(hy_package_get_nevra(self));
        location = str(hy_package_get_location(self));
        name = hy_package_get_name(self);
        epoch = hy_package_get_epoch(self);
        version = str(hy_package_get_version(self));
        release = str(hy_package_get_release(self));
        arch = hy_package_get_arch(self);
        downloadSize = hy_package_get_downloadsize(self);
        installSize = hy_package_get_installsize(self);

        Pointer flist = hy_package_get_files(self);
        assumeNotNull(flist);
        try {
            fileCount = hy_stringarray_length(flist);
        } finally {
            hy_stringarray_free(flist);
        }

    }

    /**
     * Get package NEVRA.
     * 
     * @return string consisting of package name, epoch, version, release and
     *         architecture
     */
    public String getNevra() {
        return nevra;
    }

    /**
     * Get package location.
     * 
     * @return string representing location of the package
     */
    public String getLocation() {
        return location;
    }

    /**
     * Get package name.
     * 
     * @return package name
     */
    public String getName() {
        return name;
    }

    /**
     * Get package epoch.
     * 
     * @return package epoch
     */
    public int getEpoch() {
        return epoch;
    }

    /**
     * Get package version.
     * 
     * @return package version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Get package release.
     * 
     * @return package release
     */
    public String getRelease() {
        return release;
    }

    /**
     * Get package architecture.
     * 
     * @return package architecture
     */
    public String getArch() {
        return arch;
    }

    /**
     * Get package download size.
     * 
     * @return package download size, in bytes (non-negative)
     */
    public long getDownloadSize() {
        return downloadSize;
    }

    /**
     * Get package installed size.
     * 
     * @return package installed size, in bytes (non-negative)
     */
    public long getInstallSize() {
        return installSize;
    }

    /**
     * Get number of files owned by this package.
     * 
     * @return package file count (non-negative)
     */
    public int getFileCount() {
        return fileCount;
    }

    /**
     * Get string representation of this package.
     * 
     * @return package NEVRA
     * @see {@link #getNevra()}
     */
    @Override
    public String toString() {
        return nevra;
    }

    @Override
    public int hashCode() {
        return nevra.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PackageInfo))
            return false;
        PackageInfo other = (PackageInfo) obj;
        return nevra.equals(other.nevra);
    }
}
