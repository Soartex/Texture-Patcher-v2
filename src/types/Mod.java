package types;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class that represents a single mod
 * This should have all information about that mod
 * Should also contain version details about this mod
 */
public class Mod {

    //=====================================
    // Information from mod.json files
    //=====================================

    // The unique id of this mod
    public String mod_id;

    // Human readable name of this mode
    public String mod_name;

    // Directory the mod is in in the repository
    public String mod_dir;

    // What mod version the textures are for
    public String mod_version;

    // What minecraft version the textures are for
    //public MCVersion mc_version;
    public String mc_version;

    // Array of mod authors (got to give them credit :D)
    public ArrayList<String> mod_authors;

    // String url to the mod website
    public String url_website;

    // Description of what this mod is
    public String description;

    //=====================================
    // Information about local mod state
    //=====================================

    // Path to mod on user's harddisk
    public Path local_dir;

    // Size of the mod in bytes
    public long size_on_disk;

    // Date last updated
    public Date last_updated;

    // Clean date
    public String last_updated_string;

    /**
     * Default constructor
     */
    public Mod() {
        mod_authors = new ArrayList<>();
    }

}
