package com.example.imagesearch.domain.repository
import com.example.imagesearch.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {

    //Creating Auth Interceptor to add api_key query in front of all the requests.
    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url()
            .newBuilder()
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .addHeader("x-rapidapi-key", BuildConfig.APIKEY)
            .addHeader("x-rapidapi-host", "contextualwebsearch-websearch-v1.p.rapidapi.com")
            .build()

        chain.proceed(newRequest)
    }

    //OkhttpClient for building http request url
    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .readTimeout(90, TimeUnit.SECONDS)
        .build()


    fun retrofit(): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://contextualwebsearch-websearch-v1.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}