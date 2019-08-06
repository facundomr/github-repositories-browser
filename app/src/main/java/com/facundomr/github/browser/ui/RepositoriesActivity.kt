package com.facundomr.github.browser.ui

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facundomr.github.browser.R
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.facundomr.github.browser.ui.RepositoriesViewModel.RepositoriesViewState.*
import kotlinx.android.synthetic.main.activity_repositories.*

class RepositoriesActivity : AppCompatActivity() {

    private lateinit var viewModel: RepositoriesViewModel

    private lateinit var searchView : SearchView

    private val adapter = GitHubRepositoriesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)
        viewModel = ViewModelProviders.of(this).get(RepositoriesViewModel::class.java)

        viewModel.viewState.observe(this, Observer {
            handleViewsVisibility(it)
        })

        viewModel.repositories.observe(this, Observer {
            adapter.submitList(it)
        })

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun handleViewsVisibility(it: RepositoriesViewModel.RepositoriesViewState?) {
        searching.visibility = View.GONE
        recycler.visibility = View.GONE

        when (it) {
            SEARCHING -> searching.visibility = View.VISIBLE
            OK -> recycler.visibility = View.VISIBLE
        }
    }

}