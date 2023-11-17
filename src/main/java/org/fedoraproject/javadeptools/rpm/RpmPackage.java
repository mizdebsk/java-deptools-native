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

import java.io.IOException;
import java.nio.file.Path;

import org.fedoraproject.javadeptools.nativ.NativePointer;

/**
 * @author Mikolaj Izdebski
 */
public class RpmPackage {
    private static IOException error(Path path, String message) throws IOException {
        throw new IOException("Unable to open RPM file " + path + ": " + message);
    }

    private final Path path;
    private final RpmInfo info;
    private final long headerSize;

    public RpmPackage(Path path) throws IOException {
        this.path = path;
        RpmTS ts = rpmtsCreate();
        RpmFD fd = Fopen(path.toString(), "r");
        try {
            if (Ferror(fd) != 0)
                throw error(path, Fstrerror(fd));
            rpmtsSetVSFlags(ts, RPMVSF_NOHDRCHK | RPMVSF_NOSHA1HEADER | RPMVSF_NODSAHEADER | RPMVSF_NORSAHEADER
                    | RPMVSF_NOMD5 | RPMVSF_NODSA | RPMVSF_NORSA);
            NativePointer<RpmHeader> ph = new NativePointer<>(RpmHeader.class);
            int rc = rpmReadPackageFile(ts, fd, null, ph);
            if (rc == RPMRC_NOTFOUND)
                throw error(path, "Not a RPM file");
            if (rc != RPMRC_OK && rc != RPMRC_NOTTRUSTED && rc != RPMRC_NOKEY)
                throw error(path, "Failed to parse RPM header");
            RpmHeader h = ph.dereference();
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

    public Path getPath() {
        return path;
    }

    public RpmInfo getInfo() {
        return info;
    }

    public long getHeaderSize() {
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
