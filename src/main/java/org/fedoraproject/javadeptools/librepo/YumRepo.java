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

import static org.fedoraproject.javadeptools.librepo.Librepo.LRO_DESTDIR;
import static org.fedoraproject.javadeptools.librepo.Librepo.LRO_REPOTYPE;
import static org.fedoraproject.javadeptools.librepo.Librepo.LRO_URLS;
import static org.fedoraproject.javadeptools.librepo.Librepo.LRO_YUMDLIST;
import static org.fedoraproject.javadeptools.librepo.Librepo.LR_YUMREPO;
import static org.fedoraproject.javadeptools.librepo.Librepo.lr_handle_free;
import static org.fedoraproject.javadeptools.librepo.Librepo.lr_handle_init;
import static org.fedoraproject.javadeptools.librepo.Librepo.lr_handle_perform;
import static org.fedoraproject.javadeptools.librepo.Librepo.lr_handle_setopt;
import static org.fedoraproject.javadeptools.librepo.Librepo.lr_result_free;
import static org.fedoraproject.javadeptools.librepo.Librepo.lr_result_init;

import java.io.IOException;
import java.nio.file.Path;

import com.sun.jna.Pointer;

/**
 * @author Mikolaj Izdebski
 */
public class YumRepo {
    private final String url;

    public YumRepo(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void download(Path targetPath) throws IOException {
        Pointer handle = null;
        Pointer result = null;
        try {
            handle = lr_handle_init();
            result = lr_result_init();
            if (handle == null || result == null)
                throw new RuntimeException("librepo initialization failed");

            lr_handle_setopt(handle, null, LRO_URLS, new String[] { url });
            lr_handle_setopt(handle, null, LRO_DESTDIR, targetPath.toString());
            lr_handle_setopt(handle, null, LRO_REPOTYPE, LR_YUMREPO);
            lr_handle_setopt(handle, null, LRO_YUMDLIST, new String[] { "primary", "filelists" });

            if (!lr_handle_perform(handle, result))
                throw new IOException();
        } finally {
            lr_result_free(result);
            lr_handle_free(handle);
        }
    }
}
