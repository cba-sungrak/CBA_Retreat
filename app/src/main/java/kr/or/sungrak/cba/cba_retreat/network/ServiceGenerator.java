package kr.or.sungrak.cba.cba_retreat.network;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static final String URL = "http://cba.sungrak.or.kr:9000";
//    private static final String URL = "http://192.168.0.37:8080";

    static OkHttpClient client =
            new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", "Basic YWRtaW46ZGh3bHJybGVoISEh")
                                    .addHeader("Content-Type","application/json;charset=UTF-8")
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    }).addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();


    private static Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client).build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
