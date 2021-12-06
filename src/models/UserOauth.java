package models;
// Generated Nov 23, 2021 7:39:07 AM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Post generated by hbm2java
 */
public class UserOauth extends Model<UserOauth> implements java.io.Serializable {

    private int userId;
    private int oauthType;
    private String accountId;
    private String accountFirstName;
    private String accountLastName;
    private String accountEmail;
    private String accountGender;

    public UserOauth() {
        this._table_name = "UserOauth";
    }

    public UserOauth(int userId, int oauthType, String accountId, String accountFirstName, String accountEmail, String accountGender) {
        this();
        this.userId = userId;
        this.oauthType = oauthType;
        this.accountId = accountId;
        this.accountFirstName = accountFirstName;
        this.accountEmail = accountEmail;
        this.accountGender = accountGender;
    }

    public UserOauth(int userId, int oauthType, String accountId, String accountFirstName, String accountEmail, String accountGender, Date updatedAt, Date createdAt, Date deletedAt) {
        this();
        this.userId = userId;
        this.oauthType = oauthType;
        this.accountId = accountId;
        this.accountFirstName = accountFirstName;
        this.accountEmail = accountEmail;
        this.accountGender = accountGender;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOauthType() {
        return oauthType;
    }

    public void setOauthType(int oauthType) {
        this.oauthType = oauthType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountFirstName() {
        return accountFirstName;
    }

    public void setAccountFirstName(String accountFirstName) {
        this.accountFirstName = accountFirstName;
    }

    public String getAccountLastName() {
        return accountLastName;
    }

    public void setAccountLastName(String accountLastName) {
        this.accountLastName = accountLastName;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountGender() {
        return accountGender;
    }

    public void setAccountGender(String accountGender) {
        this.accountGender = accountGender;
    }

    
}