package com.facundomr.github.browser.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facundomr.github.browser.ui.model.GitHubRepository

class GitHubRepositoryDetailViewModel(gitHubRepository: GitHubRepository) : ViewModel() {

    val title = MutableLiveData(gitHubRepository.name)
    val openPrs = MutableLiveData(gitHubRepository.openPrs.toString())
    val closedPrs = MutableLiveData(gitHubRepository.closedPrs.toString())
    val openIssues = MutableLiveData(gitHubRepository.openIssues.toString())
    val closedIssues = MutableLiveData(gitHubRepository.closedIssues.toString())

}