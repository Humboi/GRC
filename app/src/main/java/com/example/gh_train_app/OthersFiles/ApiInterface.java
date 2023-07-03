package com.example.gh_train_app.OthersFiles;


import com.example.gh_train_app.models.BaseResponse;
import com.example.gh_train_app.models.RecieveMoney;
import com.example.gh_train_app.models.SendMoney;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("v1/receive")
    Call<BaseResponse> recieveMoneyCall(@Body RecieveMoney recieveMoney);

    @POST("v1/cashout")
    Call<BaseResponse> sendMoneyCall(@Body SendMoney sendMoney);

    @GET("v1/status/{transactionid}")
    Call<BaseResponse> checkStatusCall(@Path("transactionid") String transactionid, @Header("appid") String appID);
}
