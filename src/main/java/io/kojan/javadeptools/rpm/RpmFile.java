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
    private final String dn;
    private final String bn;

    RpmFile(RpmFI fi) {
        bn = rpmfiBN(fi);
        dn = rpmfiDN(fi);
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

    @Override
    public String toString() {
        return dn + bn;
    }
}
