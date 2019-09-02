/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazem.githubtask.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hazem.githubtask.R
import com.hazem.githubtask.data.network.response.RepoDetails
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_layout.view.*

/**
 * View Holder for a [Repo] RecyclerView list item.
 */
class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val ownerImage = itemView.owner_image
    private val repoDescription = itemView.repo_description
    private val forksCount = itemView.forks_count
    private val language = itemView.language
    private val starts_count = itemView.starts_count
    private val repoName = itemView.repo_name
    private val imageLoader = itemView.image_loader


    private var repo: RepoDetails? = null

    init {
        view.setOnClickListener {
            repo?.html_url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(repo: RepoDetails?) {
        if (repo == null) {
            val resources = itemView.resources
            repoName.text = resources.getString(R.string.loading)
            starts_count.text = resources.getString(R.string.loading)
            repoDescription.visibility = View.GONE
            language.visibility = View.GONE
            imageLoader.visibility = View.VISIBLE
            forksCount.text = resources.getString(R.string.unknown)
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: RepoDetails) {
        this.repo = repo
        repoName.text = repo.name

        // if the description is missing, hide the TextView
        var descriptionVisibility = View.GONE
        if (repo.description != null) {
            repoDescription.text = repo.description
            descriptionVisibility = View.VISIBLE
        }
        repoDescription.visibility = descriptionVisibility

        forksCount.text = repo.forks_count.toString()
        starts_count.text = repo.stargazers_count.toString()

        // if the language is missing, hide the label and the value
        var languageVisibility = View.GONE
        if (!repo.language.isNullOrEmpty()) {
            val resources = this.itemView.context.resources
            language.text = resources.getString(R.string.language, repo.language)
            languageVisibility = View.VISIBLE
        }
        language.visibility = languageVisibility
        Picasso.get().load(repo.owner?.avatar_url).error(R.drawable.github_repo).fit()
            .into(ownerImage, object : Callback {
                override fun onError(e: Exception?) {
                    imageLoader.visibility = View.GONE
                }

                override fun onSuccess() {
                    imageLoader.visibility = View.GONE
                }
            })

    }

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout, parent, false)
            return RepoViewHolder(view)
        }
    }
}
