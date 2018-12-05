package com.dxc.customerAdapter.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="customer",indexes = {@Index(name = "cusIndex",columnList = "customerId"),
                                    @Index(name = "agentIndex",columnList = "agentCode"),
                                    @Index(name = "counIndex",columnList = "countryCode")})
public class CustomerEntity {
    @Id
    @Column(name = "id" , length = 36)
    private String id;
    @Column(name = "customerId", length = 10)
    private String customerId;

    @Column(name = "agentCode" , length = 10)
    private String agentCode;

    @Column(name = "countryCode", length = 10)
    private String countryCode;

    @Column(name = "createDate")
    private Date createDate;

    @Column(name = "modifiedDate")
    private Date modifiedDate;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
