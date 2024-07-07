package com.rmaproject.storyapp.ui.storylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rmaproject.storyapp.data.remote.model.stories.Story
import com.rmaproject.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class StoryListViewModel(
    repository: StoryRepository
) : ViewModel() {
    val stories: Flow<PagingData<Story>> = repository.getStoryList().cachedIn(viewModelScope)
}