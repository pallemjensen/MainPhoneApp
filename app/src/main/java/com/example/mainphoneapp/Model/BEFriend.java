package com.example.mainphoneapp.Model;

import com.google.firebase.firestore.GeoPoint;

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
    private OurGeoPoint m_ourGeo;
    //private GeoPoint m_location;

    public BEFriend(String id, String name, String phone, Double lat, Double lon, String mail, String picture,  String address) {
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

    public BEFriend(){}

    public String getPhone() {
        return m_phone;
    }

    public String getName() {
        return m_name;
    }

    public Double getLon(){ return m_lon;}

    public Double getLat() {return m_lat;}

    public String getMail() {return m_mail;}

    public String getPicture() {return m_picture;}

    public String getAddress() {return m_address;}

    public String getId() {return m_id;}

    public OurGeoPoint getLocation() { return m_ourGeo; }

    public void setLocation(GeoPoint geoPoint) {this.m_ourGeo = new OurGeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()); }

    /*
    public GeoPoint getLocation(){  return m_location;  }

    public void setLocation(GeoPoint location){ this.m_location = location; }
    */


    public void setLat(Double lat) {
        this.m_lat = lat;
    }

    public void setLng(Double lng){
        this.m_lon = lng;
    }

    public void setPicture(String picture) {this.m_picture = picture;}

    public void setId(String id) {this.m_id = id;}

    public void setAddress(String address) {
        this.m_address = address;
    }

    public void setName(String name){
        this.m_name = name;
    }

    public void setMail(String mail){
        this.m_mail = mail;
    }

    public void setPhone(String phone){
        this.m_phone = phone;
    }

    public String toString() {return "" + m_name;}




}



