package com.facundomr.github.browser.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facundomr.github.browser.ReposByUserQuery
import com.facundomr.github.browser.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepositoriesViewModel : ViewModel() {

    private val repository : GithubRepository by lazy {
        GithubRepository()
    }

    val repositories: MutableLiveData<List<ReposByUserQuery.Node>> = MutableLiveData()
    val viewState = MutableLiveData(RepositoriesViewState.READY)
    val currentUser = MutableLiveData<String>()

    fun searchRepositories(user: String) {

        currentUser.value = user
        viewState.value = RepositoriesViewState.SEARCHING

        viewModelScope.launch(Dispatchers.Main) {

            val userData = repository.searchRepositories(user)
            userData.repositories().nodes()?.let {
                repositories.value = it
                viewState.value = RepositoriesViewState.OK
            }
        }
    }

    enum class RepositoriesViewState {
        READY,
        OK,
        ERROR,
        SEARCHING
    }

}