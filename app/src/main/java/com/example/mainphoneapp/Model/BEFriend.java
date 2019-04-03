package com.example.mainphoneapp.Model;

import java.io.Serializable;

public class BEFriend implements Serializable {

    long m_id;
    private String m_name;
    private String m_phone;
    private Double m_lat;
    private Double m_lon;
    private String m_mail;
    private String m_website;
    private String m_picture;
    private String m_birthday;
    private String m_address;

    public BEFriend(String name, String phone, Double lat, Double lon, String mail, String website, String picture, String birthday, String address) {
        this.m_name = name;
        this.m_phone = phone;
        this.m_lat = lat;
        this.m_lon = lon;
        this.m_mail = mail;
        this.m_website = website;
        this.m_picture = picture;
        this.m_birthday = birthday;
        this.m_address = address;
    }

    public String getPhone() {
        return m_phone;
    }

    public String getName() {
        return m_name;
    }

    public Double getLon(){ return m_lon;}

    public Double getLat() {return m_lat;}


    public void setLat(Double lat) {
        this.m_lat = lat;
    }

    public void setLng(Double lng){
        this.m_lon = lng;
    }


    public String getMail() {return m_mail;}

    public String getWebsite() {return m_website;}

    public String getPicture() {return m_picture;}

    public String getBirthday() {return m_birthday;}

    public String getAddress() {return m_address;}

}