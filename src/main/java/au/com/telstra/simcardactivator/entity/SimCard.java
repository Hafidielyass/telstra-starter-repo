package au.com.telstra.simcardactivator.entity;

import jakarta.persistence.*;

/**
 * JPA entity representing a SIM card activation record.
 */
@Entity
@Table(name = "sim_cards")
public class SimCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String iccid;

    private String customerEmail;

    private Boolean active;

    // Constructors
    public SimCard() {
    }

    public SimCard(String iccid, String customerEmail, Boolean active) {
        this.iccid = iccid;
        this.customerEmail = customerEmail;
        this.active = active;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
