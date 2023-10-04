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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author Mikolaj Izdebski
 */
public class RpmInfoTest {
    @Test
    public void testBinaryPackage() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/foo-1-1.fc21.x86_64.rpm");
        RpmInfo info = new RpmInfo(path);

        assertFalse(info.isSourcePackage());
        assertEquals("foo", info.getName());
        assertEquals(null, info.getEpoch());
        assertEquals("1", info.getVersion());
        assertEquals("1.fc21", info.getRelease());
        assertEquals("x86_64", info.getArch());

        Iterator<Reldep> prov = info.getProvides().iterator();
        assertEquals("foo = 1-1.fc21", prov.next().toString());
        assertEquals("foo(x86-64) = 1-1.fc21", prov.next().toString());
        assertFalse(prov.hasNext());

        Iterator<Reldep> req = info.getRequires().iterator();
        assertEquals("rpmlib(CompressedFileNames) <= 3.0.4-1", req.next().toString());
        assertEquals("rpmlib(FileDigests) <= 4.6.0-1", req.next().toString());
        assertEquals("rpmlib(PayloadFilesHavePrefix) <= 4.0-1", req.next().toString());
        assertEquals("rpmlib(PayloadIsLzma) <= 4.4.6-1", req.next().toString());
        assertFalse(req.hasNext());
    }

    @Test
    public void testSourcePackage() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/foo-1-1.fc21.src.rpm");
        RpmInfo info = new RpmInfo(path);

        assertTrue(info.isSourcePackage());
        assertEquals("foo", info.getName());
        assertEquals(null, info.getEpoch());
        assertEquals("1", info.getVersion());
        assertEquals("1.fc21", info.getRelease());
        assertEquals("x86_64", info.getArch());

        Iterator<Reldep> prov = info.getProvides().iterator();
        assertFalse(prov.hasNext());

        List<Reldep> req = info.getRequires();
        assertEquals(2, req.size());
    }

    @Test
    public void testDependencies() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/soft-1-1.noarch.rpm");
        RpmInfo info = new RpmInfo(path);

        Iterator<Reldep> provIt = info.getProvides().iterator();
        assertEquals("soft", provIt.next().getName());
        assertEquals("test-Provides-A", provIt.next().getName());
        assertEquals("test-Provides-B", provIt.next().getName());
        assertEquals("test-Provides-C", provIt.next().getName());
        assertFalse(provIt.hasNext());

        Iterator<Reldep> reqIt = info.getRequires().iterator();
        assertEquals("rpmlib(CompressedFileNames)", reqIt.next().getName());
        assertEquals("rpmlib(FileDigests)", reqIt.next().getName());
        assertEquals("rpmlib(PayloadFilesHavePrefix)", reqIt.next().getName());
        assertEquals("rpmlib(PayloadIsZstd)", reqIt.next().getName());
        assertEquals("test-Requires-A", reqIt.next().getName());
        assertEquals("test-Requires-B", reqIt.next().getName());
        assertEquals("test-Requires-C", reqIt.next().getName());
        assertEquals("test-Requires-interp-A", reqIt.next().getName());
        assertEquals("test-Requires-interp-B", reqIt.next().getName());
        assertEquals("test-Requires-interp-C", reqIt.next().getName());
        assertEquals("test-Requires-post-A", reqIt.next().getName());
        assertEquals("test-Requires-post-B", reqIt.next().getName());
        assertEquals("test-Requires-post-C", reqIt.next().getName());
        assertEquals("test-Requires-posttrans-A", reqIt.next().getName());
        assertEquals("test-Requires-posttrans-B", reqIt.next().getName());
        assertEquals("test-Requires-posttrans-C", reqIt.next().getName());
        assertEquals("test-Requires-postun-A", reqIt.next().getName());
        assertEquals("test-Requires-postun-B", reqIt.next().getName());
        assertEquals("test-Requires-postun-C", reqIt.next().getName());
        assertEquals("test-Requires-pre-A", reqIt.next().getName());
        assertEquals("test-Requires-pre-B", reqIt.next().getName());
        assertEquals("test-Requires-pre-C", reqIt.next().getName());
        assertEquals("test-Requires-pretrans-A", reqIt.next().getName());
        assertEquals("test-Requires-pretrans-B", reqIt.next().getName());
        assertEquals("test-Requires-pretrans-C", reqIt.next().getName());
        assertEquals("test-Requires-preun-A", reqIt.next().getName());
        assertEquals("test-Requires-preun-B", reqIt.next().getName());
        assertEquals("test-Requires-preun-C", reqIt.next().getName());
        assertEquals("test-Requires-rpmlib-A", reqIt.next().getName());
        assertEquals("test-Requires-rpmlib-B", reqIt.next().getName());
        assertEquals("test-Requires-rpmlib-C", reqIt.next().getName());
        assertEquals("test-Requires-verify-A", reqIt.next().getName());
        assertEquals("test-Requires-verify-B", reqIt.next().getName());
        assertEquals("test-Requires-verify-C", reqIt.next().getName());
        assertFalse(reqIt.hasNext());

        Iterator<Reldep> conflIt = info.getConflicts().iterator();
        assertEquals("test-Conflicts-A", conflIt.next().getName());
        assertEquals("test-Conflicts-B", conflIt.next().getName());
        assertEquals("test-Conflicts-C", conflIt.next().getName());
        assertFalse(conflIt.hasNext());

        Iterator<Reldep> obsIt = info.getObsoletes().iterator();
        assertEquals("test-Obsoletes-A", obsIt.next().getName());
        assertEquals("test-Obsoletes-B", obsIt.next().getName());
        assertEquals("test-Obsoletes-C", obsIt.next().getName());
        assertFalse(obsIt.hasNext());

        Iterator<Reldep> recIt = info.getRecommends().iterator();
        assertEquals("test-Recommends-A", recIt.next().getName());
        assertEquals("test-Recommends-B", recIt.next().getName());
        assertEquals("test-Recommends-C", recIt.next().getName());
        assertFalse(recIt.hasNext());

        Iterator<Reldep> sugIt = info.getSuggests().iterator();
        assertEquals("test-Suggests-A", sugIt.next().getName());
        assertEquals("test-Suggests-B", sugIt.next().getName());
        assertEquals("test-Suggests-C", sugIt.next().getName());
        assertFalse(sugIt.hasNext());

        Iterator<Reldep> supIt = info.getSupplements().iterator();
        assertEquals("test-Supplements-A", supIt.next().getName());
        assertEquals("test-Supplements-B", supIt.next().getName());
        assertEquals("test-Supplements-C", supIt.next().getName());
        assertFalse(supIt.hasNext());

        Iterator<Reldep> enhIt = info.getEnhances().iterator();
        assertEquals("test-Enhances-A", enhIt.next().getName());
        assertEquals("test-Enhances-B", enhIt.next().getName());
        assertEquals("test-Enhances-C", enhIt.next().getName());
        assertFalse(enhIt.hasNext());

        Iterator<Reldep> orderIt = info.getOrderWithRequires().iterator();
        assertEquals("test-OrderWithRequires-A", orderIt.next().getName());
        assertEquals("test-OrderWithRequires-B", orderIt.next().getName());
        assertEquals("test-OrderWithRequires-C", orderIt.next().getName());
        assertFalse(orderIt.hasNext());
    }
}
