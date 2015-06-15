/*-
 * Copyright (c) 2015 Red Hat, Inc.
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
package org.fedoraproject.javadeptools.hawkey;

import static org.fedoraproject.javadeptools.hawkey.Hawkey.HY_EQ;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.HY_PKG_NAME;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_goal_count_problems;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_goal_create;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_goal_describe_problem;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_goal_free;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_goal_install_selector;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_goal_list_installs;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_goal_run;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_selector_create;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_selector_free;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_selector_set;
import static org.fedoraproject.javadeptools.hawkey.HawkeyUtils.assumeNotNull;
import static org.fedoraproject.javadeptools.hawkey.HawkeyUtils.assumeZero;
import static org.fedoraproject.javadeptools.hawkey.HawkeyUtils.pkgList;
import static org.fedoraproject.javadeptools.hawkey.HawkeyUtils.str;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;

/**
 * Package installation state.
 * <p>
 * Goal objects allocate memory from native heap. Native memory should be
 * reclaimed by calling {@link #close()}. If goal is not closed properly, native
 * memory release is delayed until {@link #finalize()} is called by garbage
 * collector.
 * 
 * @author Mikolaj Izdebski
 */
public class Goal extends AbstractHawkeyType {

    private final Sack sack;

    /**
     * Create new goal from specified sack.
     * 
     * @param sack
     *            package collection to use
     * @throws HawkeyException
     */
    public Goal(Sack sack) throws HawkeyException {
        super(hy_goal_create(sack.self()));
        this.sack = sack;
    }

    @Override
    void free() {
        hy_goal_free(self());
    }

    private void ensureSackNotClosed() {
        sack.self();
    }

    /**
     * Add specified package to installed set.
     * <p>
     * Package installation is delayed until goal is ran.
     * 
     * @param name
     *            name of package to mark for installation
     * @throws HawkeyException
     */
    public void install(String name) throws HawkeyException {
        ensureSackNotClosed();
        Pointer sltr = hy_selector_create(sack.self());
        assumeNotNull(sltr);
        try {
            hy_selector_set(sltr, HY_PKG_NAME, HY_EQ, name);
            assumeZero(hy_goal_install_selector(self(), sltr));
        } finally {
            hy_selector_free(sltr);
        }
    }

    /**
     * Resolve dependencies of all packages marked for installation.
     * 
     * @return true iff all packages were installed succcessfully, false if
     *         there are problems detected
     * @throws HawkeyException
     */
    public boolean run() throws HawkeyException {
        ensureSackNotClosed();
        return hy_goal_run(self()) == 0;
    }

    /**
     * Get list of problems that prevented goal from being achieved.
     * 
     * @return list of strings describing problems found, or empty list if there
     *         are no problems
     * @throws HawkeyException
     */
    public List<String> getProblems() throws HawkeyException {
        ensureSackNotClosed();
        int n = hy_goal_count_problems(self());
        String[] probs = new String[n];
        for (int i = 0; i < n; i++) {
            probs[i] = str(hy_goal_describe_problem(self(), i));
        }
        return Arrays.asList(probs);
    }

    /**
     * Get list of installed packages, including packages marked for
     * installation and all their dependencies.
     * 
     * @return list of packages that are currently installed
     * @throws HawkeyException
     */
    public List<PackageInfo> listInstalls() throws HawkeyException {
        ensureSackNotClosed();
        return pkgList(hy_goal_list_installs(self()));
    }
}
