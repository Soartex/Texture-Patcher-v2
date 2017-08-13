package types;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Class that represents a local GIT repository
 * These repositories have the modded textures in them
 */
public class Repository {

    // Human readable name of the repository
    public String repo_name;

    // Remote git path to the repository (github or bitbucket)
    public String remote_path;

    // Local path of the repo on the user's disk
    public Path local_dir;

    // Size of the repository in bytes
    public long size_on_disk;

    // List of mods that this repository contains
    public ArrayList<Mod> mods;

    /**
     * Default constructor
     */
    public Repository() {
        mods = new ArrayList<>();
    }


}
