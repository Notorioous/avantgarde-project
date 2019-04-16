package am.avantgarde.avantgardecommon.model;

public enum Transmission {
    MANUAL("Manual"),
    AUTOMATIC("Automatic"),
    SEMI_AUTOMATIC("Semi-Automatic"),
    CONTINUOUSLY("Continuously");

    public final String label;

    Transmission(String label){
        this.label = label;
    }
}
