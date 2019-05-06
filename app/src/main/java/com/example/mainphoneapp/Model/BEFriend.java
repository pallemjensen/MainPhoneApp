package com.example.mainphoneapp.Model;

import java.io.Serializable;

public class BEFriend implements Serializable {

    private long m_id;
    private String m_name;
    private String m_phone;
    private Double m_lat;
    private Double m_lon;
    private String m_mail;
    private String m_picture;
    private String m_address;

    public BEFriend(long id, String name, String phone, Double lat, Double lon, String mail, String picture,  String address) {
        this.m_id = id;
        this.m_name = name;
        this.m_phone = phone;
        this.m_lat = lat;
        this.m_lon = lon;
        this.m_mail = mail;
        this.m_picture = picture;
        this.m_address = address;
    }

    public BEFriend(String name, String phone, Double lat, Double lon, String mail, String picture, String address) {
        this.m_name = name;
        this.m_phone = phone;
        this.m_lat = lat;
        this.m_lon = lon;
        this.m_mail = mail;
        this.m_picture = picture;
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

    public String getPicture() {return m_picture;}

    public void setPicture(String picture) {this.m_picture = picture;}


    public String getAddress() {return m_address;}

    public String toString() {return "" + m_name;}

    public long getId() {return m_id;}

    public void setId(long id) {this.m_id = id;}
    }



