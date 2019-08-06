package com.facundomr.github.browser.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders
import com.facundomr.github.browser.R
import com.facundomr.github.browser.ui.repositories.UserRepositoriesActivity
import kotlinx.android.synthetic.main.activity_user_search.*

class UserSearchActivity : AppCompatActivity() {

    private lateinit var viewModel: UserSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)

        viewModel = ViewModelProviders.of(this).get(UserSearchViewModel::class.java)

        search.setOnClickListener {

            if (viewModel.isValid(username.text.toString())) {
                val intent = Intent(UserSearchActivity@this, UserRepositoriesActivity::class.java)
                intent.putExtra("username", username.text.toString())
                startActivity(intent)
            }
        }

    }

}
