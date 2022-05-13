/*-
 * Copyright (c) 2016 Red Hat, Inc.
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

import static org.fedoraproject.javadeptools.rpm.Rpm.RPMDBI_INSTFILENAMES;
import static org.fedoraproject.javadeptools.rpm.Rpm.rpmReadConfigFiles;
import static org.fedoraproject.javadeptools.rpm.Rpm.rpmdbFreeIterator;
import static org.fedoraproject.javadeptools.rpm.Rpm.rpmdbNextIterator;
import static org.fedoraproject.javadeptools.rpm.Rpm.rpmtsCreate;
import static org.fedoraproject.javadeptools.rpm.Rpm.rpmtsFree;
import static org.fedoraproject.javadeptools.rpm.Rpm.rpmtsInitIterator;
import static org.fedoraproject.javadeptools.rpm.Rpm.rpmtsSetRootDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdk.incubator.foreign.MemoryAddress;

/**
 * @author Mikolaj Izdebski
 */
public class RpmQuery {

    static {
        rpmReadConfigFiles(null, null);
    }

    public static List<NEVRA> byFile(Path path) {
        return byFile(path, null);
    }

    public static List<NEVRA> byFile(Path path, Path root) {
        MemoryAddress ts = rpmtsCreate();
        try {
            if (path != null) {
                if (rpmtsSetRootDir(ts, root.toString()) != 0) {
                    return Collections.emptyList();
                }
            }
            MemoryAddress mi = rpmtsInitIterator(ts, RPMDBI_INSTFILENAMES, path.toAbsolutePath().toString(), 0);
            try {
                List<NEVRA> providers = new ArrayList<>();
                MemoryAddress h;
                while (!(h = rpmdbNextIterator(mi)).equals(MemoryAddress.NULL)) {
                    providers.add(new NEVRA(h));
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
