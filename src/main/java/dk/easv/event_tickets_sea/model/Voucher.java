package dk.easv.event_tickets_sea.model;

public class Voucher {
    private int voucherId;
    private String code;
    private String description;   // e.g. "1 Free Beer", "50% off drink"
    private String eventName;     // nullable - null means valid for all events
    private String createdBy;
    private boolean isRedeemed;

    public Voucher(int voucherId, String code, String description, String eventName, String createdBy, boolean isRedeemed) {
        this.voucherId   = voucherId;
        this.code        = code;
        this.description = description;
        this.eventName   = eventName;
        this.createdBy   = createdBy;
        this.isRedeemed  = isRedeemed;
    }

    public int getVoucherId()       { return voucherId; }
    public String getCode()         { return code; }
    public String getDescription()  { return description; }
    public String getEventName()    { return eventName != null ? eventName : "All Events"; }
    public String getCreatedBy()    { return createdBy; }
    public boolean isRedeemed()     { return isRedeemed; }

    public String getStatusText()   { return isRedeemed ? "Redeemed" : "Valid"; }
}
