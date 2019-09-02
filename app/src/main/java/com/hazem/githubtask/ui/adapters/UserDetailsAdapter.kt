package com.hazem.githubtask.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hazem.githubtask.data.model.RepoDetails
import java.util.*


class UserDetailsAdapter :
    ListAdapter<RepoDetails, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    val LOADING_VIEW = 1
    val DATA_VIEW = 2

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            LOADING_VIEW -> LoadingViewHolder.create(parent)
            else -> RepoViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val repoItem = getItem(position)
        if (repoItem != null && holder is RepoViewHolder) {
            holder.bind(repoItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == itemCount - 1) LOADING_VIEW else DATA_VIEW
        } else {
            DATA_VIEW
        }
    }

    fun getList(): MutableList<RepoDetails?> {
        val items = ArrayList<RepoDetails?>()
        for (i in 0 until itemCount) {
            items.add(getItem(i))
        }
        return items
    }

    private var isLoaderVisible = false

    fun showLoadingMore() {
        isLoaderVisible = true
        val list = getList()
        list.forEachIndexed { index, repo -> if (repo == null) list.removeAt(index) }
        list.add(null)
        submitList(list)
    }

    fun removeLoadingView() {
        isLoaderVisible = false
        val list = getList()
        list.forEachIndexed { index, repo -> if (repo == null) list.removeAt(index) }
        submitList(list)
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