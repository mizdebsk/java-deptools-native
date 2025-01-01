/*-
 * Copyright (c) 2023-2025 Red Hat, Inc.
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
/**
 * Module for read-only access to <a href="https://rpm.org/">RPM</a> packages.
 *
 * <p>It provides functionality for listing installed RPM packages, reading details about RPM
 * package or reading RPM package file contents as an archive.
 *
 * @author Mikolaj Izdebski
 * @see <a href="https://rpm.org/">RPM home page</a>
 * @see <a href="https://rpm-software-management.github.io/rpm/">RPM documentation</a>
 */
module io.kojan.javadeptools.rpm {
    requires transitive org.apache.commons.compress;
    requires static org.graalvm.nativeimage;

    exports io.kojan.javadeptools.rpm;
}
