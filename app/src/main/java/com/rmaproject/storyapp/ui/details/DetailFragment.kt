package com.rmaproject.storyapp.ui.details

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.google.android.gms.maps.model.LatLng
import com.rmaproject.storyapp.R
import com.rmaproject.storyapp.data.remote.model.stories.Story
import com.rmaproject.storyapp.databinding.FragmentDetailBinding
import com.rmaproject.storyapp.utils.ObjectConverters
import java.util.*

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding: FragmentDetailBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(android.R.transition.explode)
        exitTransition = inflater.inflateTransition(android.R.transition.explode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        val story = arguments?.getParcelable<Story>(STORY_ID_KEY)!!

        with(binding) {
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
            txtDate.text = getString(
                R.string.txt_uploaded_on, ObjectConverters.isoDateFormatter(
                    story.createdAt!!,
                    TimeZone.getDefault().id
                )
            )
            ivItemPhoto.load(story.photoUrl) {
                error(R.drawable.ic_baseline_image_24)
                placeholder(R.drawable.image_shimmer)
                lifecycle(lifecycle)
            }
            binding.txtLocation.text = if (story.lat != null && story.lon != null) {
                ObjectConverters.latLongToActualLocation(
                    requireContext(),
                    LatLng(story.lat, story.lon)
                )
            } else getString(R.string.txt_no_location)

        }
    }

    companion object {
        const val STORY_ID_KEY = "STORY_ID_RECEIVE"
    }
}