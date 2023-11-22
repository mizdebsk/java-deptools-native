/*-
 * Copyright (c) 2016-2023 Red Hat, Inc.
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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * @author Mikolaj Izdebski
 */
public class RpmQueryTest {

    private Path getResource(String name) {
        return Paths.get("src/test/resources/rpm").resolve(name).toAbsolutePath();
    }

    @Test
    public void testFileLookup() throws Exception {
        List<RpmInfo> providers = RpmQuery.byFile(Paths.get("/usr/bin/bash"), getResource("root1"));
        assertNotNull(providers);
        assertEquals(1, providers.size());
        RpmInfo info = providers.iterator().next();
        assertEquals("bash", info.getName());
        assertEquals(Optional.empty(), info.getEpoch());
        assertEquals("4.3.42", info.getVersion());
        assertEquals("3.fc23", info.getRelease());
        assertEquals("x86_64", info.getArch());
        assertEquals("bash-4.3.42-3.fc23.x86_64", info.toString());
    }
}
