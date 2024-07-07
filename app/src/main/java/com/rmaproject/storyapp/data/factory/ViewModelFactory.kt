package com.rmaproject.storyapp.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rmaproject.storyapp.data.repository.StoryRepository
import com.rmaproject.storyapp.di.Injection
import com.rmaproject.storyapp.ui.addstory.AddStoryViewModel
import com.rmaproject.storyapp.ui.auth.AuthViewModel
import com.rmaproject.storyapp.ui.maps.StoryMapsViewModel
import com.rmaproject.storyapp.ui.storylist.StoryListViewModel

class ViewModelFactory(
    private val repository: StoryRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository) as T
            modelClass.isAssignableFrom(StoryListViewModel::class.java) -> StoryListViewModel(repository) as T
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> AddStoryViewModel(repository) as T
            modelClass.isAssignableFrom(StoryMapsViewModel::class.java) -> StoryMapsViewModel(repository) as T
            else -> throw Exception("Unknown ViewModel Class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE:ViewModelFactory?= null
        fun getInstance(context: Context) : ViewModelFactory = INSTANCE?: synchronized(this) {
            INSTANCE?: ViewModelFactory(Injection.provideRepository(context))
        }.also { INSTANCE = it }
    }
}