/*-
 * Copyright (c) 2020 Red Hat, Inc.
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
import static org.junit.Assert.assertFalse;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * @author Mikolaj Izdebski
 */
public class RpmInfoTest {
    @Test
    public void testFileLookup() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/foo-1-1.fc21.x86_64.rpm");
        RpmInfo info = new RpmInfo(path);

        assertEquals("foo", info.getName());
        assertEquals(0, info.getEpoch());
        assertEquals("1", info.getVersion());
        assertEquals("1.fc21", info.getRelease());
        assertEquals("x86_64", info.getArch());

        Iterator<String> prov = info.getProvides().iterator();
        assertEquals("foo", prov.next());
        assertEquals("foo(x86-64)", prov.next());
        assertFalse(prov.hasNext());

        List<String> req = info.getRequires();
        assertEquals(4, req.size());
    }
}
