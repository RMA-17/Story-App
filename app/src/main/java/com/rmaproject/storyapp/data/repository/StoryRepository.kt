package com.rmaproject.storyapp.data.repository

import android.location.Location
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rmaproject.storyapp.data.local.database.StoryDatabase
import com.rmaproject.storyapp.data.paging.StoryRemoteMediator
import com.rmaproject.storyapp.data.remote.api.ApiInterface
import com.rmaproject.storyapp.data.remote.model.login.LoginResponse
import com.rmaproject.storyapp.data.remote.model.register.RegisterResponse
import com.rmaproject.storyapp.data.remote.model.stories.Story
import com.rmaproject.storyapp.data.remote.model.upload.UploadResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

@OptIn(ExperimentalPagingApi::class)
class StoryRepository(
    private val api: ApiInterface,
    private val database: StoryDatabase
) {

    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): RegisterResponse = api.registerUser(name, email, password)

    suspend fun loginUser(email: String, password: String): LoginResponse = api.loginUser(email, password)

    fun getStoryList(): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            remoteMediator = StoryRemoteMediator(database, api),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).flow
    }

    suspend fun getStories(isLocationEnabled: String) = api.getStories(location = isLocationEnabled)

    suspend fun uploadStories(
        description: RequestBody,
        photo: MultipartBody.Part?,
        location: Location? = null
    ): UploadResponse {
        return if (location == null) {
            api.uploadStories(description, photo)
        } else {
            val lat = location.latitude
            val lon = location.longitude
            api.uploadStories(description, photo, lat.toFloat(), lon.toFloat())
        }
    }

}