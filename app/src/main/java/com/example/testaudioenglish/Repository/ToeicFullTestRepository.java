package com.example.testaudioenglish.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.ApiService.EnglishAppService;
import com.example.testaudioenglish.ApiService.RetrofitClient;
import com.example.testaudioenglish.Model.DictationQuestionsModel;
import com.example.testaudioenglish.Model.ToeicModel.UserScoreModel;
import com.example.testaudioenglish.Model.TopicDictationModel;
import com.example.testaudioenglish.Response.DictationQuestionsRespone;
import com.example.testaudioenglish.Response.DictationRespone;
import com.example.testaudioenglish.Response.ListeningResponse;
import com.example.testaudioenglish.Response.MultipleChoiceRespone;
import com.example.testaudioenglish.Response.ReadingResponse;
import com.example.testaudioenglish.Response.TopicResponse;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToeicFullTestRepository {
    private EnglishAppService englishAppService;

    public ToeicFullTestRepository() {
        englishAppService = RetrofitClient.getClient().create(EnglishAppService.class);
    }

    public LiveData<ListeningResponse> getListeningData(long id, String part) {
        MutableLiveData<ListeningResponse> data = new MutableLiveData<>();
        englishAppService.getListeningData(id, part).enqueue(new Callback<ListeningResponse>() {
            @Override
            public void onResponse(Call<ListeningResponse> call, Response<ListeningResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ListeningResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<ListeningResponse> getAllData(long id_quiz) {
        MutableLiveData<ListeningResponse> data = new MutableLiveData<>();
        englishAppService.getListeningAllData(id_quiz).enqueue(new Callback<ListeningResponse>() {
            @Override
            public void onResponse(Call<ListeningResponse> call, Response<ListeningResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ListeningResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<MultipleChoiceRespone> getAllDataPart5(long id_quiz) {
        MutableLiveData<MultipleChoiceRespone> data = new MutableLiveData<>();
        englishAppService.getAllPart5(id_quiz).enqueue(new Callback<MultipleChoiceRespone>() {
            @Override
            public void onResponse(Call<MultipleChoiceRespone> call, Response<MultipleChoiceRespone> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MultipleChoiceRespone> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<ReadingResponse> getAllDataPart6(long id, String part) {
        MutableLiveData<ReadingResponse> data = new MutableLiveData<>();
        englishAppService.getAllPart6(id, part).enqueue(new Callback<ReadingResponse>() {
            @Override
            public void onResponse(Call<ReadingResponse> call, Response<ReadingResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ReadingResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<TopicResponse> getAllTopics() {
        Log.d("abc", "getAllTopics method called");
        MutableLiveData<TopicResponse> data = new MutableLiveData<>();
        englishAppService.getAllTopicToeic().enqueue(new Callback<TopicResponse>() {
            @Override
            public void onResponse(Call<TopicResponse> call, Response<TopicResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                    Log.d("abc", "API Response: " + response.toString());
                } else {
                    data.setValue(null);
                    Log.d("abc", "API Response is null");
                }
            }

            @Override
            public void onFailure(Call<TopicResponse> call, Throwable t) {
                data.setValue(null);
                Log.d("abc", "API Call Failure", t);
            }
        });
        return data;
    }

    public void completeExam(UserScoreModel userScoreModel) {
        Call<Void> call = englishAppService.postScoreCompelte(userScoreModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xử lý thành công nếu cần
                    Log.d("Bug12", "Gửi điểm thành công");
                } else {
                    // In thông tin chi tiết khi phản hồi không thành công
                    String errorMessage = response.message();
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string(); // Đọc nội dung lỗi từ ResponseBody
                        }
                    } catch (IOException e) {
                        errorBody = "Không thể đọc chi tiết lỗi: " + e.getMessage();
                    }
                    Log.d("Bug12", "Lỗi: " + errorMessage + " - Chi tiết: " + errorBody);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // In thông tin lỗi khi yêu cầu không thành công
                Log.d("Bug12", "Lỗi: " + t.getMessage(), t);
            }
        });
    }

    public LiveData<DictationRespone> getDictationTopic() {
        MutableLiveData<DictationRespone> data = new MutableLiveData<>();
        englishAppService.getAllTopicDictation().enqueue(new Callback<DictationRespone>() {
            @Override
            public void onResponse(Call<DictationRespone> call, Response<DictationRespone> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());

                } else {
                    data.setValue(null);
                    Log.d("abc", "API Response is null");
                }
            }

            @Override
            public void onFailure(Call<DictationRespone> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<DictationQuestionsRespone> getAllQuestionDictation(Long id) {
        MutableLiveData<DictationQuestionsRespone> data = new MutableLiveData<>();
        englishAppService.getAllQuestionByIdDictation(id).enqueue(new Callback<DictationQuestionsRespone>() {
            @Override
            public void onResponse(Call<DictationQuestionsRespone> call, Response<DictationQuestionsRespone> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }
                else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<DictationQuestionsRespone> call, Throwable t) {

            }
        });
        return data;
    }
}
