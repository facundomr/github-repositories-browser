package com.facundomr.github.browser.ui.model

import java.io.Serializable

data class GitHubRepository(val name: String,
                            val closedPrs: Int,
                            val openPrs: Int,
                            val closedIssues: Int,
                            val openIssues: Int) : Serializable