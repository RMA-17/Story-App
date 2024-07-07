package com.rmaproject.storyapp.ui.storylist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.rmaproject.storyapp.R
import com.rmaproject.storyapp.data.remote.model.stories.Story
import com.rmaproject.storyapp.databinding.ItemStoryBinding
import com.rmaproject.storyapp.utils.ObjectConverters
import java.util.*

class StoryAdapter(
    private val adapterOnClick: (Story) -> Unit
) : PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) holder.bind(story, adapterOnClick)
    }

    class StoryViewHolder(
        private val binding: ItemStoryBinding,
    ) : ViewHolder(binding.root) {
        fun bind(story: Story, onClick: (Story) -> Unit) {
            binding.root.setOnClickListener {
                onClick.invoke(story)
            }
            binding.tvDetailName.text = story.name
            binding.tvDetailDescription.text = story.description
            binding.txtDate.text =
                ObjectConverters.isoDateFormatter(story.createdAt!!, TimeZone.getDefault().id)
            binding.ivItemPhoto.load(story.photoUrl) {
                crossfade(true)
                placeholder(R.drawable.image_shimmer)
                error(R.drawable.ic_baseline_image_24)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}