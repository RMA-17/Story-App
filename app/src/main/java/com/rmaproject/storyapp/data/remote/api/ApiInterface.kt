package com.rmaproject.storyapp.data.remote.api

import com.rmaproject.storyapp.data.remote.model.login.LoginResponse
import com.rmaproject.storyapp.data.remote.model.register.RegisterResponse
import com.rmaproject.storyapp.data.remote.model.stories.StoriesResponse
import com.rmaproject.storyapp.data.remote.model.upload.UploadResponse
import com.rmaproject.storyapp.utils.getToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiInterface {
    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: String =  "0",
        @Header("Authorization") apiToken: String = getToken()
    ): StoriesResponse

    @GET("stories")
    suspend fun getStories(
        @Query("location") location: String =  "1",
        @Header("Authorization") apiToken: String = getToken()
    ): StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStories(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part?,
        @Part("lat") latitude: Float?,
        @Part("lon") longitude: Float?,
        @Header("Authorization") apiToken: String = getToken()
    ) : UploadResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStories(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part?,
        @Header("Authorization") apiToken: String = getToken()
    ) : UploadResponse
}