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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A simple command-line tool for querying RPM packages.
 * 
 * @author Mikolaj Izdebski
 */
public class Main {

    private Main() {}

    private Path root;
    private Path pack;
    private Path file;
    private boolean info;

    private Path parsePathArg(String[] args, int i) {
        if (i == args.length) {
            throw new IllegalArgumentException("Option " + args[i - 1] + " requires an argument");
        }
        return Paths.get(args[i]);
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
            case "-r":
                root = parsePathArg(args, ++i);
                break;
            case "-p":
                pack = parsePathArg(args, ++i);
                break;
            case "-f":
                file = parsePathArg(args, ++i);
                break;
            case "-i":
                info = true;
                break;
            default:
                throw new IllegalArgumentException("Unknown option: " + args[i]);
            }
        }
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

    private void info(RpmInfo rpm) {
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

    private int run(String[] args) {
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
                if (info) {
                    info(rpm);
                } else {
                    System.out.println(rpm);
                }
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
        Main main = new Main();
        System.exit(main.run(args));
    }

}
