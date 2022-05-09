package com.example.giffreedownloader.api.data.model;

import com.google.gson.annotations.SerializedName;

public class Images {
    @SerializedName("fixed_height")
    private Original parameters;

    public Images(Original parameters) {
        this.parameters = parameters;
    }

    public Original getParameters() {
        return parameters;
    }

    public void setParameters(Original parameters) {
        this.parameters = parameters;
    }
}
