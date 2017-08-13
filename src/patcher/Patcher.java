package patcher;


import listeners.BrowseListener;
import listeners.PatchListener;
import listeners.TableListener;
import types.Repository;
import utils.SizeUtils;
import views.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * This is the master class that handles the entire system
 * From startup it will load everything from disk
 * It also handles the different view swapping and is the "owner" of the data
 */
public class Patcher implements Runnable {

    // Program variables.
    public final static float VERSION = 3.0F;
    public final Preferences prefsnode = Preferences.userNodeForPackage(getClass());

    // Arraylist of all repositories
    public int activeRepo;
    public ArrayList<Repository> repos;

    // Table data (created from the repos objects)
    public String[] columns = {"\u2713", "Mod Name", "Mod Version", "MC Version", "File Size", "Last Updated"};
    public Object[][] tableData;

    // Swing objects (MAIN FRAME)
    public JFrame frame;
    public JTextField path;
    public JButton patch;
    public JTable table;

    // Swing objects (LOADING/STARTUP)
    public JFrame loadingFrame;
    public JLabel loadingText1;
    public JLabel loadingText2;



    /**
     * Default constructor for the patcher class
     * Will try loading information from disk etc
     */
    public Patcher() {
        // TODO: Remove this temp code here
        repos = new ArrayList<>();
        Repository temp = new Repository();
        //temp.remote_path = "https://github.com/Soartex-Modded/Modded-1.3.x";
        //temp.repo_name = "Soartex Modded 1.3.x";
        //temp.local_dir = FileSystems.getDefault().getPath("./repositories/repo_103");
        //repos.add(temp);
        //temp = new Repository();
        //temp.remote_path = "https://github.com/Soartex-Modded/Modded-1.4.x";
        //temp.repo_name = "Soartex Modded 1.4.x";
        //temp.local_dir = FileSystems.getDefault().getPath("./repositories/repo_104");
        //repos.add(temp);
        //temp = new Repository();
        //temp.remote_path = "https://github.com/Soartex-Modded/Modded-1.5.x";
        //temp.repo_name = "Soartex Modded 1.5.x";
        //temp.local_dir = FileSystems.getDefault().getPath("./repositories/repo_105");
        //repos.add(temp);
        //temp = new Repository();
        //temp.remote_path = "https://github.com/Soartex-Modded/Modded-1.6.x";
        //temp.repo_name = "Soartex Modded 1.6.x";
        //temp.local_dir = FileSystems.getDefault().getPath("./repositories/repo_106");
        //repos.add(temp);
        temp = new Repository();
        temp.remote_path = "https://github.com/Soartex-Modded/Modded-1.7.x";
        temp.repo_name = "Soartex Modded 1.7.x";
        temp.local_dir = FileSystems.getDefault().getPath("./repositories/repo_107");
        repos.add(temp);
        temp = new Repository();
        temp.remote_path = "https://github.com/Soartex-Modded/Modded-1.8.x";
        temp.repo_name = "Soartex Modded 1.8.x";
        temp.local_dir = FileSystems.getDefault().getPath("./repositories/repo_108");
        repos.add(temp);
        temp = new Repository();
        temp.remote_path = "https://github.com/Soartex-Modded/Modded-1.10.x";
        temp.repo_name = "Soartex Modded 1.10.x";
        temp.local_dir = FileSystems.getDefault().getPath("./repositories/repo_110");
        repos.add(temp);
        temp = new Repository();
        temp.remote_path = "https://github.com/Soartex-Modded/Modded-1.11.x";
        temp.repo_name = "Soartex Modded 1.11.x";
        temp.local_dir = FileSystems.getDefault().getPath("./repositories/repo_111");
        repos.add(temp);
        temp = new Repository();
        temp.remote_path = "https://github.com/Soartex-Modded/Modded-1.12.x";
        temp.repo_name = "Soartex Modded 1.12.x";
        temp.local_dir = FileSystems.getDefault().getPath("./repositories/repo_112");
        repos.add(temp);


        // Display the loading screen
        init_loadingframe();
        // Handle the startup checks
        startup_checks();
        // Launch into choice of repositories
        choose_repository();
    }

    /**
     * This will show the loading frame for startup
     * Should launch the system, and allow the user to see the loading progress
     */
    private void init_loadingframe() {
        // Initialize the loading dialog.
        loadingFrame = new JFrame("Texture Patcher v"+VERSION);
        loadingFrame.setLayout(new GridBagLayout());
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

        // Initialize the progress bar.
        final JProgressBar progress = new JProgressBar(SwingConstants.HORIZONTAL);
        progress.setIndeterminate(true);
        loadingFrame.add(progress, gbc);
        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = insets;

        // Initialize the static message label.
        loadingText1 = new JLabel("Loading System...", SwingConstants.CENTER);
        loadingFrame.add(loadingText1, gbc);
        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = insets;

        // Initialize the loading mod number label.
        loadingText2 = new JLabel("--", SwingConstants.CENTER);
        loadingFrame.add(loadingText2, gbc);
        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = insets;

        // Finally set our close action and show it!
        loadingFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //loadingFrame.setIconImage(frame.getIconImage());
        loadingFrame.setSize(250,100);
        loadingFrame.setResizable(false);
        loadingFrame.setLocationRelativeTo(null);
        loadingFrame.setVisible(true);
    }


