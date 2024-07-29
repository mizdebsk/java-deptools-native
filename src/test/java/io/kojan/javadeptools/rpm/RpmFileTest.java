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

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

/**
 * @author Mikolaj Izdebski
 */
public class RpmFileTest {
    @Test
    public void testFiles() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/rpmfiles-1-1.noarch.rpm");
        RpmInfo info = new RpmPackage(path).getInfo();
        Iterator<RpmFile> it = info.getFiles().iterator();
//        [/a/directory, /a/directory/conf1, /a/directory/conf2, /b/a, /b/a/se, /b/a/se/file.txt, /gh]

        RpmFile dir1 = it.next();
        assertEquals("/a/", dir1.getDirectoryName());
        assertEquals("directory", dir1.getBaseName());

        RpmFile conf1 = it.next();
        assertEquals("/a/directory/", conf1.getDirectoryName());
        assertEquals("conf1", conf1.getBaseName());

        RpmFile conf2 = it.next();
        assertEquals("/a/directory/", conf2.getDirectoryName());
        assertEquals("conf2", conf2.getBaseName());

        RpmFile dir2 = it.next();
        assertEquals("/", dir2.getDirectoryName());
        assertEquals("b", dir2.getBaseName());

        RpmFile dir3 = it.next();
        assertEquals("/b/", dir3.getDirectoryName());
        assertEquals("a", dir3.getBaseName());

        RpmFile dir4 = it.next();
        assertEquals("/b/a/", dir4.getDirectoryName());
        assertEquals("se", dir4.getBaseName());

        RpmFile f1 = it.next();
        assertEquals("/b/a/se/", f1.getDirectoryName());
        assertEquals("file.txt", f1.getBaseName());

        RpmFile ghost = it.next();
        assertEquals("/gh/", ghost.getDirectoryName());
        assertEquals("ost", ghost.getBaseName());

        RpmFile symlink = it.next();
        assertEquals("/", symlink.getDirectoryName());
        assertEquals("symlink", symlink.getBaseName());

        assertFalse(it.hasNext());
    }
}
