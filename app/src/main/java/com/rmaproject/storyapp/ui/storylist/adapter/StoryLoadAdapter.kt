package com.rmaproject.storyapp.ui.storylist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rmaproject.storyapp.databinding.ItemLoadStoryBinding

class StoryLoadAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<StoryLoadAdapter.StoryLoadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): StoryLoadViewHolder {
        val binding =
            ItemLoadStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryLoadViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: StoryLoadViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class StoryLoadViewHolder(
        private val binding: ItemLoadStoryBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.btnReload.setOnClickListener { retry.invoke() }
            binding.progressCircular.isVisible = loadState is LoadState.Loading
            binding.btnReload.isVisible = loadState is LoadState.Error
            binding.tvMsgErrLoadData.isVisible = loadState is LoadState.Error
        }
    }
}