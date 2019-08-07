package com.facundomr.github.browser.repository.pagination

import androidx.paging.DataSource
import androidx.lifecycle.MutableLiveData
import com.facundomr.github.browser.ui.model.GitHubRepository

class PaginationDataSourceFactory(val username: String) : DataSource.Factory<String, GitHubRepository>() {

    val mutableLiveData = MutableLiveData<PaginationDataSource>()

    override fun create(): DataSource<String, GitHubRepository> {

        val datasource = PaginationDataSource(username)
        mutableLiveData.postValue(datasource)
        return datasource
    }
}