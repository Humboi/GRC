package com.example.gh_train_app.OthersFiles;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.gh_train_app.OthersFiles.Tools.APIKEY;
import static com.example.gh_train_app.OthersFiles.Tools.BASE_URL;
import static com.example.gh_train_app.OthersFiles.Tools.CONTENT_TYPE;


public class ApiClient {

    public static Retrofit getClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        LoggingInterceptor interceptor = new LoggingInterceptor();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            Objects.requireNonNull(copy.body()).writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static class LoggingInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("Content-Type",  CONTENT_TYPE)
                    .addHeader("apikey",  APIKEY).build();

            long t1 = System.nanoTime();
            String requestLog = String.format("Sending request %s on %s%n%s\n\n", request.url(), chain.connection(), request.headers());

            try {
                if (request.method().equalsIgnoreCase("post") || request.method().equalsIgnoreCase("put") || request.method().equalsIgnoreCase("patch") || request.method().equalsIgnoreCase("delete")) {
                    requestLog = "\n" + requestLog + "\n" + bodyToString(request);
                }
            } catch (Exception e) {
                Log.d("HTTP" , e.getMessage());
            }

            Log.d("HTTP \n" ,requestLog);

            final Response[] mainResponse = {chain.proceed(request)};
            long t2 = System.nanoTime();

            @SuppressLint("DefaultLocale") String responseLog = String.format("Received response for %s in %.1fms%n%s", mainResponse[0].request().url(), (t2 - t1) / 1e6d, mainResponse[0].headers());

            String bodyString = Objects.requireNonNull(mainResponse[0].body()).string();

            Log.d("HTTP" + "\n", responseLog + "\n" + bodyString);


            return mainResponse[0].newBuilder()
                    .body(ResponseBody.create(Objects.requireNonNull(mainResponse[0].body()).contentType(), bodyString))
                    .build();

        }
    }



}
