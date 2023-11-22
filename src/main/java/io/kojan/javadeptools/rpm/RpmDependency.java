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

import static io.kojan.javadeptools.rpm.Rpm.*;

/**
 * RPM dependency with optional relation and version.
 * 
 * @author Mikolaj Izdebski
 */
public class RpmDependency {
    private static final String[] SENSES = new String[] { "", "<", ">", "<>", "=", "<=", ">=", "<>=", };

    private final String dnevr;
    private final String name;
    private final int flags;
    private final RpmVersion version;
    private final boolean isRich;

    RpmDependency(RpmDS ds) {
        dnevr = rpmdsDNEVR(ds);
        name = rpmdsN(ds);
        flags = rpmdsFlags(ds);
        version = new RpmVersion(rpmdsEVR(ds));
        isRich = rpmdsIsRich(ds) != 0;
    }

    /**
     * Returns the name part of RPM dependency.
     * <p>
     * For example, for dependency {@code something >= 1.2-3}, it returns
     * {@code something}.
     * 
     * @return name part of RPM dependency
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the relation part of RPM dependency.
     * <p>
     * For example, for dependency {@code something >= 1.2-3}, it returns
     * {@code >=}.
     * 
     * @return relation part of RPM dependency
     */
    public String getSense() {
        return SENSES[(flags & 0xF) >> 1];
    }

    /**
     * Returns the version part of RPM dependency.
     * <p>
     * For example, for dependency {@code something >= 1.2-3}, it returns
     * {@code 1.2-3}.
     * 
     * @return version part of RPM dependency
     */
    public RpmVersion getVersion() {
        return version;
    }

    /**
     * Determines whether the RPM dependency is a rich dependency, also known as
     * boolean dependency.
     * 
     * @return {@code true} iff RPM dependency is a rich (boolean) dependency
     * @see <a href=
     *      "https://rpm-software-management.github.io/rpm/manual/boolean_dependencies.html">RPM
     *      documentation about boolean dependencies</a>
     */
    public boolean isRich() {
        return isRich;
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
