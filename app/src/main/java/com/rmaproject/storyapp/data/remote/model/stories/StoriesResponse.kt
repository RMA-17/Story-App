package com.rmaproject.storyapp.data.remote.model.stories

data class StoriesResponse(
    val error: Boolean,
    val listStory: List<Story>,
    val message: String
)