package com.rmaproject.storyapp.ui.addstory

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rmaproject.storyapp.R
import com.rmaproject.storyapp.data.factory.ViewModelFactory
import com.rmaproject.storyapp.databinding.FragmentAddStoryBinding
import com.rmaproject.storyapp.ui.addstory.events.AddStoryEvent
import com.rmaproject.storyapp.ui.addstory.events.AddStoryUiEvent
import com.rmaproject.storyapp.utils.ObjectConverters
import com.rmaproject.storyapp.utils.checkPermission
import com.rmaproject.storyapp.utils.showSnackbar

class AddStoryFragment : Fragment(R.layout.fragment_add_story) {

    private val binding: FragmentAddStoryBinding by viewBinding()
    private val viewModel: AddStoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var uriSelected: Uri? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())

        enterTransition = inflater.inflateTransition(android.R.transition.explode)
        reenterTransition = inflater.inflateTransition(android.R.transition.explode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        binding.edAddDescription.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(AddStoryEvent.AddDescription(text.toString()))
        }

        binding.buttonAdd.setOnClickListener {
            viewModel.onEvent(AddStoryEvent.UploadStory(requireContext()))
        }

        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLocation()
            } else {
                viewModel.onEvent(AddStoryEvent.AddLocation(null))
            }
        }

        viewModel.imageToBeUploaded.observe(viewLifecycleOwner) { image ->
            binding.imgNewStory.load(image)
        }

        binding.containerNewStory.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        viewModel.eventFlow.observe(viewLifecycleOwner) { event ->
            when (event) {
                is AddStoryUiEvent.EmptyDescription -> {
                    binding.inputDescription.error = getString(R.string.err_desc_empty)
                }
                is AddStoryUiEvent.EmptyImage -> {
                    showSnackbar(
                        binding.root,
                        getString(R.string.err_pick_img_first)
                    ).show()
                }
                is AddStoryUiEvent.Error -> {
                    showSnackbar(
                        binding.root,
                        "${getString(R.string.msg_err_occured)}: ${event.message}"
                    ).show()
                    toggleProgress(false)
                }
                is AddStoryUiEvent.Loading -> {
                    toggleProgress(true)
                }
                is AddStoryUiEvent.Success -> {
                    toggleProgress(false)
                    Toast.makeText(requireContext(), event.response.message, Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun toggleProgress(isVisible: Boolean) {
        binding.progressCircular.isVisible = isVisible
        binding.buttonAdd.isEnabled = !isVisible
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data
                    uriSelected = fileUri
                    val bitmap = ObjectConverters.convertUriToBitmap(
                        requireContext(),
                        uriSelected ?: return@registerForActivityResult
                    )
                    viewModel.onEvent(AddStoryEvent.AddImage(bitmap))
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.msg_cancel_pick_img),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLocation()
                }
                else -> {
                    binding.switchLocation.isChecked = false
                }
            }
        }

    private fun getMyLocation() {
        if (
            checkPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("ERR_LOCATION", task.exception.toString())
                    return@addOnCompleteListener
                }
                val location = task.result
                viewModel.onEvent(AddStoryEvent.AddLocation(location))
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}