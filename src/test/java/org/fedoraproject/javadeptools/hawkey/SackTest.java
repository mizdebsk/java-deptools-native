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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

/**
 * @author Mikolaj Izdebski
 */
public class SackTest extends AbstractHawkeyTest {

    @Test
    public void testCrete() throws Exception {
        try (Sack sack = new Sack("x86_64")) {
            assertNotNull(sack);
            assertNotNull(sack.self());
        }
    }

    @Test
    public void testUnsupportedArch() throws Exception {
        try (Sack sack = new Sack("xyzzy")) {
            fail();
        } catch (HawkeyException e) {
            assertEquals("Hawkey operation failed: unknown arch", e.getMessage());
        }
    }

    @Test
    public void testMultipleClose() throws Exception {
        try (Sack sack = new Sack("x86_64")) {
            sack.close();
            sack.close();
        }
    }

    @Test
    public void testUseAfterClose() throws Exception {
        try (Sack sack = new Sack("x86_64")) {
            sack.close();
            sack.globFile("foo");
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Sack is already closed", e.getMessage());
        }
    }

    @Test
    public void testLoadEmptyRepo() throws Exception {
        try (Sack sack = new Sack("x86_64")) {
            assertNotNull(sack);
            assertNotNull(sack.self());

            Path mdPath = getResource("empty-md.xml");
            Path primaryPath = getResource("empty-primary.xml");
            Path filelistsPath = getResource("empty-filelists.xml");
            sack.loadRepo("empty", mdPath, primaryPath, filelistsPath);
        }
    }

    @Test
    public void testLoadNonExsistentRepo() throws Exception {
        try (Sack sack = new Sack("x86_64")) {
            assertNotNull(sack);
            assertNotNull(sack.self());

            Path mdPath = getResource("non-existent-md.xml");
            Path primaryPath = getResource("non-existent-primary.xml");
            Path filelistsPath = getResource("non-existent-filelists.xml");
            sack.loadRepo("non-existent", mdPath, primaryPath, filelistsPath);
            fail();
        } catch (HawkeyException e) {
            assertEquals("Hawkey operation failed: I/O error", e.getMessage());
        }
    }

    @Test
    public void testLoadRepo() throws Exception {
        try (Sack sack = new Sack("x86_64")) {
            assertNotNull(sack);
            assertNotNull(sack.self());

            Path mdPath = getResource("jetty-md.xml");
            Path primaryPath = getResource("jetty-primary.xml");
            Path filelistsPath = getResource("jetty-filelists.xml");
            sack.loadRepo("jetty", mdPath, primaryPath, filelistsPath);
        }
    }

    @Test
    public void testFileGlob() throws Exception {
        try (Sack sack = new Sack("x86_64")) {
            assertNotNull(sack);
            assertNotNull(sack.self());

            Path mdPath = getResource("jetty-md.xml");
            Path primaryPath = getResource("jetty-primary.xml");
            Path filelistsPath = getResource("jetty-filelists.xml");
            sack.loadRepo("jetty", mdPath, primaryPath, filelistsPath);

            List<PackageInfo> pkgs = sack.globFile("*/javadoc/*");
            assertNotNull(pkgs);
            assertEquals(1, pkgs.size());
            assertEquals("jetty-javadoc-9.3.0-2.fc23.noarch", pkgs.get(0).toString());
        }
    }
}
