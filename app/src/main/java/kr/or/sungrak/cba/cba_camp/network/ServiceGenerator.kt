package kr.or.sungrak.cba.cba_camp.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {

    private const val URL = "https://cba.sungrak.or.kr:9000"

    //    private static final String URL = "http://192.168.0.37:8080";
    private var client = OkHttpClient.Builder().apply {
        addInterceptor { chain: Interceptor.Chain ->
            val newRequest = chain.request().newBuilder().apply {
                addHeader("Authorization", "Basic YWRtaW46ZGh3bHJybGVoISEh")
                addHeader("Content-Type", "application/json;charset=UTF-8")
            }.build()
            chain.proceed(newRequest)
        }
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }.build()


    private val retrofit = Retrofit.Builder().apply {
        baseUrl(URL)
        client(client)
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    @kotlin.jvm.JvmField
    val createService: ApiService = retrofit.create(ApiService::class.java)
}

