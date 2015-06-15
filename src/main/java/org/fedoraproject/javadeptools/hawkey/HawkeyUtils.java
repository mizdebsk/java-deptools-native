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

import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_free;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_get_errno;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_packagelist_count;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_packagelist_free;
import static org.fedoraproject.javadeptools.hawkey.Hawkey.hy_packagelist_get;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;

/**
 * @author Mikolaj Izdebski
 */
class HawkeyUtils {
    private static HawkeyException propagateError() throws HawkeyException {
        throw new HawkeyException(hy_get_errno());
    }

    static final void assumeNotNull(Pointer p) throws HawkeyException {
        if (p == null) {
            throw propagateError();
        }
    }

    static final void assumeZero(int code) throws HawkeyException {
        if (code != 0) {
            throw propagateError();
        }
    }

    static final String str(Pointer ptr) throws HawkeyException {
        assumeNotNull(ptr);
        try {
            return ptr.getString(0);
        } finally {
            hy_free(ptr);
        }
    }

    static final List<PackageInfo> pkgList(Pointer plist) throws HawkeyException {
        assumeNotNull(plist);
        try {
            int n = hy_packagelist_count(plist);
            PackageInfo[] pkgs = new PackageInfo[n];
            for (int i = 0; i < n; i++) {
                pkgs[i] = new PackageInfo(hy_packagelist_get(plist, i));
            }
            return Arrays.asList(pkgs);
        } finally {
            hy_packagelist_free(plist);
        }
    }

}
