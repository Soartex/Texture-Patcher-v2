package listeners;

import patcher.Patcher;
import utils.ZipFileFilter;
import views.TPTableModel;
import views.TPTableRender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class BrowseListener implements ActionListener {

    private Patcher patcher;

    public BrowseListener(Patcher patcher) {
        this.patcher = patcher;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        try {
            // Initialize the file chooser.
            final JFileChooser fileChooser = new JFileChooser();

            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileFilter(new ZipFileFilter());

            // Resolve the stored directory and files.
            final File lastDir = new File(patcher.prefsnode.get("lastDir", System.getProperty("user.dir")));

            if (lastDir.exists()) {
                fileChooser.setCurrentDirectory(lastDir);
            } else {
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

                patcher.path.setText("");
                patcher.patch.setEnabled(false);

                patcher.prefsnode.remove("path");
                patcher.prefsnode.remove("lastDir");
            }

            if (fileChooser.showOpenDialog(patcher.frame) != JFileChooser.APPROVE_OPTION) return;

            // Save the selected file.
            final File file = fileChooser.getSelectedFile();

            patcher.path.setText(file.getAbsolutePath());
            patcher.patch.setEnabled(true);

            patcher.prefsnode.put("path", file.getAbsolutePath());
            patcher.prefsnode.put("lastDir", file.getParent());

            // Clear cell highlighting
            try {
                TPTableRender tableRender = new TPTableRender(new ArrayList<Integer>(), new ArrayList<Integer>(), Color.red, Color.yellow);
                patcher.table.getColumnModel().getColumn(1).setCellRenderer(tableRender);
                patcher.table.updateUI();
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        } catch (final Exception e1) {
            e1.printStackTrace();
        }

    }

}