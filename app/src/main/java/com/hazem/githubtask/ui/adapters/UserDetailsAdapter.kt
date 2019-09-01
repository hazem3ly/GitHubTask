package com.hazem.githubtask.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hazem.githubtask.data.network.response.RepoDetails

class UserDetailsAdapter :
    ListAdapter<RepoDetails, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return RepoViewHolder.create(parent)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as RepoViewHolder).bind(repoItem)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<RepoDetails>() {
            override fun areItemsTheSame(oldItem: RepoDetails, newItem: RepoDetails): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: RepoDetails, newItem: RepoDetails): Boolean =
                oldItem == newItem
        }
    }
}