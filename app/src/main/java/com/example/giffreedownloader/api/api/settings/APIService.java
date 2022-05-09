package com.example.giffreedownloader.api.api.settings;

import com.example.giffreedownloader.api.data.model.JSONResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET(APIChapters.PREFIX_TRENDING + APIChapters.API_KEY_SUFFIX + APIChapters.LIMIT_GIFS + APIChapters.RATING)
    Call<JSONResponse> getTrendingGifs();

    @GET(APIChapters.PREFIX_SEARCH + APIChapters.API_KEY_SUFFIX + APIChapters.LIMIT_GIFS + APIChapters.RATING + APIChapters.LANGUAGE)
    Call<JSONResponse> getGifsFromSearch(@Query("q") String strSearch, @Query("offset") int offset);
}
