package com.facundomr.github.browser.repository

import com.facundomr.github.browser.ReposByUserQuery
import com.facundomr.github.browser.ui.model.GitHubRepository
import com.facundomr.github.browser.ui.model.GitHubResponse

class DataRepository(private val datasource: GitHubGraphQLDataSource) {

    suspend fun searchRepositories(user: String,
                           limit: Int,
                           previousPageKey: String? = null,
                           nextPageKey: String? = null) : GitHubResponse {

        val graphResponse =  datasource.searchRepositories(user, limit, previousPageKey, nextPageKey)

        val repositories = ArrayList<GitHubRepository>()

        val searchObject = graphResponse.data()?.search()
        searchObject?.nodes()?.forEach {

            it as ReposByUserQuery.AsRepository

            val githubRepository = GitHubRepository(name = it.name(), url = it.url().toString(),
                closedIssues = it.closedIssues().totalCount(), openIssues = it.openIssues().totalCount(),
                closedPrs =  it.closedPrs().totalCount(), openPrs =  it.openPrs().totalCount())

            repositories.add(githubRepository)
        }

        val nextPageKey = if (searchObject?.pageInfo()?.hasNextPage()!!) searchObject.pageInfo().endCursor() else null
        val previousPageKey = if (searchObject.pageInfo().hasPreviousPage()) searchObject.pageInfo().endCursor() else null

        return GitHubResponse(repositories = repositories, totalCount = searchObject.repositoryCount(),
                              previousPageKey = previousPageKey,
                              nextPageKey = nextPageKey
        )
    }

}