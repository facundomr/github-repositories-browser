package com.facundomr.github.browser.repository

import com.facundomr.github.browser.ReposByUserQuery

class GithubRepository {

    private val datasource: GitHubGraphQLDataSource by lazy {
        GitHubGraphQLDataSource()
    }

    suspend fun searchRepos(user: String) : ReposByUserQuery.User {
        return datasource.searchRepositories(user).data()?.user()!!
    }

}