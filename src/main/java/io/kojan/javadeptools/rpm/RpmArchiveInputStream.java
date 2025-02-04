/*-
 * Copyright (c) 2012-2025 Red Hat, Inc.
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

import io.kojan.javadeptools.nativ.NativePointer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioConstants;

/**
 * A class for reading RPM package as an archive.
 *
 * @author Mikolaj Izdebski
 */
public class RpmArchiveInputStream extends ArchiveInputStream<CpioArchiveEntry> {
    private RpmFI fi;
    private RpmFI cpioFi;
    private RpmFiles files;
    private RpmFD fd;
    private RpmTS ts;
    private RpmHeader h;
    private long avail;
    private ByteArrayInputStream linkBytes;
    private boolean ghost;

    /**
     * Opens RPM package from disk as {@link ArchiveInputStream}
     *
     * @param path path to a file to read as RPM package
     * @throws IOException when given file is not a valid RPM package or when I/O error occurs
     *     reading package from disk
     */
    public RpmArchiveInputStream(Path path) throws IOException {
        boolean ok = false;
        try {
            ts = rpmtsCreate();
            fd = Fopen(path.toString(), "r");
            if (Ferror(fd) != 0) throw error(path, Fstrerror(fd));
            rpmtsSetVSFlags(
                    ts,
                    RPMVSF_NOHDRCHK
                            | RPMVSF_NOSHA1HEADER
                            | RPMVSF_NODSAHEADER
                            | RPMVSF_NORSAHEADER
                            | RPMVSF_NOMD5
                            | RPMVSF_NODSA
                            | RPMVSF_NORSA);
            NativePointer ph = new NativePointer();
            int rc = rpmReadPackageFile(ts, fd, null, ph);
            if (rc == RPMRC_NOTFOUND) throw error(path, "Not a RPM file");
            if (rc != RPMRC_OK && rc != RPMRC_NOTTRUSTED && rc != RPMRC_NOKEY)
                throw error(path, "Failed to parse RPM header");
            h = ph.dereference(RpmHeader::new);
            String compr = headerGetString(h, RPMTAG_PAYLOADCOMPRESSOR);
            if (compr == null) {
                compr = "gzip";
            }
            fd = Fdopen(fd, "r." + compr);
            files = rpmfilesNew(null, h, 0, RPMFI_KEEPHEADER);
            fi = rpmfilesIter(files, RPMFI_ITER_FWD);
            cpioFi = rpmfiNewArchiveReader(fd, files, RPMFI_ITER_READ_ARCHIVE);
            ok = true;
        } finally {
            if (!ok) {
                rpmfiArchiveClose(cpioFi);
                rpmfiFree(fi);
                rpmfilesFree(files);
                headerFree(h);
                Fclose(fd);
                rpmtsFree(ts);
            }
        }
    }

    /**
     * Opens RPM package from disk as {@link ArchiveInputStream}
     *
     * @param rpm instance of RPM package to read contents of
     * @throws IOException when given file is not a valid RPM package or when I/O error occurs
     *     reading package from disk
     */
    public RpmArchiveInputStream(RpmPackage rpm) throws IOException {
        this(rpm.getPath());
    }

    @Override
    public void close() throws IOException {
        rpmfiArchiveClose(cpioFi);
        rpmfiFree(fi);
        rpmfilesFree(files);
        headerFree(h);
        Fclose(fd);
        rpmtsFree(ts);
    }

    @Override
    public CpioArchiveEntry getNextEntry() throws IOException {
        if (rpmfiNext(fi) < 0) {
            return null;
        }
        if (!ghost) {
            rpmfiNext(cpioFi);
        }
        ghost = !(rpmfiDN(fi).equals(rpmfiDN(cpioFi)) && rpmfiBN(fi).equals(rpmfiBN(cpioFi)));
        if (!ghost && rpmfiArchiveHasContent(cpioFi) != 0) {
            avail = rpmfiFSize(fi);
        } else {
            avail = 0;
        }
        final int S_IFMT = 0170000;
        final int C_ISLNK = 0120000;
        if ((rpmfiFMode(fi) & S_IFMT) == C_ISLNK) {
            linkBytes = new ByteArrayInputStream(rpmfiFLink(fi).getBytes());
        } else {
            linkBytes = null;
        }
        final CpioArchiveEntry cpio = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        cpio.setInode(rpmfiFInode(fi));
        cpio.setMode(rpmfiFMode(fi));
        // TODO rpmfiFUser
        // TODO rpmfiFGroup
        cpio.setNumberOfLinks(rpmfiFNlink(fi));
        cpio.setTime(rpmfiFMtime(fi));
        cpio.setSize(rpmfiFSize(fi));
        cpio.setRemoteDeviceMaj(gnu_dev_major(rpmfiFRdev(fi)));
        cpio.setRemoteDeviceMin(gnu_dev_minor(rpmfiFRdev(fi)));
        cpio.setName(rpmfiDN(fi) + rpmfiBN(fi));
        return cpio;
    }

    @Override
    public int read(byte[] buf, int off, int len) throws IOException {
        if (linkBytes != null) {
            return linkBytes.read(buf, off, len);
        }
        if (avail == 0) {
            return -1;
        }
        ByteBuffer nativeBuffer = ByteBuffer.allocateDirect(len);
        int n = (int) rpmfiArchiveRead(cpioFi, nativeBuffer, len);
        nativeBuffer.position(n);
        nativeBuffer.flip();
        ByteBuffer arrayBuffer = ByteBuffer.wrap(buf, off, len);
        arrayBuffer.put(nativeBuffer);
        avail -= n;
        return n;
    }

    private static IOException error(Path path, String message) throws IOException {
        throw new IOException("Unable to open RPM file " + path + ": " + message);
    }
}
