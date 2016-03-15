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
package org.fedoraproject.javadeptools.librepo;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * @author Mikolaj Izdebski
 */
class Librepo {
    private static interface LibrepoLibrary extends Library {
        Pointer lr_handle_init();

        void lr_handle_free(Pointer handle);

        void lr_handle_setopt(Pointer handle, Pointer err, Object... option);

        boolean lr_handle_perform(Pointer handle, Pointer result, Pointer err);

        Pointer lr_result_init();

        void lr_result_free(Pointer result);
    }

    private static class Lazy {
        static final LibrepoLibrary LR = (LibrepoLibrary) Native.loadLibrary("repo", LibrepoLibrary.class);
    }

    static final int LR_YUMREPO = 1 << 1;

    static final int LRO_URLS = 1;
    static final int LRO_DESTDIR = 16;
    static final int LRO_REPOTYPE = 17;
    static final int LRO_YUMDLIST = 36;

    static final Pointer lr_handle_init() {
        return Lazy.LR.lr_handle_init();
    }

    static final void lr_handle_free(Pointer handle) {
        Lazy.LR.lr_handle_free(handle);
    }

    static final void lr_handle_setopt(Pointer handle, Pointer err, Object... option) {
        Lazy.LR.lr_handle_setopt(handle, err, option);
    }

    static final boolean lr_handle_perform(Pointer handle, Pointer result, Pointer err) {
        return Lazy.LR.lr_handle_perform(handle, result, err);
    }

    static final Pointer lr_result_init() {
        return Lazy.LR.lr_result_init();
    }

    static final void lr_result_free(Pointer result) {
        Lazy.LR.lr_result_free(result);
    }

}
