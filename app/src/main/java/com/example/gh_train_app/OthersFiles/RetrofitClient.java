package com.example.gh_train_app.OthersFiles;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static final String BASE_URL = "https://api.mailgun.net/v3/sandboxcbdbdd774e4c4bbd957b6e56a500fe20.mailgun.org/";

    private static final String API_USERNAME = "api";

    //you need to change the value to your API key
    private static final String API_PASSWORD = "a668b836f283b05e8f95713226207ef6-07e45e2a-08ce663c";

    private static final String AUTH = "Basic " + Base64.encodeToString((API_USERNAME+":"+API_PASSWORD).getBytes(), Base64.NO_WRAP);

    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient() {
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                //Adding basic auth
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Authorization", AUTH)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Retrofit getClient() {
        return retrofit;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
