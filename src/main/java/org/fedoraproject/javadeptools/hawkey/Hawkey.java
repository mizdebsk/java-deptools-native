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

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * @author Mikolaj Izdebski
 */
final class Hawkey {
    private static interface HawkeyLibrary extends Library {

        Pointer hy_free(Pointer ptr);

        int hy_get_errno();

        int hy_goal_count_problems(Pointer goal);

        Pointer hy_goal_create(Pointer sack);

        Pointer hy_goal_describe_problem(Pointer goal, int i);

        void hy_goal_free(Pointer goal);

        int hy_goal_install_selector(Pointer goal, Pointer sltr);

        Pointer hy_goal_list_installs(Pointer goal);

        int hy_goal_run_flags(Pointer goal, int flags);

        void hy_package_free(Pointer pkg);

        String hy_package_get_arch(Pointer pkg);

        long hy_package_get_downloadsize(Pointer pkg);

        int hy_package_get_epoch(Pointer pkg);

        Pointer hy_package_get_files(Pointer pkg);

        long hy_package_get_installsize(Pointer pkg);

        Pointer hy_package_get_location(Pointer pkg);

        String hy_package_get_name(Pointer pkg);

        Pointer hy_package_get_nevra(Pointer pkg);

        Pointer hy_package_get_release(Pointer pkg);

        long hy_package_get_size(Pointer pkg);

        Pointer hy_package_get_version(Pointer pkg);

        boolean hy_package_identical(Pointer pkg1, Pointer pkg2);

        int hy_packagelist_count(Pointer plist);

        void hy_packagelist_free(Pointer plist);

        Pointer hy_packagelist_get(Pointer plist, int index);

        Pointer hy_query_create(Pointer sack);

        int hy_query_filter(Pointer q, int keyname, int cmp_type, String match);

        void hy_query_free(Pointer q);

        Pointer hy_query_run(Pointer q);

        Pointer hy_repo_create(String name);

        void hy_repo_free(Pointer repo);

        void hy_repo_set_string(Pointer repo, int which, String str_val);

        Pointer hy_sack_create(String cachedir, String arch, String rootdir, String logfile, int flags);

        void hy_sack_free(Pointer sack);

        int hy_sack_load_yum_repo(Pointer sack, Pointer hrepo, int flags);

        Pointer hy_selector_create(Pointer sack);

        void hy_selector_free(Pointer sltr);

        int hy_selector_set(Pointer sltr, int keyname, int cmp_type, String match);

        void hy_stringarray_free(Pointer strs);

        int hy_stringarray_length(Pointer strs);
    }

    private static class Lazy {
        static final HawkeyLibrary HY = (HawkeyLibrary) Native.loadLibrary("hawkey", HawkeyLibrary.class);
    }

    static final int HY_LOAD_FILELISTS = 1 << 1;

    static final int HY_REPO_MD_FN = 1;
    static final int HY_REPO_PRIMARY_FN = 3;
    static final int HY_REPO_FILELISTS_FN = 4;

    static final int HY_EQ = 1 << 8;
    static final int HY_GLOB = 1 << 12;

    static final int HY_PKG_FILE = 7;
    static final int HY_PKG_NAME = 8;

    static final int HY_IGNORE_WEAK_DEPS = 1 << 3;

    static final Pointer hy_free(Pointer ptr) {
        return Lazy.HY.hy_free(ptr);
    }

    static final int hy_get_errno() {
        return Lazy.HY.hy_get_errno();
    }

    static final int hy_goal_count_problems(Pointer goal) {
        return Lazy.HY.hy_goal_count_problems(goal);
    }

    static final Pointer hy_goal_create(Pointer sack) {
        return Lazy.HY.hy_goal_create(sack);
    }

    static Pointer hy_goal_describe_problem(Pointer goal, int i) {
        return Lazy.HY.hy_goal_describe_problem(goal, i);
    }

    static final void hy_goal_free(Pointer goal) {
        Lazy.HY.hy_goal_free(goal);
    }

    static final int hy_goal_install_selector(Pointer goal, Pointer sltr) {
        return Lazy.HY.hy_goal_install_selector(goal, sltr);
    }

    static final Pointer hy_goal_list_installs(Pointer goal) {
        return Lazy.HY.hy_goal_list_installs(goal);
    }

