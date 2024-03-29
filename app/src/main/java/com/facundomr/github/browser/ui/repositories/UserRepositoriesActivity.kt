package com.facundomr.github.browser.ui.repositories

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facundomr.github.browser.R
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.facundomr.github.browser.ui.detail.GitHubRepositoryDetailActivity
import com.facundomr.github.browser.ui.repositories.UserRepositoriesViewModel.RepositoriesViewState.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_user_repositories.*

class UserRepositoriesActivity : AppCompatActivity() {

    private lateinit var viewModel: UserRepositoriesViewModel

    private val adapter = GitHubRepositoriesAdapter(clickListener = {

        if (viewModel.shouldNavigateToDetail(it)) {

            val intent = Intent(UserRepositoriesActivity@this, GitHubRepositoryDetailActivity::class.java)
            intent.putExtra("repository", it)
            startActivity(intent)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_repositories)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        if(it != ERROR_WITH_NEXT_PAGE) {
            searching.visibility = View.GONE
            recycler.visibility = View.GONE
            error.visibility = View.GONE
            emptyState.visibility = View.GONE
        }

        when (it) {
            SEARCHING -> searching.visibility = View.VISIBLE
            OK -> recycler.visibility = View.VISIBLE
            ERROR_ON_FIRST_PAGE -> error.visibility = View.VISIBLE
            EMPTY_STATE -> emptyState.visibility = View.VISIBLE
            ERROR_WITH_NEXT_PAGE -> Snackbar.make(recycler, R.string.error_with_next_page, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}