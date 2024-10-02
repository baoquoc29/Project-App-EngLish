package com.example.testaudioenglish.ApiService;

import com.example.testaudioenglish.Model.LanguageDetails;
import com.example.testaudioenglish.Response.TranslateRequest;
import com.example.testaudioenglish.Response.TranslateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RapidAPI {
    @Headers({
            "x-rapidapi-key: 62ed0af846mshe249d10bb680664p18aa2djsnae34e75dcc0a",
            "x-rapidapi-host: ai-translate.p.rapidapi.com",
            "Content-Type: application/json"
    })
    @POST("translate")
    Call<TranslateResponse> translate(@Body TranslateRequest request);

    @Headers({
            "x-rapidapi-key: 62ed0af846mshe249d10bb680664p18aa2djsnae34e75dcc0a",
            "x-rapidapi-host: english-word-translator-api.p.rapidapi.com",
            "Content-Type: application/json"
    })
    @GET("translate")
    Call<LanguageDetails> getLanguageDetails(  @Query("text") String text,
                                    @Query("to_lang") String to_lang);
    @Headers({
            "x-rapidapi-key: 62ed0af846mshe249d10bb680664p18aa2djsnae34e75dcc0a",
            "x-rapidapi-host: english-word-translator-api.p.rapidapi.com",
            "Content-Type: application/json"
    })
    @GET("audio")
    Call<LanguageDetails> getAudio(  @Query("pronunciation_id") String id);
}

