/*-
 * Copyright (c) 2023 Red Hat, Inc.
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

import org.junit.jupiter.api.Test;

/**
 * @author Mikolaj Izdebski
 */
public class RpmVersionTest {
    @Test
    public void testRpmVersion() throws Exception {
        RpmVersion v0 = new RpmVersion(null);
        assertNull(v0.getEpoch());
        assertNull(v0.getVersion());
        assertNull(v0.getRelease());

        RpmVersion v1 = new RpmVersion("");
        assertNull(v1.getEpoch());
        assertNull(v1.getVersion());
        assertNull(v1.getRelease());

        RpmVersion v2 = new RpmVersion("foo");
        assertNull(v2.getEpoch());
        assertEquals("foo", v2.getVersion());
        assertNull(v2.getRelease());

        RpmVersion v3 = new RpmVersion("foo-bar");
        assertNull(v3.getEpoch());
        assertEquals("foo", v3.getVersion());
        assertEquals("bar", v3.getRelease());

        RpmVersion v4 = new RpmVersion("42:foo-bar");
        assertEquals((Long) 42L, v4.getEpoch());
        assertEquals("foo", v4.getVersion());
        assertEquals("bar", v4.getRelease());

        RpmVersion v5 = new RpmVersion("1:2");
        assertEquals((Long) 1L, v5.getEpoch());
        assertEquals("2", v5.getVersion());
        assertNull(v5.getRelease());
    }
}