    static final int hy_goal_run_flags(Pointer goal, int flags) {
        return Lazy.HY.hy_goal_run_flags(goal, flags);
    }

    static final void hy_package_free(Pointer pkg) {
        Lazy.HY.hy_package_free(pkg);
    }

    static final String hy_package_get_arch(Pointer pkg) {
        return Lazy.HY.hy_package_get_arch(pkg);
    }

    static final long hy_package_get_downloadsize(Pointer pkg) {
        return Lazy.HY.hy_package_get_downloadsize(pkg);
    }

    static final int hy_package_get_epoch(Pointer pkg) {
        return Lazy.HY.hy_package_get_epoch(pkg);
    }

    static final Pointer hy_package_get_files(Pointer pkg) {
        return Lazy.HY.hy_package_get_files(pkg);
    }

    static final long hy_package_get_installsize(Pointer pkg) {
        return Lazy.HY.hy_package_get_installsize(pkg);
    }

    static final Pointer hy_package_get_location(Pointer pkg) {
        return Lazy.HY.hy_package_get_location(pkg);
    }

    static final String hy_package_get_name(Pointer pkg) {
        return Lazy.HY.hy_package_get_name(pkg);
    }

    static final Pointer hy_package_get_nevra(Pointer pkg) {
        return Lazy.HY.hy_package_get_nevra(pkg);
    }

    static final Pointer hy_package_get_release(Pointer pkg) {
        return Lazy.HY.hy_package_get_release(pkg);
    }

    static final long hy_package_get_size(Pointer pkg) {
        return Lazy.HY.hy_package_get_size(pkg);
    }

    static final Pointer hy_package_get_version(Pointer pkg) {
        return Lazy.HY.hy_package_get_version(pkg);
    }

    static final boolean hy_package_identical(Pointer pkg1, Pointer pkg2) {
        return Lazy.HY.hy_package_identical(pkg1, pkg2);
    }

    static final int hy_packagelist_count(Pointer plist) {
        return Lazy.HY.hy_packagelist_count(plist);
    }

    static final void hy_packagelist_free(Pointer plist) {
        Lazy.HY.hy_packagelist_free(plist);
    }

    static final Pointer hy_packagelist_get(Pointer plist, int index) {
        return Lazy.HY.hy_packagelist_get(plist, index);
    }

    static final Pointer hy_query_create(Pointer sack) {
        return Lazy.HY.hy_query_create(sack);
    }

    static final int hy_query_filter(Pointer q, int keyname, int cmp_type, String match) {
        return Lazy.HY.hy_query_filter(q, keyname, cmp_type, match);
    }

    static final void hy_query_free(Pointer q) {
        Lazy.HY.hy_query_free(q);
    }

    static final Pointer hy_query_run(Pointer q) {
        return Lazy.HY.hy_query_run(q);
    }

    static final Pointer hy_repo_create(String name) {
        return Lazy.HY.hy_repo_create(name);
    }

    static final void hy_repo_free(Pointer repo) {
        Lazy.HY.hy_repo_free(repo);
    }

    static final void hy_repo_set_string(Pointer repo, int which, String str_val) {
        Lazy.HY.hy_repo_set_string(repo, which, str_val);
    }

    static final Pointer hy_sack_create(String cachedir, String arch, String rootdir, String logfile, int flags) {
        return Lazy.HY.hy_sack_create(cachedir, arch, rootdir, logfile, flags);
    }

    static final void hy_sack_free(Pointer sack) {
        Lazy.HY.hy_sack_free(sack);
    }

    static final int hy_sack_load_yum_repo(Pointer sack, Pointer hrepo, int flags) {
        return Lazy.HY.hy_sack_load_yum_repo(sack, hrepo, flags);
    }

    static final Pointer hy_selector_create(Pointer sack) {
        return Lazy.HY.hy_selector_create(sack);
    }

    static final void hy_selector_free(Pointer sltr) {
        Lazy.HY.hy_selector_free(sltr);
    }

    static final int hy_selector_set(Pointer sltr, int keyname, int cmp_type, String match) {
        return Lazy.HY.hy_selector_set(sltr, keyname, cmp_type, match);
    }

    static final void hy_stringarray_free(Pointer strs) {
        Lazy.HY.hy_stringarray_free(strs);
    }

    static final int hy_stringarray_length(Pointer strs) {
        return Lazy.HY.hy_stringarray_length(strs);
    }

}
