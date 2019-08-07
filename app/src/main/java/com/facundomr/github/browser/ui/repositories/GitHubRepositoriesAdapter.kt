package com.facundomr.github.browser.ui.repositories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facundomr.github.browser.R
import com.facundomr.github.browser.ui.model.GitHubRepository
import kotlinx.android.synthetic.main.item_repository.view.*

class GitHubRepositoriesAdapter(val clickListener: (GitHubRepository?) -> Unit)
    : PagedListAdapter<GitHubRepository, GitHubRepositoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubRepositoryViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context).inflate(R.layout.item_repository, null)
        return GitHubRepositoryViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: GitHubRepositoryViewHolder, position: Int) {

        val item = getItem(position)

        if (item == null) {
            holder.showPlaceHolder()
        } else {
            holder.render(item)
        }

        holder.itemView.setOnClickListener {
            clickListener.invoke(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GitHubRepository>() {

            override fun areItemsTheSame(old: GitHubRepository, new: GitHubRepository) = old.url == new.url

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(old: GitHubRepository, new: GitHubRepository) = old.url == new.url
        }
    }
}

class GitHubRepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun render(item: GitHubRepository) {
        itemView.repositoryTitle.text = item.name
        itemView.repositoryUrl.text = item.url
    }

    fun showPlaceHolder() {
        itemView.repositoryTitle.text = "Loading..."
        itemView.repositoryUrl.text = "Loading..."
    }

}