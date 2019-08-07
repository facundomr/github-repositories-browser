package com.facundomr.github.browser.ui.model

data class GitHubResponse(val repositories: List<GitHubRepository>,
                          val totalCount: Int = 0,
                          val previousPageKey: String?, val nextPageKey: String?)