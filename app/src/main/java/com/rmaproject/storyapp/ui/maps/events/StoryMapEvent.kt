package com.rmaproject.storyapp.ui.maps.events

import com.rmaproject.storyapp.data.remote.model.stories.Story

sealed class StoryMapEvent {
    data class Success(val storyList: List<Story>) : StoryMapEvent()
    data class Error(val message: String) : StoryMapEvent()
}