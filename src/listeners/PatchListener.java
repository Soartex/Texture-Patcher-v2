package listeners;

import com.sun.javafx.scene.shape.PathUtils;
import views.TPPatchDialog;
import patcher.Patcher;
import utils.PatchUtils;

import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class PatchListener implements ActionListener, Runnable {

    private Patcher patcher;
    private TPPatchDialog progressdialog;

    private long time1;
    private long time2;

    private File TEMP_A;

    public PatchListener(Patcher patcher) {
        // Receive the texture patcher instance for the listener.
        this.patcher = patcher;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // Start the patching process.
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            // Resolve the temporary folders.
            time1 = System.currentTimeMillis();

            TEMP_A = new File(PatchUtils.getTMP() + File.separator + ".Texture_Patcher_Temp_A_" + time1);

            // Initialize the progress dialog.
            progressdialog = new TPPatchDialog(patcher);

            progressdialog.setString("Extracting texture pack file (--/--)");
            progressdialog.setProgressValue(0);

            progressdialog.open();

            // Extract the texture pack.
            extractTexturepack();

            progressdialog.setString("Compiling mods list...");
            progressdialog.setProgressValue(25);

            // Compile the mods list.
            compileModsList();

            progressdialog.setString("Copying mods (--/--)");
            progressdialog.setProgressValue(50);

            // Extract the mod.
            extractMods();

            progressdialog.setString("Compressing texture pack file (--/--)");
            progressdialog.setProgressValue(75);

            // Compile the texture pack.
            compileTexturepack();

            // Resolve the temporary folders.
            time2 = System.currentTimeMillis();
            System.out.println("Done patching in "+(time2-time1)/1000+" seconds");

            // Return to normal.
            progressdialog.setString("Done! ("+(time2-time1)/1000+" sec)");
            progressdialog.setProgressValue(100);

            PatchUtils.delete(TEMP_A);

            PatchUtils.delay(1000);
            progressdialog.close();
            patcher.frame.requestFocus();

        } catch (final Exception e) {
            e.printStackTrace();
            progressdialog.close();
        }
    }

    private void extractTexturepack() throws IOException {
        // Make sure the temporary folder is ready.
        PatchUtils.delete(TEMP_A);

        TEMP_A.delete();
        TEMP_A.deleteOnExit();
        TEMP_A.mkdirs();

        // Calculate progress percents.
        int progressamount = 0;
        int progresscount = 0;

        try {
            final ZipFile zipfile = new ZipFile(new File(patcher.path.getText()));
            if (zipfile.size() == 0) return;
            progressamount = zipfile.size();
            zipfile.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        int count = 0;

        // Extract the texture pack.
        final ZipInputStream zipin = new ZipInputStream(new FileInputStream(new File(patcher.path.getText())));
        ZipEntry zipEntry;
        final byte[] buffer = new byte[1024 * 1024];

        System.out.println("Extracting: " + patcher.path.getText());

        while ((zipEntry = zipin.getNextEntry()) != null) {
            final String fileName = zipEntry.getName();
            final File destinationFile = new File(TEMP_A.getAbsolutePath() + File.separator + fileName);

            progressdialog.setString("Extracting texture pack file (" + ++count + "/" + progressamount + ")");

            if (zipEntry.isDirectory()) {
                new File(destinationFile.getParent()).mkdirs();
            } else {
                new File(destinationFile.getParent()).mkdirs();
                final FileOutputStream out = new FileOutputStream(destinationFile);
                int length;
                while ((length = zipin.read(buffer, 0, buffer.length)) > -1) {
                    out.write(buffer, 0, length);
                }
                out.close();
            }

            if (++progresscount >= progressamount / 25) {
                progressdialog.setProgressValue(progressdialog.getProgressValue() + 1);
                progresscount = 0;
            }
        }
        zipin.close();
    }

    private void compileModsList() throws IOException {

        // Get the modslist file ready.
        final File modslist = new File(TEMP_A, "modslist.csv");
        final ArrayList<String> mods = new ArrayList<String>();

        if (modslist.exists()) {
            // Merge the modslist if it already exists.
            final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(modslist)));
            String readline;
            reading:
            while ((readline = in.readLine()) != null) {
                rowing:
                for (final Object[] row : patcher.tableData) {
                    if ((Boolean) row[0] == false) continue rowing;
                    if (readline.split(",")[0].equals(row[1])) {
                        continue reading;
                    }
                }
                mods.add(readline);
            }
            in.close();
        }

        // If it does not exist, add a header to the top of it
        if (!modslist.exists())
            mods.add("MOD_ID,MOD_VERSION,MC_VERSION,LAST_UPDATED,SIZE_BYTES");

        // Create the new modslist.
        for(int i=0; i<patcher.tableData.length; i++) {
            if (!(Boolean)patcher.tableData[i][0]) continue;
            mods.add(patcher.repos.get(patcher.activeRepo).mods.get(i).mod_id+","
                    +patcher.repos.get(patcher.activeRepo).mods.get(i).mod_version+","
                    +patcher.repos.get(patcher.activeRepo).mods.get(i).mc_version+","
                    +"DATE GOES HERE"+","
                    +patcher.repos.get(patcher.activeRepo).mods.get(i).size_on_disk+",");
        }

        final PrintWriter out = new PrintWriter(new FileWriter(modslist));
        for (final String mod : mods) {
            out.println(mod);
        }
        out.close();
    }

    private void extractMods() {
        // Extract the mods.
        final ArrayList<Path> directories = new ArrayList<Path>();

        // Loop through and append enabled mods
        // Create the new modslist.
        for(int i=0; i<patcher.tableData.length; i++) {
            if (!(Boolean)patcher.tableData[i][0]) continue;
            directories.add(patcher.repos.get(patcher.activeRepo).mods.get(i).local_dir);
        }
        // Files we should skip
        List<String> skip = new ArrayList<>();
        skip.add("mod.json");
        skip.add("~untextured.txt");
        skip.add("untextured.txt");

        // Loop through and copy
        int count = 0;
        for (final Path dir : directories) {
            System.out.println("Copying mod: " + dir.getFileName());
            progressdialog.setString("Copying mods (" + ++count + "/" + directories.size() + ")");
            try {
                PatchUtils.copy(dir,TEMP_A.toPath(),skip);
            } catch (final IOException e) {
                System.err.println("Unable to extract " + dir.toString() + " .");
                e.printStackTrace();
            }
        }
    }

    private void compileTexturepack() throws IOException {

        // Compile the texture pack.
        final FileOutputStream out = new FileOutputStream(new File(patcher.path.getText()));
        final ZipOutputStream zipout = new ZipOutputStream(out);

        System.out.println("Compiling texture pack: " + new File(patcher.path.getText()).getAbsolutePath());

        final ArrayList<File> files = new ArrayList<File>();
        PatchUtils.getFiles(TEMP_A, files);

        final byte[] buffer = new byte[1024 * 1024];
        int count = 0;
        final int progressamount = files.size() / 25;
        int progresscount = 0;

        for (final File file : files) {
            final String temp = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(TEMP_A.getName()), file.getAbsolutePath().length());
            final String zipentrypath = temp.substring(temp.indexOf(File.separator) + 1, temp.length());
            progressdialog.setString("Compressing texture pack file (" + ++count + "/" + files.size() + ")");

            final ZipEntry zipentry = new ZipEntry(zipentrypath.replace("\\", "/"));
            zipout.putNextEntry(zipentry);

            final FileInputStream in = new FileInputStream(file);
            int length;
            while ((length = in.read(buffer, 0, buffer.length)) > -1) {
                zipout.write(buffer, 0, length);
            }
            in.close();
            zipout.closeEntry();

            if (++progresscount >= progressamount) {
                progressdialog.setProgressValue(progressdialog.getProgressValue() + 1);
                progresscount = 0;
            }
        }

        zipout.close();
        out.close();
    }
}
