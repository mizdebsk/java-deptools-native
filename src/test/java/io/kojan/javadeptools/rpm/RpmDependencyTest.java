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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

/**
 * @author Mikolaj Izdebski
 */
public class RpmDependencyTest {
    @Test
    public void testDependencies() throws Exception {
        Path path = Paths.get("src/test/resources/rpm/rpmtags-1-1.noarch.rpm");
        RpmInfo info = new RpmPackage(path).getInfo();

        RpmDependency dep0 = info.getRecommends().get(0);
        assertEquals("((ant and ivy) or maven >= 3.0.4)", dep0.toString());
        assertEquals("((ant and ivy) or maven >= 3.0.4)", dep0.getName());
        assertEquals("", dep0.getSense());
        assertEquals("", dep0.getVersion().toString());
        assertTrue(dep0.isRich());

        RpmDependency dep1 = info.getRecommends().get(1);
        assertEquals("bar = 23-4.5", dep1.toString());
        assertEquals("bar", dep1.getName());
        assertEquals("=", dep1.getSense());
        assertEquals("23-4.5", dep1.getVersion().toString());
        assertFalse(dep1.isRich());

        RpmDependency dep2 = info.getRecommends().get(2);
        assertEquals(
                "baz >= 3333333333:444444444444444444444-xaxaxayyyy.5517~77+8", dep2.toString());
        assertEquals("baz", dep2.getName());
        assertEquals(">=", dep2.getSense());
        assertEquals(
                "3333333333:444444444444444444444-xaxaxayyyy.5517~77+8",
                dep2.getVersion().toString());
        assertFalse(dep2.isRich());

        RpmDependency dep3 = info.getRecommends().get(3);
        assertEquals("foo < 1", dep3.toString());
        assertEquals("foo", dep3.getName());
        assertEquals("<", dep3.getSense());
        assertEquals("1", dep3.getVersion().toString());
        assertFalse(dep3.isRich());

        RpmDependency dep4 = info.getRecommends().get(4);
        assertEquals("nethack", dep4.toString());
        assertEquals("nethack", dep4.getName());
        assertEquals("", dep4.getSense());
        assertEquals("", dep4.getVersion().toString());
        assertFalse(dep4.isRich());

        Iterator<RpmDependency> provIt = info.getProvides().iterator();
        RpmDependency provs1 = provIt.next();
        assertEquals("rpmtags = 4242424242:1-1", provs1.toString());
        assertEquals("rpmtags", provs1.getName());
        assertEquals("=", provs1.getSense());
        assertEquals("4242424242:1-1", provs1.getVersion().toString());
        assertFalse(provs1.isRich());

        RpmDependency provs2 = provIt.next();
        assertEquals("which <= 10", provs2.toString());
        assertEquals("which", provs2.getName());
        assertEquals("<=", provs2.getSense());
        assertEquals("10", provs2.getVersion().toString());
        assertFalse(provs2.isRich());

        assertFalse(provIt.hasNext());

        Iterator<RpmDependency> conflIt = info.getConflicts().iterator();
        RpmDependency confl1 = conflIt.next();
        assertEquals("make = 2", confl1.toString());
        assertEquals("make", confl1.getName());
        assertEquals("=", confl1.getSense());
        assertEquals("2", confl1.getVersion().toString());
        assertFalse(confl1.isRich());

        assertFalse(conflIt.hasNext());

        Iterator<RpmDependency> obsIt = info.getObsoletes().iterator();
        RpmDependency obs1 = obsIt.next();
        assertEquals("rpmtags < 11", obs1.toString());
        assertEquals("rpmtags", obs1.getName());
        assertEquals("<", obs1.getSense());
        assertEquals("11", obs1.getVersion().toString());
        assertFalse(obs1.isRich());

        assertFalse(obsIt.hasNext());

        Iterator<RpmDependency> sugIt = info.getSuggests().iterator();
        RpmDependency sug1 = sugIt.next();
        assertEquals("java-latest-openjdk > 8", sug1.toString());
        assertEquals("java-latest-openjdk", sug1.getName());
        assertEquals(">", sug1.getSense());
        assertEquals("8", sug1.getVersion().toString());
        assertFalse(sug1.isRich());

        assertFalse(sugIt.hasNext());

        Iterator<RpmDependency> supIt = info.getSupplements().iterator();
        RpmDependency sup1 = supIt.next();
        assertEquals("tmt = 1", sup1.toString());
        assertEquals("tmt", sup1.getName());
        assertEquals("=", sup1.getSense());
        assertEquals("1", sup1.getVersion().toString());
        assertFalse(sup1.isRich());

        assertFalse(supIt.hasNext());

        Iterator<RpmDependency> enhIt = info.getEnhances().iterator();
        RpmDependency enh1 = enhIt.next();
        assertEquals("ant < 5", enh1.toString());
        assertEquals("ant", enh1.getName());
        assertEquals("<", enh1.getSense());
        assertEquals("5", enh1.getVersion().toString());
        assertFalse(enh1.isRich());

        assertFalse(enhIt.hasNext());

        Iterator<RpmDependency> orderIt = info.getOrderWithRequires().iterator();
        RpmDependency order1 = orderIt.next();
        assertEquals("maven >= 3", order1.toString());
        assertEquals("maven", order1.getName());
        assertEquals(">=", order1.getSense());
        assertEquals("3", order1.getVersion().toString());
        assertFalse(order1.isRich());

        assertFalse(orderIt.hasNext());
    }
}
