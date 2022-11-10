/*-
 * Copyright (c) 2012-2016 Red Hat, Inc.
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
package org.fedoraproject.javadeptools.ffi;

/**
 * @author Mikolaj Izdebski
 */
public final class Rpm {
    public static final int RPMRC_OK = 0;
    public static final int RPMRC_NOTFOUND = 1;
    public static final int RPMRC_FAIL = 2;
    public static final int RPMRC_NOTTRUSTED = 3;
    public static final int RPMRC_NOKEY = 4;

    public static final int RPMVSF_NOHDRCHK = 1 << 0;
    public static final int RPMVSF_NOSHA1HEADER = 1 << 8;
    public static final int RPMVSF_NODSAHEADER = 1 << 10;
    public static final int RPMVSF_NORSAHEADER = 1 << 11;
    public static final int RPMVSF_NOMD5 = 1 << 17;
    public static final int RPMVSF_NODSA = 1 << 18;
    public static final int RPMVSF_NORSA = 1 << 19;

    public static final int RPMTAG_NAME = 1000;
    public static final int RPMTAG_VERSION = 1001;
    public static final int RPMTAG_RELEASE = 1002;
    public static final int RPMTAG_EPOCH = 1003;
    public static final int RPMTAG_ARCH = 1022;
    public static final int RPMTAG_SOURCERPM = 1044;
    public static final int RPMTAG_BUILDARCHS = 1089;
    public static final int RPMTAG_EXCLUSIVEARCH = 1061;
    public static final int RPMTAG_PROVIDENAME = 1047;
    public static final int RPMTAG_REQUIRENAME = 1049;
    public static final int RPMTAG_CONFLICTNAME = 1054;
    public static final int RPMTAG_OBSOLETENAME = 1090;
    public static final int RPMTAG_SOURCEPACKAGE = 1106;
    public static final int RPMTAG_PAYLOADCOMPRESSOR = 1125;
    public static final int RPMTAG_PAYLOADFORMAT = 1124;
    public static final int RPMTAG_ORDERNAME = 5035;
    public static final int RPMTAG_RECOMMENDNAME = 5046;
    public static final int RPMTAG_SUGGESTNAME = 5049;
    public static final int RPMTAG_SUPPLEMENTNAME = 5052;
    public static final int RPMTAG_ENHANCENAME = 5055;

    public static final int HEADERGET_MINMEM = 1 << 0;

    public static final int RPMDBI_INSTFILENAMES = 5040;
}
