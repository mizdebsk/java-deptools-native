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
package org.fedoraproject.javadeptools.rpm;

import static org.fedoraproject.javadeptools.rpm.Rpm.*;

/**
 * @author Mikolaj Izdebski
 */
public class RpmDependency {
    private static final String[] SENSES = new String[] { "", "<", ">", "<>", "=", "<=", ">=", "<>=", };

    private final String dnevr;
    private final String name;
    private final int flags;
    private final String evr;

    RpmDependency(RpmDS ds) {
        dnevr = rpmdsDNEVR(ds);
        name = rpmdsN(ds);
        flags = rpmdsFlags(ds);
        evr = rpmdsEVR(ds);
    }

    public String getName() {
        return name;
    }

    public String getSense() {
        return SENSES[(flags & 0xF) >> 1];
    }

    public String getEVR() {
        return evr;
    }

    @Override
    public String toString() {
        return dnevr.substring(2);
    }

    @Override
    public int hashCode() {
        return dnevr.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RpmDependency && ((RpmDependency) obj).dnevr.equals(dnevr);
    }
}
