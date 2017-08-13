import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import patcher.Patcher;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        // Startup
        System.out.println("Starting up....");

        // Start the patcher in its own thread.
        new Thread(new Patcher()).start();

        // Done, exit the program
        System.out.println("Done, exiting...");

    }
}
