/*-
 * Copyright (c) 2024-2025 Red Hat, Inc.
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
package io.kojan.javadeptools.nativ.generator;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JavaCodeGenerator {

    /** Optional Java namespace (package) to put generated code under. */
    private String namespace;

    /** Generate fully-qualified class names and do not produce imports. */
    private boolean fqcn = false;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public boolean isFqcn() {
        return fqcn;
    }

    public void setFqcn(boolean fqcn) {
        this.fqcn = fqcn;
    }

    protected JavaCodeGenerator() {}

    private int indent;
    private boolean blank = true;
    private final StringBuilder codeBuilder = new StringBuilder();
    private final Set<String> imports = new TreeSet<>();

    /** Increase current indent by n levels. */
    private void indent(int n) {
        if (!blank) {
            throw new IllegalStateException("indent() called on non-blank line");
        }
        indent += n;
    }

    /** Decrease current indent by n levels. */
    private void dedent(int n) {
        if (!blank) {
            throw new IllegalStateException("dedent() called on non-blank line");
        }
        indent -= n;
        if (indent < 0) {
            throw new IllegalStateException("Negative indent!");
        }
    }

    /** Print to String */
    protected String ps(Object... objects) {
        StringBuilder sb = new StringBuilder();
        if (objects.length > 0) {
            for (Object obj : objects) {
                if (obj instanceof Class<?> cls) {
                    sb.append(javaType(cls));
                } else {
                    sb.append(obj);
                }
            }
        }
        return sb.toString();
    }

    /** Print with Advancing */
    protected void pa(Object... objects) {
        pn(objects);
        blank = true;
        char lastCh =
                codeBuilder.length() > 0 ? codeBuilder.charAt(codeBuilder.length() - 1) : '\0';
        if (lastCh == '{') {
            indent(1);
        } else if (lastCh == '(') {
            indent(2);
        }
        codeBuilder.append("\n");
    }

    /** Print with No advancing */
    protected void pn(Object... objects) {
        if (objects.length > 0) {
            String str = ps(objects);
            if (blank) {
                char firstCh = str.length() > 0 ? str.charAt(0) : '\0';
                if (firstCh == '}') {
                    dedent(1);
                } else if (firstCh == ')') {
                    dedent(2);
                }
                codeBuilder.append("    ".repeat(indent));
                blank = false;
            }
            codeBuilder.append(str);
        }
    }

    /** Print Joining stream */
    protected void pj(Stream<String> stream) {
        pn(stream.collect(Collectors.joining(", ")));
    }

    /**
     * Return formatted name of given Java type. Either fully-qualified class name (FQCN) or short
     * name, depending on configuration. If using short name, then also add appropriate import.
     */
    protected String javaType(Class<?> type) {
        if (fqcn) {
            return type.getCanonicalName();
        }
        String cn = type.getCanonicalName();
        if (cn.contains(".")) {
            String pkg = cn.substring(0, cn.lastIndexOf('.'));
            // No imports needed for classes in java.lang and in the current namespace
            if (!pkg.equals("java.lang") && !pkg.equals(namespace)) {
                imports.add(cn);
            }
        }
        return type.getSimpleName();
    }

    /** Generate and write code for the whole compilation unit. */
    public void write(Path outputFile) throws IOException {
        if (indent != 0) {
            throw new IllegalStateException(
                    "Final indent at the end of compilation unit should be zero");
        }

        try (Writer w = Files.newBufferedWriter(outputFile)) {
            if (namespace != null) {
                w.write("package " + namespace + ";\n\n");
                for (String imp : imports) {
                    w.write("import " + imp + ";\n");
                }
                w.write(codeBuilder.toString());
            }
        }
    }
}
