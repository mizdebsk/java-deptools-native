/*-
 * Copyright (c) 2012-2023 Red Hat, Inc.
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream;

/**
 * A class for reading RPM package as an archive.
 * 
 * @author Mikolaj Izdebski
 */
public class RpmArchiveInputStream extends ArchiveInputStream<CpioArchiveEntry> {
    private final CpioArchiveInputStream delegate;

    /**
     * Opens RPM package from disk as {@link ArchiveInputStream}
     * 
     * @param path path to a file to read as RPM package
     * @throws IOException when given file is not a valid RPM package or when I/O
     *                     error occurs reading package from disk
     */
    public RpmArchiveInputStream(Path path) throws IOException {
        this(new RpmPackage(path));
    }

    /**
     * Opens RPM package from disk as {@link ArchiveInputStream}
     * 
     * @param rpm instance of RPM package to read contents of
     * @throws IOException when given file is not a valid RPM package or when I/O
     *                     error occurs reading package from disk
     */
    public RpmArchiveInputStream(RpmPackage rpm) throws IOException {
        this.delegate = wrapFile(rpm);
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    @Override
    public CpioArchiveEntry getNextEntry() throws IOException {
        return delegate.getNextEntry();
    }

    @Override
    public int read() throws IOException {
        return delegate.read();
    }

    @Override
    public int read(byte[] buf) throws IOException {
        return delegate.read(buf);
    }

    @Override
    public int read(byte[] buf, int off, int len) throws IOException {
        return delegate.read(buf, off, len);
    }

    private static IOException error(Path path, String message) throws IOException {
        throw new IOException("Unable to open RPM file " + path + ": " + message);
    }

    private static boolean hasGzipMagic(InputStream fis) throws IOException {
        try {
            fis.mark(2);
            return fis.read() == 31 && fis.read() == 139;
        } finally {
            fis.reset();
        }
    }

    private static CpioArchiveInputStream wrapFile(RpmPackage rpm) throws IOException {
        RpmInfo info = rpm.getInfo();
        InputStream fis = new BufferedInputStream(Files.newInputStream(rpm.getPath()));
        fis.skip(rpm.getHeaderSize());

        InputStream cis = switch (info.getCompressionMethod()) {
        case "gzip" -> hasGzipMagic(fis) ? new GzipCompressorInputStream(fis, true) : fis;
        case "bzip2" -> new BZip2CompressorInputStream(fis);
        case "xz" -> cis = new XZCompressorInputStream(fis);
        case "lzma" -> cis = new LZMACompressorInputStream(fis);
        case "zstd" -> cis = new ZstdCompressorInputStream(fis);
        default -> {
            fis.close();
            throw error(rpm.getPath(), "Unsupported compression method: " + info.getCompressionMethod());
        }
        };

        if (!info.getArchiveFormat().equals("cpio")) {
            cis.close();
            throw error(rpm.getPath(), "Unsupported archive format: " + info.getArchiveFormat());
        }

        return new CpioArchiveInputStream(cis);
    }

}
