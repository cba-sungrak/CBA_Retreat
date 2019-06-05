package kr.or.sungrak.cba.cba_retreat.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static final String URL = "http://cba.sungrak.or.kr:9000";
//    private static final String URL = "http://192.168.0.37:8080";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

//    static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request newRequest = chain.request().newBuilder()
//                    .addHeader("Authorization", "Basic YWRtaW46ZGh3bHJybGVoISEh")
//                    .build();
//            return chain.proceed(newRequest);
//        }
//    }).build();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(URL)
//                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
