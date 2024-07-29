/*-
 * Copyright (c) 2024 Red Hat, Inc.
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
 * RPM file.
 * 
 * @author Mikolaj Izdebski
 */
public class RpmFile {
    private final String name;
    private final String dn;
    private final String bn;
    private final int mode;
    private final long size;

    RpmFile(RpmFI fi) {
        bn = rpmfiBN(fi);
        dn = rpmfiDN(fi);
        name = dn + bn;
        size = rpmfiFSize(fi);
        mode = rpmfiFMode(fi);
    }

    /**
     * Returns the name of RPM file.
     * 
     * @return name of RPM file
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the base name of RPM file.
     * <p>
     * For example, for file {@code /foo/bar/baz.txt}, it returns {@code baz.txt}.
     * 
     * @return base name of RPM file
     */
    public String getBaseName() {
        return bn;
    }

    /**
     * Returns the directory name of RPM file.
     * <p>
     * For example, for file {@code /foo/bar/baz.txt}, it returns {@code /foo/bar}.
     * 
     * @return directory name of RPM file
     */
    public String getDirectoryName() {
        return dn;
    }

    /**
     * Returns size of RPM file.
     * 
     * @return size of RPM file
     */
    public long getSize() {
        return size;
    }

    /**
     * Returns mode of RPM file.
     * 
     * @return mode of RPM file
     */
    public int getMode() {
        return mode;
    }

    /**
     * Determines whether the file is a directory.
     *
     * @return true iff RPM file is a directory
     */
    public boolean isDirectory() {
        return (mode & S_IFMT) == S_IFDIR;
    }

    /**
     * Determines whether the file is a regular file.
     *
     * @return true iff RPM file is a regular file
     */
    public boolean isRegularFile() {
        return (mode & S_IFMT) == S_IFREG;
    }

    /**
     * Determines whether the file is a symbolic link.
     *
     * @return true iff RPM file is a symbolic link
     */
    public boolean isSymbolicLink() {
        return (mode & S_IFMT) == S_IFLNK;
    }

    // From /usr/include/linux/stat.h
    private static final int S_IFMT = 00170000;
    private static final int S_IFLNK = 0120000;
    private static final int S_IFREG = 0100000;
    private static final int S_IFDIR = 0040000;

    @Override
    public String toString() {
        return name;
    }
}
