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

import static org.fedoraproject.javadeptools.ffi.Rpm.RPMDBI_INSTFILENAMES;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fedoraproject.javadeptools.ffi.Rpmlib;

import jdk.incubator.foreign.MemoryAddress;

/**
 * @author Mikolaj Izdebski
 */
public class RpmQuery {

    static {
        Rpmlib.rpmReadConfigFiles(null, null);
    }

    public static List<? extends NEVRA> byFile(Path path) {
        return byFile(path, null);
    }

    public static List<? extends NEVRA> byFile(Path path, Path root) {
        MemoryAddress ts = Rpmlib.rpmtsCreate();
        try {
            if (path != null) {
                if (Rpmlib.rpmtsSetRootDir(ts, root.toString()) != 0) {
                    return Collections.emptyList();
                }
            }
            MemoryAddress mi = Rpmlib.rpmtsInitIterator(ts, RPMDBI_INSTFILENAMES, path.toAbsolutePath().toString(), 0);
            try {
                List<NEVRAImpl> providers = new ArrayList<>();
                MemoryAddress h;
                while (!(h = Rpmlib.rpmdbNextIterator(mi)).equals(MemoryAddress.NULL)) {
                    providers.add(NEVRAImpl.from(h));
                }
                return Collections.unmodifiableList(providers);
            } finally {
                Rpmlib.rpmdbFreeIterator(mi);
            }
        } finally {
            Rpmlib.rpmtsFree(ts);
        }
    }

}
