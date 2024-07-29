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
package io.kojan.javadeptools.rpm;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A simple command-line tool for querying RPM packages.
 * 
 * @author Mikolaj Izdebski
 */
public class Main {

    private final String[] args;
    private String currentArg;
    private Iterator<String> argsIterator;

    private Main(String[] args) {
        this.args = args;
    }

    private Path root;
    private Path pack;
    private Path file;
    private String actionName;
    private Consumer<RpmInfo> action = this::nvrAction;

    private Path parsePathArg() {
        if (!argsIterator.hasNext()) {
            throw new IllegalArgumentException("Option " + currentArg + " requires an argument");
        }
        return Paths.get(argsIterator.next());
    }

    private void setAction(Consumer<RpmInfo> action) {
        if (actionName != null) {
            throw new IllegalArgumentException("Action " + currentArg + " conflict with previously-set " + actionName);
        }
        actionName = currentArg;
        this.action = action;
    }

    private void setDepsAction(Function<RpmInfo, List<RpmDependency>> depsGetter) {
        setAction(rpm -> printDeps(depsGetter.apply(rpm)));
    }

    private void parseArgs(String[] args) {
        for (argsIterator = Arrays.asList(args).iterator(); argsIterator.hasNext();) {
            currentArg = argsIterator.next();
            switch (currentArg) {
            case "-r":
                root = parsePathArg();
                break;
            case "-p":
                pack = parsePathArg();
                break;
            case "-f":
                file = parsePathArg();
                break;
            case "-i":
                setAction(this::infoAction);
                break;
            case "-l":
                setAction(this::filesAction);
                break;
            case "--provides":
                setDepsAction(RpmInfo::getProvides);
                break;
            case "--requires":
                setDepsAction(RpmInfo::getRequires);
                break;
            case "--conflicts":
                setDepsAction(RpmInfo::getConflicts);
                break;
            case "--obsoletes":
                setDepsAction(RpmInfo::getObsoletes);
                break;
            case "--recommends":
                setDepsAction(RpmInfo::getRecommends);
                break;
            case "--suggests":
                setDepsAction(RpmInfo::getSuggests);
                break;
            case "--supplements":
                setDepsAction(RpmInfo::getSupplements);
                break;
            case "--enhances":
                setDepsAction(RpmInfo::getEnhances);
                break;
            case "--orderWithRequires":
                setDepsAction(RpmInfo::getOrderWithRequires);
                break;
            default:
                throw new IllegalArgumentException("Unknown option: " + currentArg);
            }
        }
    }

    private void nvrAction(RpmInfo rpm) {
        System.out.println(rpm.toString());
    }

    private String infoOptional(Optional<?> opt) {
        if (!opt.isPresent()) {
            return "(absent)";
        }
        return opt.get().toString();

    }

    private String infoList(List<?> list) {
        if (list.isEmpty()) {
            return "(empty list)";
        }
        return list.stream().map(Object::toString).collect(Collectors.joining(" "));
    }

    private void infoAction(RpmInfo rpm) {
        System.out.println("Name               : " + rpm.getName());
        System.out.println("Epoch              : " + infoOptional(rpm.getEpoch()));
        System.out.println("Version            : " + rpm.getVersion());
        System.out.println("Release            : " + rpm.getRelease());
        System.out.println("Arch               : " + rpm.getArch());
        System.out.println("License            : " + rpm.getLicense());
        System.out.println("Is source package  : " + rpm.isSourcePackage());
        System.out.println("Source RPM         : " + rpm.getSourceRPM());
        System.out.println("Source name        : " + rpm.getSourceName());
        System.out.println("Exclusive arch     : " + infoList(rpm.getExclusiveArch()));
        System.out.println("Build archs        : " + infoList(rpm.getBuildArchs()));
        System.out.println("Archive format     : " + rpm.getArchiveFormat());
        System.out.println("Compression method : " + rpm.getCompressionMethod());
    }

    private void printDeps(List<RpmDependency> deps) {
        for (RpmDependency dep : deps) {
            System.out.println(dep.toString());
        }
    }

    private void filesAction(RpmInfo rpm) {
        for (RpmFile file : rpm.getFiles()) {
            System.out.println(file.toString());
        }
    }

    private int run() {
        try {
            parseArgs(args);
            List<RpmInfo> rpms = new ArrayList<>();
            if (pack != null) {
                rpms.add(new RpmPackage(pack).getInfo());
            }
            if (file != null) {
                rpms.addAll(RpmQuery.byFile(file, root));
            }
            for (RpmInfo rpm : rpms) {
                action.accept(rpm);
            }
            return 0;
        } catch (IOException e) {
            System.err.println("I/O error when reading RPM package: " + e.getMessage());
            return 1;
        }
    }

    /**
     * Invokes the CLI tool.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Main main = new Main(args);
        System.exit(main.run());
    }

}
