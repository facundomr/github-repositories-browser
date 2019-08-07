package com.facundomr.github.browser.ui.repositories

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facundomr.github.browser.R
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.facundomr.github.browser.ui.detail.GitHubRepositoryDetailActivity
import com.facundomr.github.browser.ui.model.GitHubRepository
import com.facundomr.github.browser.ui.repositories.UserRepositoriesViewModel.RepositoriesViewState.*
import kotlinx.android.synthetic.main.activity_user_repositories.*
import kotlinx.android.synthetic.main.activity_user_search.*

class UserRepositoriesActivity : AppCompatActivity() {

    private lateinit var viewModel: UserRepositoriesViewModel

    private val adapter = GitHubRepositoriesAdapter(clickListener = {

        if (viewModel.shouldNavigateToDetail(it)) {

            val intent = Intent(UserRepositoriesActivity@this, GitHubRepositoryDetailActivity::class.java)

            val gitHubRepository = GitHubRepository(it!!.name(),
                it.closedPrs().totalCount(), it.openPrs().totalCount(),
                it.closedIssues().totalCount(), it.openIssues().totalCount())

            intent.putExtra("repository", gitHubRepository)
            startActivity(intent)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_repositories)

        val username = intent.extras?.getString("username", "")!!
        viewModel = ViewModelProviders.of(this, UserRepositoriesViewModelFactory(username))
                                      .get(UserRepositoriesViewModel::class.java)

        viewModel.title.observe(this, Observer {
            supportActionBar?.title = it
        })

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
        userNotFound.visibility = View.GONE
        recycler.visibility = View.GONE

        when (it) {
            SEARCHING -> searching.visibility = View.VISIBLE
            USER_NOT_FOUND -> userNotFound.visibility = View.VISIBLE
            OK -> recycler.visibility = View.VISIBLE

        }
    }

}