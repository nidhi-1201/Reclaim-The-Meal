package com.parth.grabthemealv3;



import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class UserModel {
    @Id
    private int uid;
    private String email;



    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    }
