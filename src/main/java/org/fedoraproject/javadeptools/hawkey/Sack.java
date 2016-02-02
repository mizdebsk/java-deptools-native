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
import static org.fedoraproject.javadeptools.hawkey.Hawkey.HY_GLOB;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.HY_LOAD_FILELISTS;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.HY_PKG_ARCH;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.HY_PKG_FILE;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.HY_PKG_NAME;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.HY_PKG_PROVIDES;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.HY_REPO_FILELISTS_FN;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.HY_REPO_MD_FN;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.HY_REPO_PRIMARY_FN;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_package_get_requires;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_packagelist_count;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_packagelist_free;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_packagelist_get;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_query_create;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_query_filter;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_query_filter_reldep_in;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_query_free;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_query_run;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_reldeplist_free;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_repo_create;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_repo_free;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_repo_set_string;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_sack_create;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_sack_free;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_sack_load_yum_repo;
import static org.fedoraproject.javadeptools.hawkey.HawkeyUtils.assumeNotNull;
import static org.fedoraproject.javadeptools.hawkey.HawkeyUtils.assumeZero;
import static org.fedoraproject.javadeptools.hawkey.HawkeyUtils.pkgList;

import java.nio.file.Path;
import java.util.List;

import com.sun.jna.Pointer;

/**
 * Collection of packages.
 * <p>
 * Sack objects typically allocate large amounts of memory from native heap.
 * Native memory should be reclaimed by calling {@link #close()}. If sack is not
 * closed properly, native memory release is delayed until {@link #finalize()}
 * is called by garbage collector.
 * 
 * @author Mikolaj Izdebski
 */
public class Sack extends AbstractHawkeyType {

    /**
     * Create a new, empty sack.
     * 
     * @param arch
     *            architecture to be used
     * @throws HawkeyException
     */
    public Sack(String arch) throws HawkeyException {
        super(hy_sack_create(null, arch, null, null, 0));
    }

    @Override
    void free() {
        hy_sack_free(self());
    }

    /**
     * Add packages from given YUM repository to this sack.
     * 
     * @param name
     *            string identifying the repository
     * @param md
     *            path to repository {@code repomd.xml} file
     * @param primary
     *            path to repository {@code primary.xml} file
     * @param filelists
     *            path to repository {@code filelists.xml} file
     * @throws HawkeyException
     */
    public void loadRepo(String name, Path md, Path primary, Path filelists) throws HawkeyException {
        Pointer repo = hy_repo_create(name);
        assumeNotNull(repo);

        try {
            hy_repo_set_string(repo, HY_REPO_MD_FN, md.toString());
            hy_repo_set_string(repo, HY_REPO_PRIMARY_FN, primary.toString());
            hy_repo_set_string(repo, HY_REPO_FILELISTS_FN, filelists.toString());
            assumeZero(hy_sack_load_yum_repo(self(), repo, HY_LOAD_FILELISTS));
        } finally {
            hy_repo_free(repo);
        }
    }

    /**
     * List packages from this sack with files matching given glob pattern.
     * 
     * @param glob
     *            wildcard pattern to match file names against
     * @return list of packages which own at least one file with matching name
     * @throws HawkeyException
     */
    public List<PackageInfo> globFile(String glob) throws HawkeyException {
        Pointer query = hy_query_create(self());
        assumeNotNull(query);

        try {
            hy_query_filter(query, HY_PKG_FILE, HY_GLOB, glob);
            return pkgList(hy_query_run(query));
        } finally {
            hy_query_free(query);
        }
    }

    /**
     * List packages required by package of given names. Dependencies must
     * already be resolved.
     * 
     * @param name
     *            name of package to resolve dependencies of
     * @param arch
     *            arch of package to resolve dependencies of
     * @return list of packages required by specified package
     * @throws HawkeyException
     */
    public List<PackageInfo> resolveRequires(String name, String arch) throws HawkeyException {
        Pointer q1 = hy_query_create(self());
        assumeNotNull(q1);

        try {
            hy_query_filter(q1, HY_PKG_NAME, HY_EQ, name);
            hy_query_filter(q1, HY_PKG_ARCH, HY_EQ, arch);

            Pointer plist = hy_query_run(q1);
            assumeNotNull(plist);
            try {
                int n = hy_packagelist_count(plist);
                if (n != 1)
                    return null;
                Pointer pkg = hy_packagelist_get(plist, 0);

                Pointer q2 = hy_query_create(self());
                assumeNotNull(q2);
                try {
                    Pointer rl = hy_package_get_requires(pkg);
                    assumeNotNull(rl);
                    hy_query_filter_reldep_in(q2, HY_PKG_PROVIDES, rl);
                    hy_reldeplist_free(rl);
                    return pkgList(hy_query_run(q2));
                } finally {
                    hy_query_free(q2);
                }
            } finally {
                hy_packagelist_free(plist);
            }
        } finally {
            hy_query_free(q1);
        }
    }
}
