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
import static org.junit.Assert.assertTrue;

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
    public void testRpmTags() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/rpmtags-1-1.src.rpm");
        RpmInfo info = new RpmInfo(path);

        Iterator<String> exclArchIt = info.getExclusiveArch().iterator();
        assertEquals("aarch64", exclArchIt.next());
        assertEquals("ppc64le", exclArchIt.next());
        assertEquals("s390x", exclArchIt.next());
        assertEquals("x86_64", exclArchIt.next());
        assertEquals("noarch", exclArchIt.next());
        assertFalse(exclArchIt.hasNext());

        Iterator<String> buildArchIt = info.getBuildArchs().iterator();
        assertEquals("noarch", buildArchIt.next());
        assertFalse(buildArchIt.hasNext());

        assertEquals(null, info.getSourceRPM());
        assertEquals("CC0", info.getLicense());

        path = Paths.get("src/test/resources/rpm/rpmtags-1-1.noarch.rpm");
        info = new RpmInfo(path);

        assertEquals("rpmtags-1-1.src.rpm", info.getSourceRPM());
        assertEquals("CC0", info.getLicense());
    }

    @Test
    public void testBinaryPackage() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/foo-1-1.fc21.x86_64.rpm");
        RpmInfo info = new RpmInfo(path);

        assertFalse(info.isSourcePackage());
        assertEquals("foo-1-1.fc21.src.rpm", info.getSourceRPM());
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

    @Test
    public void testSourcePackage() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/foo-1-1.fc21.src.rpm");
        RpmInfo info = new RpmInfo(path);

        assertTrue(info.isSourcePackage());
        assertEquals(null, info.getSourceRPM());
        assertEquals("foo", info.getName());
        assertEquals(0, info.getEpoch());
        assertEquals("1", info.getVersion());
        assertEquals("1.fc21", info.getRelease());
        assertEquals("x86_64", info.getArch());

        Iterator<String> prov = info.getProvides().iterator();
        assertFalse(prov.hasNext());

        List<String> req = info.getRequires();
        assertEquals(2, req.size());
    }

    @Test
    public void testDependencies() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/soft-1-1.noarch.rpm");
        RpmInfo info = new RpmInfo(path);

        assertEquals("ASL 2.0", info.getLicense());

        Iterator<String> provIt = info.getProvides().iterator();
        assertEquals("soft", provIt.next());
        assertEquals("test-Provides-A", provIt.next());
        assertEquals("test-Provides-B", provIt.next());
        assertEquals("test-Provides-C", provIt.next());
        assertFalse(provIt.hasNext());

        Iterator<String> reqIt = info.getRequires().iterator();
        assertEquals("rpmlib(CompressedFileNames)", reqIt.next());
        assertEquals("rpmlib(FileDigests)", reqIt.next());
        assertEquals("rpmlib(PayloadFilesHavePrefix)", reqIt.next());
        assertEquals("rpmlib(PayloadIsZstd)", reqIt.next());
        assertEquals("test-Requires-A", reqIt.next());
        assertEquals("test-Requires-B", reqIt.next());
        assertEquals("test-Requires-C", reqIt.next());
        assertEquals("test-Requires-interp-A", reqIt.next());
        assertEquals("test-Requires-interp-B", reqIt.next());
        assertEquals("test-Requires-interp-C", reqIt.next());
        assertEquals("test-Requires-post-A", reqIt.next());
        assertEquals("test-Requires-post-B", reqIt.next());
        assertEquals("test-Requires-post-C", reqIt.next());
        assertEquals("test-Requires-posttrans-A", reqIt.next());
        assertEquals("test-Requires-posttrans-B", reqIt.next());
        assertEquals("test-Requires-posttrans-C", reqIt.next());
        assertEquals("test-Requires-postun-A", reqIt.next());
        assertEquals("test-Requires-postun-B", reqIt.next());
        assertEquals("test-Requires-postun-C", reqIt.next());
        assertEquals("test-Requires-pre-A", reqIt.next());
        assertEquals("test-Requires-pre-B", reqIt.next());
        assertEquals("test-Requires-pre-C", reqIt.next());
        assertEquals("test-Requires-pretrans-A", reqIt.next());
        assertEquals("test-Requires-pretrans-B", reqIt.next());
        assertEquals("test-Requires-pretrans-C", reqIt.next());
        assertEquals("test-Requires-preun-A", reqIt.next());
        assertEquals("test-Requires-preun-B", reqIt.next());
        assertEquals("test-Requires-preun-C", reqIt.next());
        assertEquals("test-Requires-rpmlib-A", reqIt.next());
        assertEquals("test-Requires-rpmlib-B", reqIt.next());
        assertEquals("test-Requires-rpmlib-C", reqIt.next());
        assertEquals("test-Requires-verify-A", reqIt.next());
        assertEquals("test-Requires-verify-B", reqIt.next());
        assertEquals("test-Requires-verify-C", reqIt.next());
        assertFalse(reqIt.hasNext());

        Iterator<String> conflIt = info.getConflicts().iterator();
        assertEquals("test-Conflicts-A", conflIt.next());
        assertEquals("test-Conflicts-B", conflIt.next());
        assertEquals("test-Conflicts-C", conflIt.next());
        assertFalse(conflIt.hasNext());

        Iterator<String> obsIt = info.getObsoletes().iterator();
        assertEquals("test-Obsoletes-A", obsIt.next());
        assertEquals("test-Obsoletes-B", obsIt.next());
        assertEquals("test-Obsoletes-C", obsIt.next());
        assertFalse(obsIt.hasNext());

        Iterator<String> recIt = info.getRecommends().iterator();
        assertEquals("test-Recommends-A", recIt.next());
        assertEquals("test-Recommends-B", recIt.next());
        assertEquals("test-Recommends-C", recIt.next());
        assertFalse(recIt.hasNext());

        Iterator<String> sugIt = info.getSuggests().iterator();
        assertEquals("test-Suggests-A", sugIt.next());
        assertEquals("test-Suggests-B", sugIt.next());
        assertEquals("test-Suggests-C", sugIt.next());
        assertFalse(sugIt.hasNext());

        Iterator<String> supIt = info.getSupplements().iterator();
        assertEquals("test-Supplements-A", supIt.next());
        assertEquals("test-Supplements-B", supIt.next());
        assertEquals("test-Supplements-C", supIt.next());
        assertFalse(supIt.hasNext());

        Iterator<String> enhIt = info.getEnhances().iterator();
        assertEquals("test-Enhances-A", enhIt.next());
        assertEquals("test-Enhances-B", enhIt.next());
        assertEquals("test-Enhances-C", enhIt.next());
        assertFalse(enhIt.hasNext());

        Iterator<String> orderIt = info.getOrderWithRequires().iterator();
        assertEquals("test-OrderWithRequires-A", orderIt.next());
        assertEquals("test-OrderWithRequires-B", orderIt.next());
        assertEquals("test-OrderWithRequires-C", orderIt.next());
        assertFalse(orderIt.hasNext());
    }
}
