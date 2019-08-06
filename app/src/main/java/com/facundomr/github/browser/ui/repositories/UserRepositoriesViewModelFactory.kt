package com.facundomr.github.browser.ui.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserRepositoriesViewModelFactory(val username: String): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserRepositoriesViewModel(username) as T
    }
}