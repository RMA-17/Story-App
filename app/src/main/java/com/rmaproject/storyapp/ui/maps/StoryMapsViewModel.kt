package com.rmaproject.storyapp.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmaproject.storyapp.data.repository.StoryRepository
import com.rmaproject.storyapp.ui.maps.events.StoryMapEvent
import kotlinx.coroutines.launch

class StoryMapsViewModel(
    private val repository: StoryRepository
) : ViewModel() {

    private val _eventFlow = MutableLiveData<StoryMapEvent>()
    val eventFlow: LiveData<StoryMapEvent> = _eventFlow

    fun getStories() {
        viewModelScope.launch {
            try {
                val response = repository.getStories("1")
                if (response.error) {
                    _eventFlow.postValue(StoryMapEvent.Error(response.message))
                    Log.d("STORY_ERR", response.message)
                    return@launch
                }
                _eventFlow.postValue(StoryMapEvent.Success(response.listStory))
            } catch (e: Exception) {
                Log.d("STORY_ERR", e.toString())
                _eventFlow.postValue(StoryMapEvent.Error(e.message ?: "Error when fetching data"))
            }
        }
    }
}