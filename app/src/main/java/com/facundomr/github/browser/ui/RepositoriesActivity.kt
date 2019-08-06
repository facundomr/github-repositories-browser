package com.facundomr.github.browser.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facundomr.github.browser.R
import com.facundomr.github.browser.ReposByUserQuery

class RepositoriesActivity : AppCompatActivity() {

    private lateinit var viewModel: RepositoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(RepositoriesViewModel::class.java)

        viewModel.repositories.observe(this, Observer<List<ReposByUserQuery.Node>> {
            Toast.makeText(applicationContext, "repositories count: ${it.size}", Toast.LENGTH_LONG).show()
        })

        viewModel.searchRepos("facundomr")
    }

}