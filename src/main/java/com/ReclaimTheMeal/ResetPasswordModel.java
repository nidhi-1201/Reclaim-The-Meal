package com.parth.grabthemealv3;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class ResetPasswordModel {
    @Id
    private int uid;
    private String token;

    private Timestamp exipryDate;
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getExipryDate() {
        return exipryDate;
    }

    public void setExipryDate(Timestamp exipryDate) {
        this.exipryDate = exipryDate;
    }
}
