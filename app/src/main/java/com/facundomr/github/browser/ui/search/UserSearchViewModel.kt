package com.facundomr.github.browser.ui.search

import androidx.lifecycle.ViewModel

class UserSearchViewModel : ViewModel() {

    fun isValid(username: String) = !username.isEmpty()

}