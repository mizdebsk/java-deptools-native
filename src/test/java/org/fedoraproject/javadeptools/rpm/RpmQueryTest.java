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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

/**
 * @author Mikolaj Izdebski
 */
public class RpmQueryTest {

    private Path getResource(String name) {
        return Paths.get("src/test/resources/rpm").resolve(name).toAbsolutePath();
    }

    @Test
    public void testFileLookup() throws Exception {
        List<? extends NEVRA> providers = RpmQuery.byFile(Paths.get("/usr/bin/bash"), getResource("root1"));
        assertNotNull(providers);
        assertEquals(1, providers.size());
        NEVRA nevra = providers.iterator().next();
        assertEquals("bash", nevra.getName());
        assertEquals(0, nevra.getEpoch());
        assertEquals("4.3.42", nevra.getVersion());
        assertEquals("3.fc23", nevra.getRelease());
        assertEquals("x86_64", nevra.getArch());
        assertEquals("bash-4.3.42-3.fc23.x86_64", nevra.toString());
    }
}
