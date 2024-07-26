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
package io.kojan.javadeptools.nativ;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * @author Mikolaj Izdebski
 */
public class DynamicLinkerTest {

    @Test
    public void testDefault() throws Exception {
        DynamicLinker dl = new DynamicLinker();

        // Basic libc functions should be available in default linker
        assertTrue(dl.find("qsort").isPresent());

        // Likewise libjvm functions
        assertTrue(dl.find("JNI_CreateJavaVM").isPresent());

        // libcurl is not part of JVM process and won't be bound unless dlopen-ed first
        assertFalse(dl.find("curl_easy_init").isPresent());
    }

    @Test
    public void testCurl() throws Exception {
        DynamicLinker dl = new DynamicLinker("libcurl.so.4");

        // curl_easy_init() is part of libcurl
        assertTrue(dl.find("curl_easy_init").isPresent());

        // libcurl depends on libc, so qsort() should be available
        assertTrue(dl.find("qsort").isPresent());

        // libcurl does not depend on libjvm
        assertFalse(dl.find("JNI_CreateJavaVM").isPresent());
    }

    @Test
    public void testMissingLibrary() throws Exception {
        try {
            new DynamicLinker("xyzzy");
            fail();
        } catch (Exception e) {
            assertEquals("Unable to dlopen native library: xyzzy", e.getMessage());
        }
    }
}
