package patcher;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.FetchResult;
import org.json.JSONArray;
import org.json.JSONObject;
import types.Mod;
import types.Repository;
import utils.DirUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class handles all loading of the repositories from disk
 * Should process the mod.json and other files into the java format
 */
public class RepoLoader {

    /**
     * This will load a repository if needed
     * If the directory is already there, then it will not clone it
     * If the directory is there, it should update it
     */
    public static void clone_if_needed(Patcher patcher, Repository repo) {
        // If the path does not exist, then we should clone
        if(!Files.exists(repo.local_dir)) {
            patcher.loadingText2.setText("Cloning Repository");
            try {
                System.out.println("Downloading = "+repo.remote_path);
                Git.cloneRepository().setURI(repo.remote_path).setDirectory(repo.local_dir.toFile()).call();
            } catch (GitAPIException e) {
                System.err.println("Unable to clone repository");
                e.printStackTrace();
            }
        }
        // Else lets update it, reset the repo, then pull
        else {
            try {
                patcher.loadingText2.setText("Updating Repository");
                Git git = Git.open(repo.local_dir.toFile());
                git.reset().setMode(ResetCommand.ResetType.HARD).call();
                FetchResult result1 = git.fetch().setCheckFetchedObjects(true).call();
                PullResult result2 = git.pull().call();
                System.out.println(result2.getMergeResult().toString());
            } catch (GitAPIException | IOException e) {
                System.err.println("Unable to update repository...");
                System.err.println(repo.local_dir);
                e.printStackTrace();
            }
        }
    }

    /**
     * This will load all data needed into the repo object
     * This will find the repo size, along with loading all the mods
     */
    public static void load_repo_data(Patcher patcher, Repository repo) {

        // First lets see if the local path is created
        // If it is then load the information about them
        if(!Files.exists(repo.local_dir)) {
            System.err.println("Unable to open local repository, cannot load information....");
            System.err.println(repo.local_dir);
            return;
        }
        // Load size
        repo.size_on_disk = DirUtils.size(repo.local_dir);
        // Get list of directories, return if empty folder
        File[] files = repo.local_dir.toFile().listFiles();
        if(files == null) {
            System.err.println("Empty repository...");
            System.err.println(repo.local_dir);
            return;
        }
        // Loop through top directory folders
        for(File file: files) {
            // If it is not a directory skip it
            if(!file.isDirectory())
                continue;
            // Else lets check if there is a mod.json file
            File modjson = new File(file,"mod.json");
            // Skip if we cannot find the mod.json
            if (!modjson.exists())
                continue;
            // Load the json file
            JSONObject json;
            try {
                String data = new String(Files.readAllBytes(modjson.toPath()));
                json = new JSONObject(data);
            } catch (IOException e) {
                System.err.println("Unable to read mod.json");
                System.err.println(modjson);
                continue;
            }
            // Update display
            patcher.loadingText2.setText("Loading "+file.getName());
            // Load the json information into new mod
            Mod mod = new Mod();
            try {
                // Open the git repo
                Git git = Git.open(repo.local_dir.toFile());
                // Get the git history for this directory
                Iterable<RevCommit> commits = git.log().addPath(file.getName()).call();
                // Set our last updated
                mod.last_updated = new Date(commits.iterator().next().getCommitTime() * 1000L);
                mod.last_updated_string = new SimpleDateFormat("MMMMM dd, yyyy HH:mm:ss z").format(mod.last_updated);
            } catch (Exception e) {
                System.err.println("Unable to update repository...");
                System.err.println(repo.local_dir);
                e.printStackTrace();
                continue;
            }
            // Mod ID
            if(json.has("mod_id")) mod.mod_id = json.getString("mod_id");
            else continue;
            // Mod name
            if(json.has("mod_name")) mod.mod_name = json.getString("mod_name");
            else mod.mod_name = mod.mod_id;
            // Mod directory, and file size
            mod.mod_dir = file.getName();
            mod.local_dir = file.getAbsoluteFile().toPath();
            mod.size_on_disk = DirUtils.size(mod.local_dir);
            // Mod version
            if(json.has("mod_version")) mod.mod_version = json.getString("mod_version");
            else mod.mod_version = "0.0.0";
            // MC Version
            if(json.has("mc_version")) mod.mc_version = json.getString("mc_version");
            else mod.mc_version = "0.0.0";
            // Mod Authors
            if(json.has("mod_authors")) {
                Object obj = json.get("mod_authors");
                if(obj instanceof JSONArray) {
                    for (int i=0; i<((JSONArray)obj).length(); i++) {
                        mod.mod_authors.add((String)((JSONArray)obj).get(i));
                    }
                }
            }
            // Description
            if(json.has("url_website")) mod.url_website = json.getString("url_website");
            else mod.url_website = "";
            // Description
            if(json.has("description")) mod.description = json.getString("description");
            else mod.description = "No description available.";
            // Append the new mod!!!
            repo.mods.add(mod);
        }
    }

}
