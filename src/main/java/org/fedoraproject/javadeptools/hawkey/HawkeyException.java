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

/**
 * Exception thrown when an error is detected by native Hawkey library.
 * 
 * @author Mikolaj Izdebski
 */
public class HawkeyException extends Exception {

    private static final long serialVersionUID = 1;

    private static String[] msgTable = new String[] { //
            "unknown error", //
            "general runtime error", //
            "client programming error", //
            "error propagated from libsolv", //
            "I/O error", //
            "cache write error", //
            "ill-formed query", //
            "unknown arch", //
            "validation check failed", //
            "ill-specified selector", //
            "goal found no solutions", //
            "the capability was not available" };

    HawkeyException(int errno) {
        super("Hawkey operation failed: " + msgTable[errno]);
    }

}
