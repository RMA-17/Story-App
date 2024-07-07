package com.rmaproject.storyapp.ui.maps

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.rmaproject.storyapp.R
import com.rmaproject.storyapp.data.factory.ViewModelFactory
import com.rmaproject.storyapp.data.remote.model.stories.Story
import com.rmaproject.storyapp.ui.maps.events.StoryMapEvent

class StoryMapFragment : Fragment() {

    private lateinit var gMap: GoogleMap
    private val viewModel: StoryMapsViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private var boundsBuilder = LatLngBounds.builder()

    private val callback = OnMapReadyCallback { googleMap ->
        gMap = googleMap

        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true

        setCustomMapStyle()

        googleMap.setOnPoiClickListener { poi ->
            val poiMarker = googleMap.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
            poiMarker?.showInfoWindow()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())

        exitTransition = inflater.inflateTransition(android.R.transition.explode)
        enterTransition = inflater.inflateTransition(android.R.transition.explode)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()

        createContextMenu(menuHost)

        viewModel.eventFlow.observe(viewLifecycleOwner) { event ->
            when (event) {
                is StoryMapEvent.Error -> {
                    Toast.makeText(requireContext(), "Error: ${event.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                is StoryMapEvent.Success -> {
                    addMarkers(event.storyList)
                }
            }
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onStart() {
        super.onStart()

        viewModel.getStories()
    }

    private fun setCustomMapStyle() {
        try {
            val success =
                gMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.custom_map_style
                    )
                )
            if (!success) Log.d("ERR_MAP", "Style parsing failed")
        } catch (e: Exception) {
            Log.d("ERR_MAP", e.toString())
        }
    }

    private fun addMarkers(storyList: List<Story>) {
        for ((name, lat, lon, description) in storyList) {
            gMap.addMarker(
                MarkerOptions()
                    .position(LatLng(lat!!, lon!!))
                    .title(name)
                    .snippet(description)
            )
            boundsBuilder.include(LatLng(lat, lon))
        }

        val bounds = boundsBuilder.build()

        gMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private fun createContextMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.map_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.normal_type -> {
                        gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                        true
                    }
                    R.id.satellite_type -> {
                        gMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        true
                    }
                    R.id.terrain_type -> {
                        gMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        true
                    }
                    R.id.hybrid_type -> {
                        gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}