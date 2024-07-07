package com.rmaproject.storyapp

import android.app.Application
import com.google.android.material.color.DynamicColors

class StoryAppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}