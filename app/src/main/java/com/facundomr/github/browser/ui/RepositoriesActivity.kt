package com.facundomr.github.browser.ui

import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facundomr.github.browser.R
import com.facundomr.github.browser.ReposByUserQuery
import android.app.Activity
import android.view.inputmethod.InputMethodManager


class RepositoriesActivity : AppCompatActivity() {

    private lateinit var viewModel: RepositoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)
        viewModel = ViewModelProviders.of(this).get(RepositoriesViewModel::class.java)

        viewModel.viewState.observe(this, Observer {
            Toast.makeText(applicationContext, "state: ${it.name}", Toast.LENGTH_SHORT).show()
        })

        viewModel.repositories.observe(this, Observer<List<ReposByUserQuery.Node>> {
            Toast.makeText(applicationContext, "repositories count: ${it.size}", Toast.LENGTH_LONG).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_search, menu)

        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(menuItem) as SearchView

        searchView.queryHint = getString(R.string.enter_user_name)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchRepositories(query!!)

                val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchView.windowToken, 0)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

}