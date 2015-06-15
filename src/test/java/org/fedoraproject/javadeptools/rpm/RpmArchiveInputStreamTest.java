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
package org.fedoraproject.javadeptools.rpm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Test;

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

        ArchiveInputStream ais = new RpmArchiveInputStream(path);

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

        assertNull(ais.getNextEntry());

        ais.close();
    }

    @SuppressWarnings("resource")
    @Test
    public void testInvalidRPM() throws Exception {
        Path path = getResource("invalid.rpm");

        try {
            new RpmArchiveInputStream(path);
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Unable to open "));
            assertTrue(e.getMessage().endsWith(": Not a RPM file"));
        }
    }

    @SuppressWarnings("resource")
    @Test
    public void testNonexistentRPM() throws Exception {
        Path path = Paths.get("/some/non-existent/path");

        try {
            new RpmArchiveInputStream(path);
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Unable to open "));
            assertTrue(e.getMessage().endsWith(": No such file or directory"));
        }
    }

    @Test
    public void testLZMA() throws Exception {
        Path path = getResource("foo-1-1.fc21.x86_64.rpm");

        ArchiveInputStream ais = new RpmArchiveInputStream(path);
        assertNull(ais.getNextEntry());
        ais.close();
    }

}
