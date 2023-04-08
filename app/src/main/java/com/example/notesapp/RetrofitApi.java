package com.example.notesapp;

import com.example.delivery.Mpesa.DataModal;
import com.example.delivery.Mpesa.ExpressGetStatusResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitApi {
    @POST("express/initialize")
    Call<DataModal> createPost(
            @Header("Apikey") String apikey,
            @Header("Accept") String acceptHeader,
            @Body DataModal data

    );

//    @GET("express/get_status/{account_no}")
//    Call <ExpressGetStatusResponse> getExpressGetStatus(
//            @Path("account_no") String accountNo,
//            @Header("Apikey") String apiKey,
//            @Header("Accept") String accept
//    );
}
