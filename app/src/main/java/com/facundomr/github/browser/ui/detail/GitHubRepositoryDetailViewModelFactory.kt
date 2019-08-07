package com.facundomr.github.browser.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.facundomr.github.browser.ui.model.GitHubRepository

class GitHubRepositoryDetailViewModelFactory(val gitHubRepository: GitHubRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GitHubRepositoryDetailViewModel(gitHubRepository) as T
    }

}