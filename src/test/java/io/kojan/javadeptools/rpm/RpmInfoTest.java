/*-
 * Copyright (c) 2020-2023 Red Hat, Inc.
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * @author Mikolaj Izdebski
 */
public class RpmInfoTest {
    @Test
    public void testRpmTags() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/rpmtags-1-1.src.rpm");
        RpmInfo info = new RpmPackage(path).getInfo();

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
        assertEquals(Optional.of(4242424242L), info.getEpoch());
        assertEquals("CC0", info.getLicense());

        Iterator<RpmDependency> reqIt = info.getRequires().iterator();
        assertEquals("bash", reqIt.next().toString());

        Iterator<RpmDependency> provIt = info.getProvides().iterator();
        assertEquals("rpmtags = 4242424242:1-1", provIt.next().toString());
        assertFalse(provIt.hasNext());

        path = Paths.get("src/test/resources/rpm/rpmtags-1-1.noarch.rpm");
        info = new RpmPackage(path).getInfo();

        assertEquals(Optional.of(4242424242L), info.getEpoch());
        assertEquals("rpmtags-1-1.src.rpm", info.getSourceRPM());
        assertEquals("CC0", info.getLicense());
    }

    @Test
    public void testBinaryPackage() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/foo-1-1.fc21.x86_64.rpm");
        RpmInfo info = new RpmPackage(path).getInfo();

        assertFalse(info.isSourcePackage());
        assertEquals("foo-1-1.fc21.src.rpm", info.getSourceRPM());
        assertEquals("foo", info.getName());
        assertEquals(Optional.empty(), info.getEpoch());
        assertEquals("1", info.getVersion());
        assertEquals("1.fc21", info.getRelease());
        assertEquals("x86_64", info.getArch());

        Iterator<RpmDependency> prov = info.getProvides().iterator();
        assertEquals("foo = 1-1.fc21", prov.next().toString());
        assertEquals("foo(x86-64) = 1-1.fc21", prov.next().toString());
        assertFalse(prov.hasNext());

        List<RpmDependency> req = info.getRequires();
        assertEquals(4, req.size());
    }

    @Test
    public void testSourcePackage() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/foo-1-1.fc21.src.rpm");
        RpmInfo info = new RpmPackage(path).getInfo();

        assertTrue(info.isSourcePackage());
        assertEquals(null, info.getSourceRPM());
        assertEquals("foo", info.getName());
        assertEquals(Optional.empty(), info.getEpoch());
        assertEquals("1", info.getVersion());
        assertEquals("1.fc21", info.getRelease());
        assertEquals("x86_64", info.getArch());

        Iterator<RpmDependency> prov = info.getProvides().iterator();
        assertFalse(prov.hasNext());

        List<RpmDependency> req = info.getRequires();
        assertEquals(2, req.size());
    }

    @Test
    public void testDependencies() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/soft-1-1.noarch.rpm");
        RpmInfo info = new RpmPackage(path).getInfo();

        assertEquals("ASL 2.0", info.getLicense());

        Iterator<RpmDependency> provIt = info.getProvides().iterator();
        assertEquals("soft = 1-1", provIt.next().toString());
        assertEquals("test-Provides-A", provIt.next().toString());
        assertEquals("test-Provides-B", provIt.next().toString());
        assertEquals("test-Provides-C", provIt.next().toString());
        assertFalse(provIt.hasNext());

        Iterator<RpmDependency> reqIt = info.getRequires().iterator();
        assertEquals("rpmlib(CompressedFileNames) <= 3.0.4-1", reqIt.next().toString());
        assertEquals("rpmlib(FileDigests) <= 4.6.0-1", reqIt.next().toString());
        assertEquals("rpmlib(PayloadFilesHavePrefix) <= 4.0-1", reqIt.next().toString());
        assertEquals("rpmlib(PayloadIsZstd) <= 5.4.18-1", reqIt.next().toString());
        assertEquals("test-Requires-A", reqIt.next().toString());
        assertEquals("test-Requires-B", reqIt.next().toString());
        assertEquals("test-Requires-C", reqIt.next().toString());
        assertEquals("test-Requires-interp-A", reqIt.next().toString());
        assertEquals("test-Requires-interp-B", reqIt.next().toString());
        assertEquals("test-Requires-interp-C", reqIt.next().toString());
        assertEquals("test-Requires-post-A", reqIt.next().toString());
        assertEquals("test-Requires-post-B", reqIt.next().toString());
        assertEquals("test-Requires-post-C", reqIt.next().toString());
        assertEquals("test-Requires-posttrans-A", reqIt.next().toString());
        assertEquals("test-Requires-posttrans-B", reqIt.next().toString());
        assertEquals("test-Requires-posttrans-C", reqIt.next().toString());
        assertEquals("test-Requires-postun-A", reqIt.next().toString());
        assertEquals("test-Requires-postun-B", reqIt.next().toString());
        assertEquals("test-Requires-postun-C", reqIt.next().toString());
        assertEquals("test-Requires-pre-A", reqIt.next().toString());
        assertEquals("test-Requires-pre-B", reqIt.next().toString());
        assertEquals("test-Requires-pre-C", reqIt.next().toString());
        assertEquals("test-Requires-pretrans-A", reqIt.next().toString());
        assertEquals("test-Requires-pretrans-B", reqIt.next().toString());
        assertEquals("test-Requires-pretrans-C", reqIt.next().toString());
        assertEquals("test-Requires-preun-A", reqIt.next().toString());
        assertEquals("test-Requires-preun-B", reqIt.next().toString());
        assertEquals("test-Requires-preun-C", reqIt.next().toString());
        assertEquals("test-Requires-rpmlib-A", reqIt.next().toString());
        assertEquals("test-Requires-rpmlib-B", reqIt.next().toString());
        assertEquals("test-Requires-rpmlib-C", reqIt.next().toString());
        assertEquals("test-Requires-verify-A", reqIt.next().toString());
        assertEquals("test-Requires-verify-B", reqIt.next().toString());
        assertEquals("test-Requires-verify-C", reqIt.next().toString());
        assertFalse(reqIt.hasNext());

        Iterator<RpmDependency> conflIt = info.getConflicts().iterator();
        assertEquals("test-Conflicts-A", conflIt.next().toString());
        assertEquals("test-Conflicts-B", conflIt.next().toString());
        assertEquals("test-Conflicts-C", conflIt.next().toString());
        assertFalse(conflIt.hasNext());

        Iterator<RpmDependency> obsIt = info.getObsoletes().iterator();
        assertEquals("test-Obsoletes-A", obsIt.next().toString());
        assertEquals("test-Obsoletes-B", obsIt.next().toString());
        assertEquals("test-Obsoletes-C", obsIt.next().toString());
        assertFalse(obsIt.hasNext());

        Iterator<RpmDependency> recIt = info.getRecommends().iterator();
        assertEquals("test-Recommends-A", recIt.next().toString());
        assertEquals("test-Recommends-B", recIt.next().toString());
        assertEquals("test-Recommends-C", recIt.next().toString());
        assertFalse(recIt.hasNext());

        Iterator<RpmDependency> sugIt = info.getSuggests().iterator();
        assertEquals("test-Suggests-A", sugIt.next().toString());
        assertEquals("test-Suggests-B", sugIt.next().toString());
        assertEquals("test-Suggests-C", sugIt.next().toString());
        assertFalse(sugIt.hasNext());

        Iterator<RpmDependency> supIt = info.getSupplements().iterator();
        assertEquals("test-Supplements-A", supIt.next().toString());
        assertEquals("test-Supplements-B", supIt.next().toString());
        assertEquals("test-Supplements-C", supIt.next().toString());
        assertFalse(supIt.hasNext());

        Iterator<RpmDependency> enhIt = info.getEnhances().iterator();
        assertEquals("test-Enhances-A", enhIt.next().toString());
        assertEquals("test-Enhances-B", enhIt.next().toString());
        assertEquals("test-Enhances-C", enhIt.next().toString());
        assertFalse(enhIt.hasNext());

        Iterator<RpmDependency> orderIt = info.getOrderWithRequires().iterator();
        assertEquals("test-OrderWithRequires-A", orderIt.next().toString());
        assertEquals("test-OrderWithRequires-B", orderIt.next().toString());
        assertEquals("test-OrderWithRequires-C", orderIt.next().toString());
        assertFalse(orderIt.hasNext());
    }
}
