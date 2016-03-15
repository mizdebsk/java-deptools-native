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
package org.fedoraproject.javadeptools.librepo;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * @author Mikolaj Izdebski
 */
public class YumRepoTest {

    private Path tempDir;
    private Path resourcesDir;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() throws Exception {
        tempDir = Paths.get("target/librepo-test").resolve(testName.getMethodName()).toAbsolutePath();
        delete(tempDir);
        Files.createDirectories(tempDir);

        resourcesDir = Paths.get("src/test/resources/librepo").resolve(testName.getMethodName()).toAbsolutePath();
    }

    private void delete(Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
            for (Path child : Files.newDirectoryStream(path))
                delete(child);

        Files.deleteIfExists(path);
    }

    @Test
    public void uncompressedLoadTest() throws Exception {
        String url = resourcesDir.toUri().toURL().toString();
        YumRepo yumRepo = new YumRepo(url);
        yumRepo.download(tempDir);

        for (String fn : Arrays.asList("repomd.xml", "primary.xml", "filelists.xml")) {
            Path p1 = resourcesDir.resolve("repodata").resolve(fn);
            Path p2 = tempDir.resolve("repodata").resolve(fn);
            assertTrue(Files.exists(p2, LinkOption.NOFOLLOW_LINKS));
            assertTrue(Files.exists(p2, LinkOption.NOFOLLOW_LINKS));
            byte[] data1 = Files.readAllBytes(p1);
            byte[] data2 = Files.readAllBytes(p1);
            assertArrayEquals(data1, data2);
        }
    }

}
