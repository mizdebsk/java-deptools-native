/*-
 * Copyright (c) 2016-2023 Red Hat, Inc.
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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * List installed RPM packages by given criteria.
 * 
 * @author Mikolaj Izdebski
 */
public class RpmQuery {

    private RpmQuery() {}

    static {
        rpmReadConfigFiles(null, null);
    }

    /**
     * List RPM packages installed in the system that provide given file.
     * 
     * @param path provided file to query packages
     * @return list of packages that provide given file
     */
    public static List<RpmInfo> byFile(Path path) {
        return byFile(path, null);
    }

    /**
     * List RPM packages installed in a chroot that provide given file.
     * 
     * @param path provided file to query packages
     * @param root path to chroot in which packages are installed
     * @return list of packages that provide given file
     */
    public static List<RpmInfo> byFile(Path path, Path root) {
        RpmTS ts = rpmtsCreate();
        try {
            if (root != null) {
                if (rpmtsSetRootDir(ts, root.toString()) != 0) {
                    return Collections.emptyList();
                }
            }
            RpmMI mi = rpmtsInitIterator(ts, RPMDBI_INSTFILENAMES, path.toAbsolutePath().toString(), 0);
            try {
                List<RpmInfo> providers = new ArrayList<>();
                RpmHeader h;
                while ((h = rpmdbNextIterator(mi)) != null) {
                    providers.add(new RpmInfo(h));
                }
                return Collections.unmodifiableList(providers);
            } finally {
                rpmdbFreeIterator(mi);
            }
        } finally {
            rpmtsFree(ts);
        }
    }

}
