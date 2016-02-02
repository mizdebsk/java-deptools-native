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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mikolaj Izdebski
 */
public class GoalTest extends AbstractHawkeyTest {

    private Sack sack;

    @Before
    public void setUp() throws Exception {
        sack = new Sack("x86_64");
    }

    @After
    public void tearDown() throws Exception {
        sack.close();
    }

    @Test
    public void testCreate() throws Exception {
        try (Goal goal = new Goal(sack)) {
            assertNotNull(goal);
            assertNotNull(goal.self());
        }
    }

    @Test
    public void testCreateWithClosedSack() throws Exception {
        sack.close();
        try (Goal goal = new Goal(sack)) {
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Sack is already closed", e.getMessage());
        }
    }

    @Test
    public void testMultipleClose() throws Exception {
        try (Goal goal = new Goal(sack)) {
            goal.close();
            goal.close();
        }
    }

    @Test
    public void testUseAfterClose() throws Exception {
        try (Goal goal = new Goal(sack)) {
            goal.close();
            goal.getProblems();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Goal is already closed", e.getMessage());
        }
    }

    @Test
    public void testCloseAfterSackClose() throws Exception {
        try (Goal goal = new Goal(sack)) {
            sack.close();
        }
    }

    @Test
    public void testUseAfterSackClose() throws Exception {
        try (Goal goal = new Goal(sack)) {
            sack.close();
            goal.getProblems();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Sack is already closed", e.getMessage());
        }
    }

    private void addRepo(String name) throws Exception {
        Path mdPath = getResource(name + "-md.xml");
        Path primaryPath = getResource(name + "-primary.xml");
        Path filelistsPath = getResource(name + "-filelists.xml");
        sack.loadRepo(name, mdPath, primaryPath, filelistsPath);
    }

    @Test
    public void testInstall() throws Exception {
        addRepo("setup");

        try (Goal goal = new Goal(sack)) {
            goal.install("setup");
            boolean ok = goal.run();
            assertTrue(ok);
            assertEquals(4, goal.listInstalls().size());
            assertEquals(0, goal.getProblems().size());
        }
    }

    @Test
    public void testInstallUnsatisfiedDep() throws Exception {
        addRepo("setup");

        try (Goal goal = new Goal(sack)) {
            goal.install("fedora-release-server");
            boolean ok = goal.run();
            assertFalse(ok);
            assertEquals(1, goal.getProblems().size());
        }
    }

    @Test
    public void testResolveRequires() throws Exception {
        addRepo("setup");

        try (Goal goal = new Goal(sack)) {
            goal.install("setup");
            boolean ok = goal.run();
            assertTrue(ok);
            List<PackageInfo> requires = sack.resolveRequires("fedora-repos", "noarch");
            assertEquals(2, requires.size());
        }
    }
}
