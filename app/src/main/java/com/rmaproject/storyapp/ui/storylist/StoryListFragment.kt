package com.rmaproject.storyapp.ui.storylist

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rmaproject.storyapp.R
import com.rmaproject.storyapp.data.factory.ViewModelFactory
import com.rmaproject.storyapp.data.preferences.UserInfo
import com.rmaproject.storyapp.databinding.FragmentListStoryBinding
import com.rmaproject.storyapp.ui.details.DetailFragment
import com.rmaproject.storyapp.ui.storylist.adapter.StoryAdapter
import com.rmaproject.storyapp.ui.storylist.adapter.StoryLoadAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StoryListFragment : Fragment(R.layout.fragment_list_story) {

    private val binding: FragmentListStoryBinding by viewBinding()
    private val viewModel: StoryListViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val storyAdapter by lazy {
        StoryAdapter { story ->
            val bundle = bundleOf(DetailFragment.STORY_ID_KEY to story)
            findNavController().navigate(
                R.id.action_nav_story_list_to_nav_detail,
                bundle
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        reenterTransition = inflater.inflateTransition(android.R.transition.move)
        exitTransition = inflater.inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        createContextMenu(menuHost)

        binding.storyRv.apply {
            adapter = storyAdapter.withLoadStateFooter(
                footer = StoryLoadAdapter {
                    storyAdapter.retry()
                }
            )
            layoutManager = LinearLayoutManager(requireContext())
        }

        storyAdapter.addLoadStateListener { state ->
            binding.progressCircular.isVisible = state.source.refresh is LoadState.Loading
            binding.viewError.isVisible = state.source.refresh is LoadState.Error
        }

        binding.btnRetry.setOnClickListener {
            storyAdapter.retry()
        }
    }

    override fun onStart() {
        super.onStart()
        if (UserInfo.token.isEmpty()) {
            findNavController().navigate(R.id.action_nav_story_list_to_nav_login)
            return
        }

        submitPagingData()
    }

    override fun onResume() {
        super.onResume()
        storyAdapter.refresh()
    }

    private fun submitPagingData() {
        lifecycleScope.launch {
            viewModel.stories.collectLatest { stories ->
                storyAdapter.submitData(lifecycle, stories)
            }
        }
    }

    private fun createContextMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_logout -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setIcon(R.drawable.ic_baseline_logout_24)
                            .setTitle("Log out")
                            .setMessage(getString(R.string.warn_logout))
                            .setNegativeButton(getString(R.string.txt_cancel)) { _, _ -> }
                            .setPositiveButton("Ya") { _, _ ->
                                UserInfo.clear()
                                findNavController().navigate(R.id.action_nav_story_list_to_nav_login)
                            }
                            .create().show()
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}