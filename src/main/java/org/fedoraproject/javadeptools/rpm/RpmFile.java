package org.fedoraproject.javadeptools.rpm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.fedoraproject.javadeptools.fma.Mman;
import org.fedoraproject.javadeptools.fma.Rpmio;
import org.fedoraproject.javadeptools.fma.Unistd;

public class RpmFile {
    private URL url;
    private RpmInfo info;
    private byte[] content;

    private RpmFile(URL url, RpmInfo info, byte[] content) throws IOException {
        this.url = url;
        this.info = info;
        this.content = content;
    }

    public static RpmFile from(Path path) throws IOException {
        var info = new RpmInfo(path);
        byte[] content;
        try (var is = Files.newInputStream(path)) {
            content = is.readAllBytes();
        }
        return new RpmFile(path.toUri().toURL(), info, content);
    }

    public static RpmFile from(URL url) throws IOException {
        if (url.getProtocol().equals("file")) {
            try {
                return from(Path.of(url.toURI()));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        byte[] content;
        try (var is = url.openStream()) {
            content = is.readAllBytes();
        }
        var intFD = Mman.memfd_create(url.getPath(), 0);
        var fd = Rpmio.fdDup(intFD);
        Unistd.write(intFD, content);
        Unistd.lseek(intFD, 0, Unistd.SEEK_SET);

        RpmInfo info;
        try {
            info = new RpmInfo(url, fd);
        } finally {
            Unistd.close(intFD);
        }

        return new RpmFile(url, info, content);
    }

    public URL getURL() {
        return url;
    }

    public RpmInfo getInfo() {
        return info;
    }

    public InputStream getContent() {
        return new ByteArrayInputStream(content);
    }

    @Override
    public String toString() {
        return getInfo().toString();
    }
}
