package com.facundomr.github.browser.ui.search

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.rules.TestRule
import org.junit.Rule

class UserSearchViewModelTest {

    @Rule @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: UserSearchViewModel

    @Before
    fun setup() {
        viewModel = UserSearchViewModel()
    }

    @Test
    fun whenTheViewModelIsCreatedTheUsernameItsEmptyAndTheStateIsReady() {

        Assert.assertEquals("", viewModel.username)
        Assert.assertEquals(UserSearchViewModel.SearchViewState.READY, viewModel.viewState.value)
    }

    @Test
    fun ifTheUserJustEntersSpacesThenTheInputIsInvalidAndTheStateIsEmpty() {

        viewModel.textChanged(" ")
        Assert.assertFalse(viewModel.userNameIsValid())
        Assert.assertEquals(UserSearchViewModel.SearchViewState.EMPTY_INPUT, viewModel.viewState.value)
    }

    @Test
    fun ifTheUserEntersAUsernameInTheCorrectFormatThenTheStateIsReadyAndTheUsernameIsValid() {

        viewModel.textChanged("toptal")
        Assert.assertTrue(viewModel.userNameIsValid())
        Assert.assertEquals(UserSearchViewModel.SearchViewState.READY, viewModel.viewState.value)
    }

    @Test
    fun ifTheUserEntersAUsernameWithSpacesThenTheStateIsReadyAndTheUsernameIsValidButTheUsernameIsTrimmed() {

        viewModel.textChanged(" toptal ")
        Assert.assertTrue(viewModel.userNameIsValid())
        Assert.assertEquals(UserSearchViewModel.SearchViewState.READY, viewModel.viewState.value)
        Assert.assertEquals("toptal", viewModel.username)
    }

}