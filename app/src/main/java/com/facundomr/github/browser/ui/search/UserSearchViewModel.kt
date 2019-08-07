package com.facundomr.github.browser.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserSearchViewModel : ViewModel() {

    val viewState = MutableLiveData(SearchViewState.READY)
    var username = ""

    fun userNameIsValid() : Boolean {
        val valid = !username.isEmpty()

        if (!valid) {
            viewState.value = SearchViewState.EMPTY_INPUT
        } else {
            viewState.value = SearchViewState.READY
        }

        return valid
    }

    fun textChanged(username: String) {
        this.username = username
        userNameIsValid()
    }

    enum class SearchViewState {
        READY,
        EMPTY_INPUT
    }

}