package com.example.giffreedownloader.api.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JSONResponse {
    @SerializedName("data")
    private List<Gif> gifs;

    public JSONResponse(List<Gif> gifs) {
        this.gifs = gifs;
    }

    public void addGif(Gif e) {
        gifs.add(e);
    }

    public List<Gif> getGifs() {
        return gifs;
    }

    public void setGifs(List<Gif> gifs) {
        this.gifs = gifs;
    }
}
