package com.ead.authuser.helpers;

import static java.nio.file.Files.readString;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

public final class FilesUtils {
  public static String loadPayload(final String path) throws URISyntaxException, IOException {
    return readString(
        Paths.get(
            Objects.requireNonNull(FilesUtils.class.getClassLoader().getResource(path)).toURI()));
  }
}
