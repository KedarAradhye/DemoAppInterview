package com.android.demoappinterview.Network;

import com.android.demoappinterview.Model.GetSearchResultResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/")
    Call<GetSearchResultResponse> getSearchList(@Query("s") String searchname,@Query("page") int mPageNumber,@Query("apikey") String apiKey);
}
