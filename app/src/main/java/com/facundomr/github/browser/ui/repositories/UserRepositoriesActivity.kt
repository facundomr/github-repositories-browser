package com.facundomr.github.browser.ui.repositories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facundomr.github.browser.R
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.facundomr.github.browser.ui.repositories.UserRepositoriesViewModel.RepositoriesViewState.*
import kotlinx.android.synthetic.main.activity_user_repositories.*

class UserRepositoriesActivity : AppCompatActivity() {

    private lateinit var viewModel: UserRepositoriesViewModel

    private val adapter = GitHubRepositoriesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_repositories)

        val username = intent.extras?.getString("username", "")!!
        viewModel = ViewModelProviders.of(this, UserRepositoriesViewModelFactory(username)).get(UserRepositoriesViewModel::class.java)

        viewModel.viewState.observe(this, Observer {
            handleViewsVisibility(it)
        })

        viewModel.repositories.observe(this, Observer {
            adapter.submitList(it)
        })

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun handleViewsVisibility(it: UserRepositoriesViewModel.RepositoriesViewState?) {
        searching.visibility = View.GONE
        recycler.visibility = View.GONE

        when (it) {
            SEARCHING -> searching.visibility = View.VISIBLE
            OK -> recycler.visibility = View.VISIBLE
        }
    }

}