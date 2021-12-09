package com.bd.testbrandi.data.network

import com.bd.testbrandi.model.SearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

val BASE_URL: String = "https://dapi.kakao.com"
val REST_API_KEY: String = "d041c3f5493bef0d8954979c076ea20d"
val KAKAO_AK: String = "KakaoAK $REST_API_KEY"

interface ApiService {

    @GET("/v2/search/image")
    suspend fun searchImage(
        @Header("Authorization") kakaoAK: String = KAKAO_AK,
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): SearchResponse

    object ApiClient {
        private fun getClient(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val apiService: ApiService = getClient().create(ApiService::class.java)

        private fun getLoggingIntercepter() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        private fun getOkHttpClient() = OkHttpClient.Builder().addInterceptor(getLoggingIntercepter()).build()
    }
}