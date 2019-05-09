package com.example.mainphoneapp.Helper;

public class Upload {

    private String mName;
    private String mImageUrl;

    public Upload() {
            //Empty constructor needed
    }

    public Upload(String name, String imageUrl) {

        if (name.trim().equals("")){
            name = "No Name";
        }
        this.mName = name;
        this.mImageUrl = imageUrl;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        this.mName = name;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.mImageUrl = imageUrl;
    }

}
