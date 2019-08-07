package com.facundomr.github.browser.ui.repositories

import com.facundomr.github.browser.ui.model.GitHubRepository
import org.junit.Assert
import org.junit.Test

class UserRepositoriesViewModelTest {

    private val viewModel = UserRepositoriesViewModel("facu")

    @Test
    fun ifTheRepositoryIsNullThenShouldntNavigateToDetail() {

        Assert.assertFalse(viewModel.shouldNavigateToDetail(null))
    }

    @Test
    fun ifTheRepositoryIsNotNullThenShouldNavigateToDetail() {

        val repository = GitHubRepository(name = "GitHub Repository Browser", url="",
            closedPrs = 1, openPrs = 2,
            closedIssues = 3,openIssues = 4)

        Assert.assertTrue(viewModel.shouldNavigateToDetail(repository))
    }

}