package types;

/**
 * Different minecraft versions
 * 1.7.x = MC_1_07 etc
 */
public enum MCVersion {
    UNKNOWN,
    MC_1_02,
    MC_1_03,
    MC_1_04,
    MC_1_05,
    MC_1_06,
    MC_1_07,
    MC_1_08,
    MC_1_09,
    MC_1_10,
    MC_1_11,
    MC_1_12;

    /**
     * This will take a string in and try to return what is the valid mc version
     */
    public static MCVersion parse(String str) {
        // Split the string
        String[] splits = str.split("\\.");
        // If we do not have at least two, then not valid number
        if(splits.length < 2)
            return UNKNOWN;
        // Match first and second letters
        if(splits[0].equals("1") && splits[1].equals("2"))
            return MC_1_02;
        if(splits[0].equals("1") && splits[1].equals("3"))
            return MC_1_03;
        if(splits[0].equals("1") && splits[1].equals("4"))
            return MC_1_04;
        if(splits[0].equals("1") && splits[1].equals("5"))
            return MC_1_05;
        if(splits[0].equals("1") && splits[1].equals("6"))
            return MC_1_06;
        if(splits[0].equals("1") && splits[1].equals("7"))
            return MC_1_07;
        if(splits[0].equals("1") && splits[1].equals("8"))
            return MC_1_08;
        if(splits[0].equals("1") && splits[1].equals("9"))
            return MC_1_09;
        if(splits[0].equals("1") && splits[1].equals("10"))
            return MC_1_10;
        if(splits[0].equals("1") && splits[1].equals("11"))
            return MC_1_11;
        if(splits[0].equals("1") && splits[1].equals("12"))
            return MC_1_12;
        // Else default
        return UNKNOWN;
    }

}
