package com.example.giffreedownloader.api.data.model;

import com.google.gson.annotations.SerializedName;

public class Original {
    @SerializedName("url")
    private String url;

    public Original(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
