package com.rmaproject.storyapp.di

import android.content.Context
import com.rmaproject.storyapp.data.local.database.StoryDatabase
import com.rmaproject.storyapp.data.remote.api.ApiConfig
import com.rmaproject.storyapp.data.repository.StoryRepository

object Injection {
    fun provideRepository(context: Context) : StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val api = ApiConfig.getApi()
        return StoryRepository(api, database)
    }
}