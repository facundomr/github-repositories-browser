package com.facundomr.github.browser.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facundomr.github.browser.R
import com.facundomr.github.browser.ui.repositories.UserRepositoriesActivity
import kotlinx.android.synthetic.main.activity_user_search.*
import com.facundomr.github.browser.ui.search.UserSearchViewModel.SearchViewState.*

class UserSearchActivity : AppCompatActivity() {

    private lateinit var viewModel: UserSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)

        viewModel = ViewModelProviders.of(this).get(UserSearchViewModel::class.java)

        viewModel.viewState.observe(this, Observer {

            when(it) {
                EMPTY_INPUT -> inputLayout.error = getString(R.string.required)
                READY -> inputLayout.error = null
            }

        })

        username.addTextChangedListener(object:TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.textChanged(p0.toString())
            }

        })

        search.setOnClickListener {

            if (viewModel.userNameIsValid()) {
                val intent = Intent(UserSearchActivity@this, UserRepositoriesActivity::class.java)
                intent.putExtra("username", viewModel.username)
                startActivity(intent)
            }
        }

    }

}
