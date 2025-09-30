package Util;

public enum GasTypeEnum {
    NO_SELECTION("NO SELECTION YET",-1),
    GAS_TYPE_1(CommunicationString.GAS1_SELECTED,1),
    GAS_TYPE_2(CommunicationString.GAS2_SELECTED,2),
    GAS_TYPE_3(CommunicationString.GAS3_SELECTED,3),
    GAS_TYPE_4(CommunicationString.GAS4_SELECTED,4),
    GAS_TYPE_5(CommunicationString.GAS5_SELECTED,5);

    public final String label;
    public final int intVersion;

    private GasTypeEnum(String label,int intVersion) {
        this.label = label;
        this.intVersion=intVersion;
    }
    public static GasTypeEnum getEnum(int version){
        switch(version){
            case (1)-> {
                return GAS_TYPE_1;
            }
            case (2)-> {
                return GAS_TYPE_2;
            }
            case (3)-> {
                return GAS_TYPE_3;
            }
            case (4)-> {
                return GAS_TYPE_4;
            }
            case (5)-> {
                return GAS_TYPE_5;
            }
            default -> {
                return NO_SELECTION;
            }
        }
    }
}
