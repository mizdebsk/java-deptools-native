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

import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.BeforeClass;

/**
 * @author Mikolaj Izdebski
 */
public abstract class AbstractHawkeyTest {

    /**
     * Skip tests if Hawkey is not installed in the system.
     */
    @BeforeClass
    public static void assumeHawkeyPresent() {
        boolean installed = false;

        for (String libdir : System.getProperty("java.library.path").split(File.pathSeparator)) {
            installed |= Files.exists(Paths.get(libdir).resolve(System.mapLibraryName("hawkey")));
        }

        assumeTrue("Hawkey is installed", installed);
    }

    Path getResource(String name) {
        return Paths.get("src/test/resources/hawkey").resolve(name);
    }
}
