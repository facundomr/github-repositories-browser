package com.facundomr.github.browser.repository.pagination

import androidx.paging.DataSource
import com.facundomr.github.browser.ReposByUserQuery
import androidx.lifecycle.MutableLiveData



class PaginationDataSourceFactory(val username: String) : DataSource.Factory<String, ReposByUserQuery.Node>() {

    val mutableLiveData = MutableLiveData<PaginationDataSource>()

    override fun create(): DataSource<String, ReposByUserQuery.Node> {

        val datasource = PaginationDataSource(username)
        mutableLiveData.postValue(datasource)
        return datasource
    }

}