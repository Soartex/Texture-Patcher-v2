package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PatchUtils {

    // Shared methods.
    public static String getTMP() {
        // Resolve the system's temporary directory.
        final String OS = System.getProperty("os.name").toUpperCase();

        if (OS.contains("WIN")) return System.getenv("TMP");

        else if (OS.contains("MAC") || OS.contains("DARWIN"))
            return System.getProperty("user.home") + "/Library/Caches/";
        else if (OS.contains("NUX")) return System.getProperty("user.home");

        return System.getProperty("user.dir");
    }

    public static void delay(final long time) {
        // Create a thread delay for the given time.
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFiles(final File f, final ArrayList<File> files) {
        // Return all the files in a directory and its subdirectories.
        if (f.isFile()) return;

        final File[] afiles = f.getAbsoluteFile().listFiles();

        if (afiles == null) return;

        for (final File file : afiles) {
            if (file.isDirectory()) getFiles(file, files);
            else files.add(file.getAbsoluteFile());
        }
    }

    public static void delete(final File f) {
        // Delete a folder and all of its subdirectories.
        f.delete();

        if (f.isFile()) return;

        final File[] files = f.getAbsoluteFile().listFiles();

        if (files == null) return;

        for (final File file : files) {
            delete(file);
            f.delete();
        }
    }


    public static void copy(Path sourcePath, Path targetPath, List<String> toskip) throws IOException {
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                // Place we are going to copy to
                Path output = targetPath.resolve(sourcePath.relativize(file));
                // If it already exists, delete it so we can overwrite it
                if(output.toFile().exists())
                    output.toFile().delete();
                // Copy if it is one that we shouldn't skip
                if(!toskip.contains(file.getFileName().toString()))
                    Files.copy(file, output);
                return FileVisitResult.CONTINUE;
            }
        });
    }



}
