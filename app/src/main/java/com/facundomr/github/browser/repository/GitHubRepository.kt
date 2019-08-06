package com.facundomr.github.browser.repository

import androidx.paging.PageKeyedDataSource
import com.apollographql.apollo.api.Response
import com.facundomr.github.browser.ReposByUserQuery

class GitHubRepository {

    private val datasource: GitHubGraphQLDataSource by lazy {
        GitHubGraphQLDataSource()
    }

    suspend fun searchRepositories(user: String,
                           limit: Int,
                           previousPageKey: String? = null,
                           nextPageKey: String? = null) : Response<ReposByUserQuery.Data> {

        return datasource.searchRepositories(user, limit, previousPageKey, nextPageKey)
    }

}