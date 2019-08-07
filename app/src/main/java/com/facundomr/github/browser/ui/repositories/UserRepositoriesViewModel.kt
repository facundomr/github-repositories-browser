package com.facundomr.github.browser.ui.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.facundomr.github.browser.repository.pagination.PaginationDataSourceFactory
import androidx.lifecycle.Transformations
import com.facundomr.github.browser.ui.model.GitHubRepository

class UserRepositoriesViewModel(val username: String) : ViewModel() {

    val title = MutableLiveData("$username repositories")
    var repositories: LiveData<PagedList<GitHubRepository>> = MutableLiveData()
    lateinit var viewState: LiveData<RepositoriesViewState>

    init {
        createFactory()
    }

    private fun createFactory() {
        val dataFactory = PaginationDataSourceFactory(username)

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setInitialLoadSizeHint(45)
            .setEnablePlaceholders(true)
            .build()

        viewState = Transformations.switchMap(dataFactory.mutableLiveData) { dataSource -> dataSource.viewState }

        repositories = LivePagedListBuilder(dataFactory, config).build()
    }

    fun shouldNavigateToDetail(gitHubRepository: GitHubRepository?) = (gitHubRepository != null)

    enum class RepositoriesViewState {
        OK,
        ERROR_ON_FIRST_PAGE,
        ERROR_WITH_NEXT_PAGE,
        SEARCHING,
        EMPTY_STATE
    }
}