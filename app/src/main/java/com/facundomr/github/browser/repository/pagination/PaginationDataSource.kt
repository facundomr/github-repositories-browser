package com.facundomr.github.browser.repository.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.facundomr.github.browser.ReposByUserQuery
import com.facundomr.github.browser.repository.DataRepository
import com.facundomr.github.browser.ui.repositories.UserRepositoriesViewModel.RepositoriesViewState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PaginationDataSource(val username: String) : PageKeyedDataSource<String, ReposByUserQuery.Node>() {

    private val repository : DataRepository by lazy {
        DataRepository()
    }

    val viewState = MutableLiveData<RepositoriesViewState>()

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, ReposByUserQuery.Node>) {

        viewState.postValue(RepositoriesViewState.SEARCHING)

        GlobalScope.launch {

            try {

                val result = repository.searchRepositories(user = username, limit = params.requestedLoadSize)!!

                if (result.data()?.user() == null) {
                    viewState.postValue(RepositoriesViewState.USER_NOT_FOUND)
                } else if (result.data()?.user()?.repositories()?.nodes()?.isEmpty()!!) {
                    viewState.postValue(RepositoriesViewState.EMPTY_STATE)

                } else {

                    val list = result.data()?.user()?.repositories()?.nodes()!!
                    val before = result.data()?.user()?.repositories()?.pageInfo()?.startCursor()
                    val after = result.data()?.user()?.repositories()?.pageInfo()?.endCursor()

                    val totalCount = result.data()?.user()?.repositories()?.totalCount()

                    callback.onResult(list!!, 0, totalCount!!, before, after)
                    viewState.postValue(RepositoriesViewState.OK)
                }

            } catch (t: Throwable) {
                viewState.postValue(RepositoriesViewState.ERROR_ON_FIRST_PAGE)
            }

        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, ReposByUserQuery.Node>) {

        GlobalScope.launch {

            try {

                val result = repository.searchRepositories(user = username,
                    limit = params.requestedLoadSize,
                    nextPageKey = params.key)

                val list = result.data()?.user()?.repositories()?.nodes()!!
                val after = result.data()?.user()?.repositories()?.pageInfo()?.endCursor()

                callback.onResult(list, after)

            } catch (t: Throwable) {
                viewState.postValue(RepositoriesViewState.ERROR_WITH_NEXT_PAGE)
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, ReposByUserQuery.Node>) {

        GlobalScope.launch {

            try {

                val result = repository.searchRepositories(user = username,
                    limit = params.requestedLoadSize,
                    previousPageKey = params.key)

                val list = result.data()?.user()?.repositories()?.nodes()!!
                val before = result.data()?.user()?.repositories()?.pageInfo()?.startCursor()

                callback.onResult(list, before)

            } catch (t: Throwable) {
                viewState.postValue(RepositoriesViewState.ERROR_WITH_NEXT_PAGE)
            }
        }
    }

}