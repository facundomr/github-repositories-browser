package com.facundomr.github.browser.repository

import com.facundomr.github.browser.ui.model.GitHubRepository
import com.facundomr.github.browser.ui.model.GitHubResponse

class DataRepository(private val datasource: GitHubGraphQLDataSource) {

    suspend fun searchRepositories(user: String,
                           limit: Int,
                           previousPageKey: String? = null,
                           nextPageKey: String? = null) : GitHubResponse {

        val graphResponse =  datasource.searchRepositories(user, limit, previousPageKey, nextPageKey)

        val repositories = ArrayList<GitHubRepository>()

        graphResponse.data()?.user()?.repositories()?.nodes()?.forEach {

            val githubRepository = GitHubRepository(name = it.name(), url = it.url().toString(),
                closedIssues = it.closedIssues().totalCount(), openIssues = it.openIssues().totalCount(),
                closedPrs =  it.closedPrs().totalCount(), openPrs =  it.openPrs().totalCount())

            repositories.add(githubRepository)
        }

        return GitHubResponse(repositories = repositories, totalCount = graphResponse?.data()?.user()?.repositories()?.totalCount()!!,
                              previousPageKey = graphResponse.data()?.user()?.repositories()?.pageInfo()?.startCursor(),
                              nextPageKey = graphResponse.data()?.user()?.repositories()?.pageInfo()?.endCursor())
    }

}