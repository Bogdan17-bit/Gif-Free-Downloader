package com.example.giffreedownloader.api.data.model;

import com.google.gson.annotations.SerializedName;

public class Gif {
    @SerializedName("id")
    String id;

    @SerializedName("bitly_url")
    String bitlyUrl;

    @SerializedName("images")
    Images images;

    public Images getImage() {
        return images;
    }

    public void setImage(Images images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBitlyUrl() {
        return bitlyUrl;
    }

    public void setBitlyUrl(String bitlyUrl) {
        this.bitlyUrl = bitlyUrl;
    }
}
