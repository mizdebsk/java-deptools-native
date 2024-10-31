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
package io.kojan.javadeptools.nativ.generator;

import io.kojan.javadeptools.nativ.AbstractNativeProxy;
import io.kojan.javadeptools.nativ.Native;
import io.kojan.javadeptools.nativ.NativeObject;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class NativeGlueGenerator extends JavaCodeGenerator {

    /** Add Javadoc comments to generated methods and classes. */
    private boolean javadoc = true;

    /** Return native type for given Java type */
    private Class<?> nativeType(Class<?> jType) {
        return switch (jType) {
            case Class<?> cls when String.class.isAssignableFrom(cls) -> MemorySegment.class;
            case Class<?> cls when NativeObject.class.isAssignableFrom(cls) -> MemorySegment.class;
            case Class<?> cls when int.class.isAssignableFrom(cls) -> int.class;
            case Class<?> cls when long.class.isAssignableFrom(cls) -> long.class;
            default -> throw new IllegalStateException("data type is not supported: " + jType);
        };
    }

    /** Return native address layout for given Java type */
    private String layout(Class<?> type) {
        if (Void.TYPE.isAssignableFrom(type)) {
            return "VOID";
        } else if (String.class.isAssignableFrom(type)) {
            return "STR";
        } else if (NativeObject.class.isAssignableFrom(type)) {
            return "OBJ";
        } else if (long.class.isAssignableFrom(type)) {
            return "LONG";
        } else if (int.class.isAssignableFrom(type)) {
            return "INT";
        } else if (Buffer.class.isAssignableFrom(type)) {
            return "BUFF";
        }
        throw new IllegalStateException("data type is not supported: " + type);
    }

    /** Emit code for down-converting Java type to native type */
    private void emitDownConvert(Class<?> jType, String jName) {
        if (String.class.isAssignableFrom(jType)) {
            pn("downConvertString(", jName, ", arena)");
        } else if (NativeObject.class.isAssignableFrom(jType)) {
            pn("downConvertObject(", jName, ")");
        } else if (Buffer.class.isAssignableFrom(jType)) {
            pn("downConvertBuffer(", jName, ")");
        } else {
            pn(jName);
        }
    }

    /**
     * Emit optional code for up-converting native type to Java type, if needed. Return true if
     * up-converting was needed.
     */
    private boolean emitUpConvert(Class<?> jType) {
        if (String.class.isAssignableFrom(jType)) {
            pn("upConvertString(");
            return true;
        } else if (NativeObject.class.isAssignableFrom(jType)) {
            pn("upConvertObject(", jType, "::new, ");
            return true;
        }
        return false;
    }

    /** Generate code for class constructor. */
    private void emitConstructor(Class<?> iface) {
        pa("public ", iface.getSimpleName(), "_Impl(", SymbolLookup.class, " lookup) {");
        pa("super(lookup);");
        pa("}");
        pa();
    }

    /** Generate code for a single method stub. */
    private void genMethodStub(Method method) {
        List<Parameter> params = Arrays.asList(method.getParameters());
        Class<?> retType = method.getReturnType();
        boolean ret = !retType.equals(Void.TYPE);
        if (javadoc) {
            pa("/**");
            pa(" * Method stub that invokes native method {@code ", method.getName(), "}.");
            for (Parameter param : params) {
                pa(" * @param ", param.getName(), " ", param.getType());
            }
            if (ret) {
                pa(" * @return ", retType);
            }
            pa(" */");
        }
        pa("@Override");
        pn("public ", retType, " ", method.getName() + "(");
        pj(params.stream().map(param -> javaType(param.getType()) + " " + param.getName()));
        pa(") {");
        // Arena is need only for down-converting Strings
        if (params.stream().anyMatch(c -> c.getType().equals(String.class))) {
            pa("try (", Arena.class, " arena = ", Arena.class, ".ofConfined()) {");
        } else {
            pa("try {");
        }
        boolean upConvertNeeded = false;
        if (ret) {
            pn("return ");
            upConvertNeeded = emitUpConvert(retType);
            pn("(", nativeType(retType), ")");
        }
        pa("mh_", method.getName(), ".invokeExact(");
        for (var it = params.iterator(); it.hasNext(); ) {
            Parameter param = it.next();
            emitDownConvert(param.getType(), param.getName());
            pa(it.hasNext() ? "," : "");
        }
        if (upConvertNeeded) {
            pn(")");
        }
        pa(");");

        pa("} catch (", Throwable.class, " _t) {");
        pn("throw new ", RuntimeException.class);
        pa("(\"Failed to invoke native function ", method.getName(), "\", _t);");
        pa("}");
        pa("}");
        pa();
    }

    /** Generate code for method handle member variable. */
    private void emitMethodHandle(Method method) {
        List<Parameter> params = Arrays.asList(method.getParameters());
        pn("private final ", MethodHandle.class, " mh_", method.getName(), " = makeMethodHandle(");
        pn(layout(method.getReturnType()), ", ");
        pn("\"", method.getName(), "\"", params.isEmpty() ? "" : ", ");
        pj(params.stream().map(param -> layout(param.getType())));
        pa(");");
        pa();
    }

    /** Generate code for getFunctionLayouts method. Used by GraalVM native image generation. */
    private void emitLayoutsGetter(Collection<Method> methods) {
        Set<String> layouts = new TreeSet<>();

        for (Method method : methods) {
            List<Parameter> params = Arrays.asList(method.getParameters());
            Class<?> retType = method.getReturnType();
            boolean ret = !retType.equals(Void.TYPE);
            layouts.add(
                    ps(
                            FunctionDescriptor.class,
                            ".of",
                            ret ? "" : "Void",
                            "(",
                            ret ? layout(retType) : "",
                            !ret || params.isEmpty() ? "" : ", ",
                            params.stream()
                                    .map(param -> layout(param.getType()))
                                    .collect(Collectors.joining(", ")),
                            ")"));
        }

        pa("public static Iterable<", FunctionDescriptor.class, "> getFunctionLayouts() {");
        pa("return ", Arrays.class, ".asList(");
        for (var it = layouts.iterator(); it.hasNext(); ) {
            pa(it.next(), it.hasNext() ? "," : "");
        }
        pa(");");
        pa("}");
    }

    /** Generate code for proxy class. */
    public void emitClass(Class<?> iface) {
        pa();
        if (javadoc) {
            pa("/**");
            pa(" * Native implementation of ", iface, ".");
            pa(" */");
        }
        pn("final class ", iface.getSimpleName() + "_Impl");
        pn(" extends ", AbstractNativeProxy.class);
        pn(" implements ", iface);
        pa(" {");

        emitConstructor(iface);

        // The order of methods returned by reflection is not stable and can vary.
        // Therefore sort methods by name for stable, reproducible output.
        List<Method> methods = Arrays.asList(iface.getMethods());
        Collections.sort(methods, Comparator.comparing(Method::getName));

        for (Method method : methods) {
            genMethodStub(method);
            emitMethodHandle(method);
        }

        emitLayoutsGetter(methods);

        pa("}");
    }

    private String lookup;

    public void setJvmDefaultLookup() {
        lookup = ps(Native.class, ".jvmDefaultLookup()");
    }

    public void setDlsymDefaultLookup() {
        lookup = ps(Native.class, ".dlsymDefaultLookup()");
    }

    public void setDlopenLookup(String lib0, String... libs) {
        lookup =
                ps(
                        Native.class,
                        ".dlopenLookup(\"",
                        lib0,
                        "\"",
                        Arrays.asList(libs).stream()
                                .map(lib -> ", \"" + lib + "\"")
                                .collect(Collectors.joining()),
                        ")");
    }

    /** Generate code for trampoline class with static methods. */
    public void emitTrampoline(Class<?> iface) {
        pa();
        if (javadoc) {
            pa("/**");
            pa(" * Trampoline class that contains methods of ", iface, " as static methods.");
            pa(" */");
        }
        pa("class ", iface.getSimpleName() + "_Static {");

        pa("private static class Lazy {");
        pa("static final ", SymbolLookup.class, " LOOKUP = ", lookup, ";");
        pa(
                "static final ",
                iface.getSimpleName(),
                " LIB = new ",
                iface.getSimpleName(),
                "_Impl(LOOKUP);");
        pa("}");

        // The order of methods returned by reflection is not stable and can vary.
        // Therefore sort methods by name for stable, reproducible output.
        List<Method> methods = Arrays.asList(iface.getMethods());
        Collections.sort(methods, Comparator.comparing(Method::getName));

        for (Method method : methods) {
            List<Parameter> params = Arrays.asList(method.getParameters());
            Class<?> retType = method.getReturnType();
            boolean ret = !retType.equals(Void.TYPE);

            pa();
            if (javadoc) {
                pa("/**");
                pa(" * Method stub that invokes native method {@code ", method.getName(), "}.");
                for (Parameter param : params) {
                    pa(" * @param ", param.getName(), " ", param.getType());
                }
                if (ret) {
                    pa(" * @return ", retType);
                }
                pa(" */");
            }
            pn("public static final ", retType, " ", method.getName() + "(");
            pj(params.stream().map(param -> javaType(param.getType()) + " " + param.getName()));
            pa(") {");
            if (ret) {
                pn("return ");
            }
            pn("Lazy.LIB.", method.getName(), "(");
            pj(params.stream().map(Parameter::getName));
            pa(");");
            pa("}");
        }

        pa("}");
    }
}
