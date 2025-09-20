package Util;

public class CommunicationString {
    //HOSE COMMUNICATION
    public static String CONNECTED="CONNECTED";
    public static String NOT_CONNECTED="NOT CONNECTED";

    //Credit card/Bank communication
    public static String APPROVED="APPROVED";
    public static String DENIED="BROKE!";

    //GasStationCommunication
    //I don't quite know if this is the way we want to do it
    //The reason I thinking of it this way, is because I think it would
    //be a lot harder to parse if we used, say a string builder, but please feel free
    //to change this if you disagree, because it does seem a little silly
    public static String OUT_OF_GAS1="No gas 1";
    public static String OUT_OF_GAS2="No gas 2";
    public static String OUT_OF_GAS3="No gas 3";
    public static String OUT_OF_GAS4="No gas 4";
    public static String OUT_OF_GAS5="No gas 5";
    //So the thinking here is maybe it should be a string builder because how
    //else are we going to handle this in a string, so you just give it the whole price
    //list.
    // Example: Send(UPDATE_PRICES +"4.0,"+ "5.0",6.0")
    //I honestly don't know the best way of doing this
    public static String UPDATE_PRICES="";
    //This has the same issue as ^
    public static String VOLUME_PUMPED="Volume pumped is:";
    public static String TRANSACTION_COMPLETE="Transaction is complete";

    //To me, its not a big deal to do it like this, makes it easy to put in a switch statment,
    //and makes the code readably, but if you guys would rather this done with string builders,
    //or something more clever then let me know
    public static String GAS1_SELECTED="Customer wants gas 1";
    public static String GAS2_SELECTED="Customer wants gas 2";
    public static String GAS3_SELECTED="Customer wants gas 3";
    public static String GAS4_SELECTED="Customer wants gas 4";
    public static String GAS5_SELECTED="Customer wants gas 5";




}
