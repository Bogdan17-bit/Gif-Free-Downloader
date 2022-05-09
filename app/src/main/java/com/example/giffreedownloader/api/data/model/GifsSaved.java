package com.example.giffreedownloader.api.data.model;

import java.io.Serializable;
import java.util.List;

public class GifsSaved implements Serializable {

    private List<Gif> gifs;

    public GifsSaved(List<Gif> gifs) {
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