    /**
     * This will load information from disk
     * Will scan current repositories
     * It will also try to get the latest from the git repositories
     */
    private void startup_checks() {
        // Loop through each repository to see if we need to clone any of them
        for(Repository repo : repos) {
            loadingText1.setText(repo.repo_name+" Checking");
            RepoLoader.clone_if_needed(this, repo);
        }
        // Now that we have clones all needed
        // Loop through each repository to load details from disk
        for(Repository repo : repos) {
            loadingText1.setText(repo.repo_name+" Loading");
            RepoLoader.load_repo_data(this, repo);
            System.out.println(repo.repo_name+" = "+SizeUtils.humanBytes(repo.size_on_disk,true)+" ("+ repo.mods.size()+" mods)");
        }
    }

    /**
     * This will launch the master gui of the patcher
     * We assume everything is already loaded, so just launch it!
     */
    @Override
    public void run() {
        // Set the native look ad feel for before the skin in the config is loaded.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e1) {
            e1.printStackTrace();
        }

        // Make the main window
        try {
            frame = new JFrame("Texture Patcher v"+VERSION);
            frame.setLayout(new GridBagLayout());
            frame.setSize(prefsnode.getInt("width", 700), prefsnode.getInt("height", 500));
            if (prefsnode.getInt("x", -1000) != -1000 && prefsnode.getInt("y", -1000) != -1000) {
                frame.setLocation(prefsnode.getInt("x", 50), prefsnode.getInt("y", 50));
            } else {
                frame.setLocationRelativeTo(null);
            }
            frame.setExtendedState(prefsnode.getInt("max", Frame.NORMAL));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            //frame.addWindowListener(new Listeners.ExitListener(this));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // Make our table data
        tableData = new Object[repos.get(activeRepo).mods.size()][];
        for(int i=0; i<repos.get(activeRepo).mods.size(); i++) {
            // {"\u2713", "Mod Name", "Mod Version", "MC Version", "File Size", "Date Modified"};
            Object[] row = new Object[6];
            row[0] = false;
            row[1] = repos.get(activeRepo).mods.get(i).mod_name;
            row[2] = repos.get(activeRepo).mods.get(i).mod_version;
            row[3] = repos.get(activeRepo).mods.get(i).mc_version;
            row[4] = SizeUtils.humanBytes(repos.get(activeRepo).mods.get(i).size_on_disk,true);
            row[5] = repos.get(activeRepo).mods.get(i).last_updated_string;
            tableData[i] = row;
        }

        // Initialize the path text field.
        //==========================================================================
        final Insets insets = new Insets(1, 3, 1, 3);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 4;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = insets;
        path = new JTextField();
        path.setEditable(false);
        frame.add(path, gbc);

        // Initialize the browse button.
        //==========================================================================
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = insets;
        final JButton browse = new JButton("Browse");
        browse.addActionListener(new BrowseListener(this));
        frame.add(browse, gbc);

        // Initialize the table.
        //==========================================================================
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weightx = 4;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = insets;
        table = new JTable(new TPTableModel(columns,tableData));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setMaxWidth(25);
        table.getColumnModel().getColumn(2).setMaxWidth(150);
        table.getColumnModel().getColumn(3).setMaxWidth(100);
        table.getColumnModel().getColumn(4).setMaxWidth(100);
        table.addMouseListener(new TableListener(this));
        frame.add(new JScrollPane(table), gbc);

        // Initialize the patch button.
        //==========================================================================
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = insets;
        patch = new JButton("Patch");
        patch.addActionListener(new PatchListener(this));
        patch.setEnabled(false);
        frame.add(patch, gbc);

        // Open the window.
        //==========================================================================
        frame.setVisible(true);
    }


    /**
     * This will create a dropdown that will ask the user what repo they want
     * This will then set the repo ID for the system
     */
    private void choose_repository() {
        // Disable the loading screen
        loadingFrame.setVisible(false);
        // Display Option Pane for User
        String[] possibleValues = new String[repos.size()];
        for (int i = 0; i < repos.size(); i++) {
            possibleValues[i] = repos.get(i).repo_name;
        }
        String selectedValue = (String) JOptionPane.showInputDialog(null,
                "Please Select a Repository", "Select A Repository", JOptionPane.PLAIN_MESSAGE,
                null, possibleValues, possibleValues[possibleValues.length - 1]);

        // If cancel or exit is pressed the return value is null. Therefor exit the program
        if (selectedValue == null) {
            System.exit(0);
        }

        // Find the selected branch
        activeRepo = repos.size() - 1;
        for (int i = 0; i < repos.size(); i++) {
            if (repos.get(i).repo_name.equalsIgnoreCase(selectedValue)) {
                activeRepo = i;
            }
        }
        System.out.println("Active Repo = "+activeRepo);
    }


}
