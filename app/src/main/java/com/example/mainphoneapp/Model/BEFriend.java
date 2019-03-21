package com.example.mainphoneapp.Model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Date;

public class BEFriend implements Serializable {

    private String m_name;
    private String m_phone;
    private double m_lat;
    private double m_lon;
    private String m_mail;
    private String m_website;
    private String m_picture;
    private String m_birthday;

    public BEFriend(String name, String phone, double lat, double lon, String mail, String website, String picture, String birthday) {
        this.m_name = name;
        this.m_phone = phone;
        this.m_lat = lat;
        this.m_lon = lon;
        this.m_mail = mail;
        this.m_website = website;
        this.m_picture = picture;
        this.m_birthday = birthday;
    }

    public String getPhone() {
        return m_phone;
    }

    public String getName() {
        return m_name;
    }

    public Double getLon(){ return m_lon;}

    public Double getLat() {return m_lat;}

    public String getMail() {return m_mail;}

    public String getWebsite() {return m_website;}

    public String getPicture() {return m_picture;}

    public String getBirthday() {return m_birthday;}
}