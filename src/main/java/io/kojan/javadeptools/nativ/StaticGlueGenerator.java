/*-
 * Copyright (c) 2024 Red Hat, Inc.
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
package io.kojan.javadeptools.nativ;

import io.kojan.javadeptools.nativ.generator.NativeGlueGenerator;
import java.io.IOException;
import java.nio.file.Paths;

class StaticGlueGenerator {
    public static void main(String[] args) throws IOException {
        NativeGlueGenerator dl = new NativeGlueGenerator();
        dl.setNamespace("io.kojan.javadeptools.nativ");
        dl.emitClass(DynamicLinker.LibDL.class);
        dl.setJvmDefaultLookup();
        dl.emitTrampoline(DynamicLinker.LibDL.class);
        dl.write(Paths.get("src/main/java/io/kojan/javadeptools/nativ/LibDL_Impl.java"));
    }
}
