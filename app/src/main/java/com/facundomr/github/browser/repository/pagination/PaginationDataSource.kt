package com.facundomr.github.browser.repository.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.facundomr.github.browser.ReposByUserQuery
import com.facundomr.github.browser.repository.DataRepository
import com.facundomr.github.browser.ui.repositories.UserRepositoriesViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PaginationDataSource(val username: String) : PageKeyedDataSource<String, ReposByUserQuery.Node>() {

    private val repository : DataRepository by lazy {
        DataRepository()
    }

    val networkState = MutableLiveData<UserRepositoriesViewModel.RepositoriesViewState>()

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, ReposByUserQuery.Node>) {

        networkState.postValue(UserRepositoriesViewModel.RepositoriesViewState.SEARCHING)

        GlobalScope.launch {

            val result = repository.searchRepositories(user = username, limit = params.requestedLoadSize)

            val list = result.data()?.user()?.repositories()?.nodes()!!
            val before = result.data()?.user()?.repositories()?.pageInfo()?.startCursor()
            val after = result.data()?.user()?.repositories()?.pageInfo()?.endCursor()

            val totalCount = result.data()?.user()?.repositories()?.totalCount()

            callback.onResult(list!!, 0, totalCount!!, before, after)
            networkState.postValue(UserRepositoriesViewModel.RepositoriesViewState.OK)
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, ReposByUserQuery.Node>) {

        GlobalScope.launch {

            val result = repository.searchRepositories(user = username,
                limit = params.requestedLoadSize,
                nextPageKey = params.key)

            val list = result.data()?.user()?.repositories()?.nodes()!!
            val after = result.data()?.user()?.repositories()?.pageInfo()?.endCursor()

            callback.onResult(list, after)
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, ReposByUserQuery.Node>) {

        GlobalScope.launch {

            val result = repository.searchRepositories(user = username,
                limit = params.requestedLoadSize,
                previousPageKey = params.key)

            val list = result.data()?.user()?.repositories()?.nodes()!!
            val before = result.data()?.user()?.repositories()?.pageInfo()?.startCursor()

            callback.onResult(list, before)
        }
    }

}