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
package io.kojan.javadeptools.rpm;

import static io.kojan.javadeptools.rpm.Rpm.*;

import java.io.IOException;
import java.nio.file.Path;

import io.kojan.javadeptools.nativ.NativePointer;

/**
 * Representation of a RPM package file that is present in the local file
 * system.
 * 
 * @author Mikolaj Izdebski
 */
public class RpmPackage {
    private static IOException error(Path path, String message) throws IOException {
        throw new IOException("Unable to open RPM file " + path + ": " + message);
    }

    private final Path path;
    private final RpmInfo info;
    private final long headerSize;

    /**
     * Read RPM package from disk.
     * 
     * @param path path to a file to read as RPM package
     * @throws IOException when given file is not a RPM valid package or when I/O
     *                     error occurs reading package from disk
     */
    public RpmPackage(Path path) throws IOException {
        this.path = path;
        RpmTS ts = rpmtsCreate();
        RpmFD fd = Fopen(path.toString(), "r");
        try {
            if (Ferror(fd) != 0)
                throw error(path, Fstrerror(fd));
            rpmtsSetVSFlags(ts, RPMVSF_NOHDRCHK | RPMVSF_NOSHA1HEADER | RPMVSF_NODSAHEADER | RPMVSF_NORSAHEADER
                    | RPMVSF_NOMD5 | RPMVSF_NODSA | RPMVSF_NORSA);
            NativePointer ph = new NativePointer();
            int rc = rpmReadPackageFile(ts, fd, null, ph);
            if (rc == RPMRC_NOTFOUND)
                throw error(path, "Not a RPM file");
            if (rc != RPMRC_OK && rc != RPMRC_NOTTRUSTED && rc != RPMRC_NOKEY)
                throw error(path, "Failed to parse RPM header");
            RpmHeader h = ph.dereference(RpmHeader::new);
            try {
                info = new RpmInfo(h);
            } finally {
                headerFree(h);
            }
            headerSize = Ftell(fd);
        } finally {
            Fclose(fd);
            rpmtsFree(ts);
        }
    }

    /**
     * Returns path to RPM package in the file system.
     * 
     * @return path to RPM package
     */
    public Path getPath() {
        return path;
    }

    /**
     * Returns detailed information about RPM package, extracted from RPM header.
     * 
     * @return {@link RpmInfo} for the RPM package
     */
    public RpmInfo getInfo() {
        return info;
    }

    long getHeaderSize() {
        return headerSize;
    }

    @Override
    public String toString() {
        return info.toString();
    }

    @Override
    public int hashCode() {
        return info.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RpmPackage && ((RpmPackage) obj).info.equals(info);
    }
}
