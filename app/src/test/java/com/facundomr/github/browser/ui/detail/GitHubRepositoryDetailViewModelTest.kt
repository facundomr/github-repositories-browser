package com.facundomr.github.browser.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.facundomr.github.browser.ui.model.GitHubRepository
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class GitHubRepositoryDetailViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun whenCreatingTheViewModelTheObserversAreInTheCorrectState() {

        val repository = GitHubRepository(name = "GitHub Repository Browser", url="",
                                          closedPrs = 1, openPrs = 2,
                                          closedIssues = 3,openIssues = 4)

        val viewModel = GitHubRepositoryDetailViewModel(repository)

        Assert.assertEquals("GitHub Repository Browser", viewModel.title.value)
        Assert.assertEquals("1", viewModel.closedPrs.value)
        Assert.assertEquals("2", viewModel.openPrs.value)
        Assert.assertEquals("3", viewModel.closedIssues.value)
        Assert.assertEquals("4", viewModel.openIssues.value)
    }
}