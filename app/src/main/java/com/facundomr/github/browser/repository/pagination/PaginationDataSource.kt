package com.facundomr.github.browser.repository.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.facundomr.github.browser.repository.DataRepository
import com.facundomr.github.browser.ui.model.GitHubRepository
import com.facundomr.github.browser.ui.repositories.UserRepositoriesViewModel.RepositoriesViewState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PaginationDataSource(private val username: String, private val repository: DataRepository) : PageKeyedDataSource<String, GitHubRepository>() {

    val viewState = MutableLiveData<RepositoriesViewState>()

    override fun loadInitial( params: LoadInitialParams<String>, callback: LoadInitialCallback<String, GitHubRepository>) {

        viewState.postValue(RepositoriesViewState.SEARCHING)

        GlobalScope.launch {

            try {

                val result = repository.searchRepositories(user = username, limit = params.requestedLoadSize)!!

                if (result.totalCount == 0) {
                    viewState.postValue(RepositoriesViewState.EMPTY_STATE)
                } else {
                    callback.onResult(result.repositories, 0, result.totalCount, result.previousPageKey, result.nextPageKey)
                    viewState.postValue(RepositoriesViewState.OK)
                }

            } catch (t: Throwable) {
                viewState.postValue(RepositoriesViewState.ERROR_ON_FIRST_PAGE)
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, GitHubRepository>) {

        GlobalScope.launch {

            try {

                val result = repository.searchRepositories(user = username,
                    limit = params.requestedLoadSize,
                    nextPageKey = params.key)

                callback.onResult(result.repositories, result.nextPageKey)

            } catch (t: Throwable) {
                viewState.postValue(RepositoriesViewState.ERROR_WITH_NEXT_PAGE)
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, GitHubRepository>) {

        GlobalScope.launch {

            try {

                val result = repository.searchRepositories(user = username,
                    limit = params.requestedLoadSize,
                    previousPageKey = params.key)

                callback.onResult(result.repositories, result.previousPageKey)

            } catch (t: Throwable) {
                viewState.postValue(RepositoriesViewState.ERROR_WITH_NEXT_PAGE)
            }
        }
    }
}