package com.facundomr.github.browser.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.facundomr.github.browser.ReposByUserQuery
import com.facundomr.github.browser.repository.pagination.PaginationDataSourceFactory
import androidx.lifecycle.Transformations

class RepositoriesViewModel : ViewModel() {

    private val user = "jakewharton"

    var repositories: LiveData<PagedList<ReposByUserQuery.Node>> = MutableLiveData()
    lateinit var viewState: LiveData<RepositoriesViewState>

    init {
        createFactory()
    }

    private fun createFactory() {
        val dataFactory = PaginationDataSourceFactory(user)

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