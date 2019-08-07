package com.facundomr.github.browser.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facundomr.github.browser.R
import com.facundomr.github.browser.ui.model.GitHubRepository
import kotlinx.android.synthetic.main.activity_github_repository_detail.*


class GitHubRepositoryDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: GitHubRepositoryDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_repository_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = intent.extras?.get("repository") as GitHubRepository
        viewModel = ViewModelProviders.of(this, GitHubRepositoryDetailViewModelFactory(repository))
                                      .get(GitHubRepositoryDetailViewModel::class.java)

        viewModel.title.observe(this, Observer {
            supportActionBar?.title = it
        })

        viewModel.openPrs.observe(this, Observer {
            openPrs.text = it
        })

        viewModel.closedPrs.observe(this, Observer {
            closedPrs.text = it
        })

        viewModel.openIssues.observe(this, Observer {
            openIssues.text = it
        })

        viewModel.closedIssues.observe(this, Observer {
            closedIssues.text = it
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}