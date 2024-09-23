package com.example.testaudioenglish.ApiService;

import com.example.testaudioenglish.Model.DictationQuestionsModel;
import com.example.testaudioenglish.Model.ToeicModel.UserScoreModel;
import com.example.testaudioenglish.Model.TopicDictationModel;
import com.example.testaudioenglish.Response.DictationQuestionsRespone;
import com.example.testaudioenglish.Response.DictationRespone;
import com.example.testaudioenglish.Response.ListeningResponse;
import com.example.testaudioenglish.Response.MultipleChoiceRespone;
import com.example.testaudioenglish.Response.ReadingResponse;
import com.example.testaudioenglish.Response.TopicResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EnglishAppService {
    @GET("/dev/api/v1/listening/id/{id}/part/{part}")
    Call<ListeningResponse> getListeningData(
            @Path("id") long id,
            @Path("part") String part
    );

    @GET("/dev/api/v1/listening-answers/id_quiz/{id_quiz}")
    Call<ListeningResponse> getListeningAllData(
            @Path("id_quiz") long id
    );

    @GET("/dev/api/v1/show/id/{id_quiz}")
    Call<MultipleChoiceRespone> getAllPart5(@Path("id_quiz") long id);

    @GET("/dev/api/v1/reading/id/{id}/part/{part}")
    Call<ReadingResponse> getAllPart6(@Path("id") long id, @Path("part") String part);

    @GET("/dev/api/v1/show_quiz")
    Call<TopicResponse> getAllTopicToeic();

    @POST("/dev/api/v1/complete_exam")
    Call<Void> postScoreCompelte(@Body UserScoreModel userScoreModel);

    @GET("/dev/api/v1/dictation/get_dictation_topic")
    Call<DictationRespone> getAllTopicDictation();

    @GET("/dev/api/v1/dictation/id/{id_dictation}")
    Call<DictationQuestionsRespone> getAllQuestionByIdDictation(@Path("id_dictation") long id);

}
