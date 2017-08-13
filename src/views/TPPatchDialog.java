package views;

import patcher.Patcher;

import javax.swing.*;
import java.awt.*;

public class TPPatchDialog {
    private final Patcher patcher;

    private static final long serialVersionUID = 1L;

    private final JFrame frame;
    private final JProgressBar progress;
    private final JLabel status;

    public TPPatchDialog(Patcher patcher) {
        this.patcher = patcher;

        frame = new JFrame("Patching...");
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setIconImage(patcher.frame.getIconImage());

        final Insets insets = new Insets(2, 2, 1, 2);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = insets;

        progress = new JProgressBar();
        progress.setStringPainted(true);

        frame.add(progress, gbc);

        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = insets;

        status = new JLabel("", SwingConstants.CENTER);
        frame.add(status, gbc);

        frame.setSize(250, 75);
        frame.setResizable(false);
        frame.setLocationRelativeTo(patcher.frame);
    }

    public void open() {
        patcher.frame.setEnabled(false);
        frame.setVisible(true);
    }

    public void close() {
        frame.dispose();
        patcher.frame.setEnabled(true);
        patcher.frame.requestFocus();
    }

    public void setProgressValue(final int value) {
        progress.setValue(value);
    }

    public int getProgressValue() {
        return progress.getValue();
    }

    public void setString(final String value) {
        status.setText(value);
    }
}