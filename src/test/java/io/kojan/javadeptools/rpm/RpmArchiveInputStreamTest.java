/*-
 * Copyright (c) 2015-2023 Red Hat, Inc.
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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.junit.jupiter.api.Test;

/**
 * @author Mikolaj Izdebski
 */
public class RpmArchiveInputStreamTest {

    private Path getResource(String name) {
        return Paths.get("src/test/resources/rpm").resolve(name);
    }

    @Test
    public void testSRPM() throws Exception {
        Path path = getResource("foo-1-1.fc21.src.rpm");

        RpmArchiveInputStream ais = new RpmArchiveInputStream(path);

        ArchiveEntry entry = ais.getNextEntry();
        assertNotNull(entry);
        assertEquals("foo.spec", entry.getName());
        assertEquals(296, entry.getSize());

        ArchiveEntry entry2 = ais.getNextEntry();
        assertNotNull(entry2);
        assertEquals("some-file", entry2.getName());
        assertEquals(5, entry2.getSize());

        byte[] buf = new byte[6];
        int rc = ais.read(buf);
        assertEquals(5, rc);
        assertEquals("test\n\0", new String(buf));

        assertEquals(-1, ais.read(buf));

        assertNull(ais.getNextEntry());

        ais.close();
    }

    @Test
    public void testInvalidRPM() throws Exception {
        Path path = getResource("invalid.rpm");

        try (var _ = new RpmArchiveInputStream(path)) {
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Unable to open "));
            assertTrue(e.getMessage().endsWith(": Not a RPM file"));
        }
    }

    @Test
    public void testNonexistentRPM() throws Exception {
        Path path = Paths.get("/some/non-existent/path");

        try (var _ = new RpmArchiveInputStream(path)) {
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Unable to open "));
            assertTrue(e.getMessage().endsWith(": No such file or directory"));
        }
    }

    @Test
    public void testLZMA() throws Exception {
        Path path = getResource("foo-1-1.fc21.x86_64.rpm");

        RpmArchiveInputStream ais = new RpmArchiveInputStream(path);
        assertNull(ais.getNextEntry());
        ais.close();
    }

    @Test
    public void testZSTD() throws Exception {
        Path path = getResource("testrpm-1-1.fc31.x86_64.rpm");

        RpmArchiveInputStream ais = new RpmArchiveInputStream(path);
        assertNull(ais.getNextEntry());
        ais.close();
    }

    @Test
    public void testNoCompression() throws Exception {
        Path path = getResource("bar-1.0.0-1.fc23.noarch.rpm");

        RpmArchiveInputStream ais = new RpmArchiveInputStream(path);
        assertNotNull(ais.getNextEntry());
        assertNull(ais.getNextEntry());
        ais.close();
    }

    @Test
    public void testFilesWithNoContent() throws Exception {
        Path path = getResource("rpmfiles-1-1.noarch.rpm");

        RpmArchiveInputStream ais = new RpmArchiveInputStream(path);

        // Files in the package are:
        // e1 /a/directory
        // e2 /a/directory/conf1
        // e3 /a/directory/conf2
        // e4 /b
        // e5 /b/a
        // e6 /b/a/se
        // e7 /b/a/se/file.txt
        // e8 /gh/ost
        // e9 /symlink

        CpioArchiveEntry e1 = ais.getNextEntry();
        assertNotNull(e1);
        assertEquals("/a/directory", e1.getName());
        assertTrue(e1.isDirectory());
        assertFalse(e1.isSymbolicLink());
        assertEquals(0, e1.getSize());
        assertEquals(-1, ais.read());

        CpioArchiveEntry e2 = ais.getNextEntry();
        assertNotNull(e2);
        assertEquals("/a/directory/conf1", e2.getName());
        assertFalse(e2.isDirectory());
        assertFalse(e2.isSymbolicLink());
        assertEquals(0, e2.getSize());
        assertEquals(-1, ais.read());

        CpioArchiveEntry e3 = ais.getNextEntry();
        assertNotNull(e3);
        assertEquals("/a/directory/conf2", e3.getName());
        assertFalse(e3.isDirectory());
        assertFalse(e3.isSymbolicLink());
        assertEquals(0, e3.getSize());
        assertEquals(-1, ais.read());

        CpioArchiveEntry e4 = ais.getNextEntry();
        assertNotNull(e4);
        assertEquals("/b", e4.getName());
        assertTrue(e4.isDirectory());
        assertFalse(e4.isSymbolicLink());
        assertEquals(0, e4.getSize());
        assertEquals(-1, ais.read());

        CpioArchiveEntry e5 = ais.getNextEntry();
        assertNotNull(e5);
        assertEquals("/b/a", e5.getName());
        assertTrue(e5.isDirectory());
        assertFalse(e5.isSymbolicLink());
        assertEquals(0, e5.getSize());
        assertEquals(-1, ais.read());

        CpioArchiveEntry e6 = ais.getNextEntry();
        assertNotNull(e6);
        assertEquals("/b/a/se", e6.getName());
        assertTrue(e6.isDirectory());
        assertFalse(e6.isSymbolicLink());
        assertEquals(0, e6.getSize());
        assertEquals(-1, ais.read());

        CpioArchiveEntry e7 = ais.getNextEntry();
        assertNotNull(e7);
        assertEquals("/b/a/se/file.txt", e7.getName());
        assertFalse(e7.isDirectory());
        assertFalse(e7.isSymbolicLink());
        assertEquals(8, e7.getSize());
        assertArrayEquals("content\n".getBytes(), ais.readAllBytes());

        CpioArchiveEntry e8 = ais.getNextEntry();
        assertNotNull(e8);
        assertEquals("/gh/ost", e8.getName());
        assertFalse(e8.isDirectory());
        assertFalse(e8.isSymbolicLink());
        assertEquals(0, e8.getSize());
        assertEquals(-1, ais.read());

        CpioArchiveEntry e9 = ais.getNextEntry();
        assertNotNull(e9);
        assertEquals("/symlink", e9.getName());
        assertFalse(e9.isDirectory());
        assertTrue(e9.isSymbolicLink());
        assertEquals(9, e9.getSize());
        assertArrayEquals("something".getBytes(), ais.readAllBytes());

        assertNull(ais.getNextEntry());
        ais.close();
    }
}
