package com.example.amrita.booksellingapp.myClasses;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserDetails {
    private String Id;
    private String name;
    private String add;
    private String clg;
    private String img;
    private Long pno;
    private @ServerTimestamp Date timestamp;
    public UserDetails(){}

    public UserDetails(String Id, String name, String add, String clg, String img, Long pno, Date timestamp) {
        this.Id=Id;
        this.name = name;
        this.add = add;
        this.clg = clg;
        this.img=img;
        this.pno=pno;
        this.timestamp=timestamp;
    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getClg() {
        return clg;
    }

    public void setClg(String clg) {
        this.clg = clg;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public Long getPno() {
        return pno;
    }

    public void setPno(Long pno) {
        this.pno = pno;
    }
}

