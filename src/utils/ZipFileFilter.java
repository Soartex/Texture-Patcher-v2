package utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ZipFileFilter extends FileFilter {
    @Override
    public boolean accept(final File f) {
        return f.isDirectory() || f.getName().endsWith(".zip");
    }

    @Override
    public String getDescription() {
        return "Texture Pack Archive (*.zip)";
    }
}