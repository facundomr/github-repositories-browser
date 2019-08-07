package com.facundomr.github.browser.repository.pagination

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PageKeyedDataSource
import com.apollographql.apollo.exception.ApolloException
import com.facundomr.github.browser.repository.DataRepository
import com.facundomr.github.browser.ui.model.GitHubRepository
import com.facundomr.github.browser.ui.model.GitHubResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import com.facundomr.github.browser.ui.repositories.UserRepositoriesViewModel.RepositoriesViewState.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers

class PaginationDataSourceTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var pagination : PaginationDataSource
    lateinit var dataRepository: DataRepository

    lateinit var initialParamsMock : PageKeyedDataSource.LoadInitialParams<String>
    lateinit var initialCallbackMock: PageKeyedDataSource.LoadInitialCallback<String, GitHubRepository>

    lateinit var paramsMock : PageKeyedDataSource.LoadParams<String>
    lateinit var callbackMock: PageKeyedDataSource.LoadCallback<String, GitHubRepository>

    lateinit var githubResponse : GitHubResponse

    @Before
    fun setup() {

        dataRepository = mock(DataRepository::class.java)
        pagination = PaginationDataSource("toptal", dataRepository)

        initialParamsMock = mock()
        initialCallbackMock = mock()

        paramsMock = mock()
        callbackMock = mock()
    }

    @Test
    fun whenLoadingInitialDataAndTotalCountIsZeroThenTheFinalStateIsEMPTY() {
        runBlocking {
            GlobalScope.launch {

                githubResponse = GitHubResponse(ArrayList(), 0, null, null)

                pagination.loadInitial(initialParamsMock, initialCallbackMock)

                Assert.assertEquals(EMPTY_STATE, pagination.viewState.value)
            }
        }
    }

    @Test
    fun whenLoadingInitialDataAndTotalCountIsntZeroThenTheFinalStateIsOK() {
        runBlocking {
            GlobalScope.launch {

                githubResponse = GitHubResponse(ArrayList(), 100, null, null)

                pagination.loadInitial(initialParamsMock, initialCallbackMock)

                Assert.assertEquals(OK, pagination.viewState.value)
            }
        }
    }

    @Test
    fun whenLoadingInitialDataAnAnExceptionOccursThenTheFinalStateIsERROR_ON_FIRST_PAGE() {
        runBlocking {
            GlobalScope.launch {

                `when`(dataRepository.searchRepositories(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenThrow(ApolloException(""))
                githubResponse = GitHubResponse(ArrayList(), 100, null, null)

                pagination.loadInitial(initialParamsMock, initialCallbackMock)

                Assert.assertEquals(ERROR_ON_FIRST_PAGE, pagination.viewState.value)
            }
        }
    }

    @Test
    fun whenLoadingAfterDataAnAnExceptionOccursThenTheFinalStateIsERROR_ON_FIRST_PAGE() {
        runBlocking {
            GlobalScope.launch {

                `when`(dataRepository.searchRepositories(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenThrow(ApolloException(""))
                githubResponse = GitHubResponse(ArrayList(), 100, null, null)

                pagination.loadAfter(paramsMock, callbackMock)

                Assert.assertEquals(ERROR_WITH_NEXT_PAGE, pagination.viewState.value)
            }
        }
    }

    @Test
    fun whenLoadingBeforeDataAnAnExceptionOccursThenTheFinalStateIsERROR_ON_FIRST_PAGE() {
        runBlocking {
            GlobalScope.launch {

                `when`(dataRepository.searchRepositories(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenThrow(ApolloException(""))
                githubResponse = GitHubResponse(ArrayList(), 100, null, null)

                pagination.loadBefore(paramsMock, callbackMock)

                Assert.assertEquals(ERROR_WITH_NEXT_PAGE, pagination.viewState.value)
            }
        }
    }
}

inline fun <reified T: Any> mock() = mock(T::class.java)