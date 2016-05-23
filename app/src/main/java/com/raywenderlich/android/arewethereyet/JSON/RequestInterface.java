package com.raywenderlich.android.arewethereyet.JSON;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("android/testjson.json")//("android/jsonandroid")
    Call<JSONResponse> getJSON();
}
