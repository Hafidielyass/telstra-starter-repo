package au.com.telstra.simcardactivator.api;

/**
 * Response for querying a SIM card record by ID.
 */
public class SimCardResponse {
    private String iccid;
    private String customerEmail;
    private Boolean active;

    public SimCardResponse() {
    }

    public SimCardResponse(String iccid, String customerEmail, Boolean active) {
        this.iccid = iccid;
        this.customerEmail = customerEmail;
        this.active = active;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
