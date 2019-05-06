package com.example.mainphoneapp.Model;

import java.io.Serializable;

public class BEFriend implements Serializable {

    private String m_id;
    private String m_name;
    private String m_phone;
    private Double m_lat;
    private Double m_lon;
    private String m_mail;
    private String m_picture;
    private String m_address;


    public BEFriend() {
    }

    public String getPhone() {
        return m_phone;
    }

    public void setPhone(String phone) {
        this.m_phone = phone;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        this.m_name = name;
    }

    public Double getLon() {
        return m_lon;
    }

    public Double getLat() {
        return m_lat;
    }

    public void setLat(Double lat) {
        this.m_lat = lat;
    }

    public void setLng(Double lng) {
        this.m_lon = lng;
    }

    public String getMail() {
        return m_mail;
    }

    public void setmail(String mail) {
        this.m_mail = mail;
    }

    public String getPicture() {
        return m_picture;
    }

    public void setPicture(String picture) {
        this.m_picture = picture;
    }


    public String getAddress() {
        return m_address;
    }

    public void setAddress(String address) {
        this.m_address = address;
    }

    public String getId() {
        return m_id;
    }

    public void setId(String id) {
        this.m_id = id;
    }


    public String toString() {
        return "" + m_name;
    }

}


