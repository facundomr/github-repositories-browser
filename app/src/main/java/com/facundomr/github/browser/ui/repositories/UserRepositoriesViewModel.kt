package com.facundomr.github.browser.ui.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.facundomr.github.browser.ReposByUserQuery
import com.facundomr.github.browser.repository.pagination.PaginationDataSourceFactory
import androidx.lifecycle.Transformations

class UserRepositoriesViewModel(val username: String) : ViewModel() {

    var repositories: LiveData<PagedList<ReposByUserQuery.Node>> = MutableLiveData()
    lateinit var viewState: LiveData<RepositoriesViewState>

    init {
        createFactory()
    }

    private fun createFactory() {
        val dataFactory = PaginationDataSourceFactory(username)

        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(true)
            .build()

        viewState = Transformations.switchMap(dataFactory.mutableLiveData) { dataSource -> dataSource.networkState }

        repositories = LivePagedListBuilder(dataFactory, config).build()
    }

    enum class RepositoriesViewState {
        OK,
        ERROR,
        SEARCHING
    }

}