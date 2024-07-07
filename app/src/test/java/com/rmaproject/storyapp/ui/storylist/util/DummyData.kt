package com.rmaproject.storyapp.ui.storylist.util

import com.rmaproject.storyapp.data.remote.model.stories.Story

object DummyData {
    fun generateDummyResponse() :  List<Story> {
        val stories: MutableList<Story> = arrayListOf()

        for (i in 1..20) {
            stories.add(
                Story(
                    name = "User $i",
                    lat = 12312312.3,
                    lon = 12312312.3,
                    description = "test",
                    id = i.toString(),
                    createdAt = "2022-01-08T06:34:18.598Z",
                    photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png"
                )
            )
        }

        return stories
    }
}