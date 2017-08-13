package utils;

public class SizeUtils {

    /**
     * Will take in some bytes and return a string of it in human readable form
     * If the second argument is true then it will use SI units
     * https://stackoverflow.com/a/3758880/7718197
     */
    public static String humanBytes(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
