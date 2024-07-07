package com.rmaproject.storyapp.data.remote.model.stories

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class Story(
    val name: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val description: String? = null,
    @PrimaryKey val id: String,
    val createdAt: String? = null,
    val photoUrl: String? = null
) : Parcelable