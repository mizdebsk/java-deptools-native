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
        assertEquals("/a/directory", dir1.getName());
        assertEquals("/a/", dir1.getDirectoryName());
        assertEquals("directory", dir1.getBaseName());
        assertEquals(0, dir1.getSize());
        assertEquals(0040755, dir1.getMode());
        assertTrue(dir1.isDirectory());
        assertFalse(dir1.isRegularFile());
        assertFalse(dir1.isSymbolicLink());

        RpmFile conf1 = it.next();
        assertEquals("/a/directory/conf1", conf1.getName());
        assertEquals("/a/directory/", conf1.getDirectoryName());
        assertEquals("conf1", conf1.getBaseName());
        assertEquals(0, conf1.getSize());
        assertEquals(0100644, conf1.getMode());
        assertFalse(conf1.isDirectory());
        assertTrue(conf1.isRegularFile());
        assertFalse(conf1.isSymbolicLink());

        RpmFile conf2 = it.next();
        assertEquals("/a/directory/conf2", conf2.getName());
        assertEquals("/a/directory/", conf2.getDirectoryName());
        assertEquals("conf2", conf2.getBaseName());
        assertEquals(0, conf2.getSize());
        assertEquals(0100644, conf2.getMode());
        assertFalse(conf2.isDirectory());
        assertTrue(conf2.isRegularFile());
        assertFalse(conf2.isSymbolicLink());

        RpmFile dir2 = it.next();
        assertEquals("/b", dir2.getName());
        assertEquals("/", dir2.getDirectoryName());
        assertEquals("b", dir2.getBaseName());
        assertEquals(0, dir2.getSize());
        assertEquals(0040755, dir2.getMode());
        assertTrue(dir2.isDirectory());
        assertFalse(dir2.isRegularFile());
        assertFalse(dir2.isSymbolicLink());

        RpmFile dir3 = it.next();
        assertEquals("/b/a", dir3.getName());
        assertEquals("/b/", dir3.getDirectoryName());
        assertEquals("a", dir3.getBaseName());
        assertEquals(0, dir3.getSize());
        assertEquals(0040755, dir3.getMode());
        assertTrue(dir3.isDirectory());
        assertFalse(dir3.isRegularFile());
        assertFalse(dir3.isSymbolicLink());

        RpmFile dir4 = it.next();
        assertEquals("/b/a/se", dir4.getName());
        assertEquals("/b/a/", dir4.getDirectoryName());
        assertEquals("se", dir4.getBaseName());
        assertEquals(0, dir4.getSize());
        assertEquals(0040755, dir4.getMode());
        assertTrue(dir4.isDirectory());
        assertFalse(dir4.isRegularFile());
        assertFalse(dir4.isSymbolicLink());

        RpmFile f1 = it.next();
        assertEquals("/b/a/se/file.txt", f1.getName());
        assertEquals("/b/a/se/", f1.getDirectoryName());
        assertEquals("file.txt", f1.getBaseName());
        assertEquals(8, f1.getSize());
        assertEquals(0100644, f1.getMode());
        assertFalse(f1.isDirectory());
        assertTrue(f1.isRegularFile());
        assertFalse(f1.isSymbolicLink());

        RpmFile ghost = it.next();
        assertEquals("/gh/ost", ghost.getName());
        assertEquals("/gh/", ghost.getDirectoryName());
        assertEquals("ost", ghost.getBaseName());
        assertEquals(0, ghost.getSize());
        assertEquals(0100000, ghost.getMode());
        assertFalse(ghost.isDirectory());
        assertTrue(ghost.isRegularFile());
        assertFalse(ghost.isSymbolicLink());

        RpmFile symlink = it.next();
        assertEquals("/symlink", symlink.getName());
        assertEquals("/", symlink.getDirectoryName());
        assertEquals("symlink", symlink.getBaseName());
        assertEquals(9, symlink.getSize());
        assertEquals(0120777, symlink.getMode());
        assertFalse(symlink.isDirectory());
        assertFalse(symlink.isRegularFile());
        assertTrue(symlink.isSymbolicLink());

        assertFalse(it.hasNext());
    }
}
