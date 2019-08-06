package com.facundomr.github.browser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import com.facundomr.github.browser.BuildConfig
import com.facundomr.github.browser.R
import com.facundomr.github.browser.ReposByUserQuery
import com.facundomr.github.browser.type.CustomType
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class RepositoriesActivity : AppCompatActivity() {

    lateinit var viewModel: RepositoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(RepositoriesViewModel::class.java)

        val user = "facundomr"

        val query = ReposByUserQuery.builder()
                                    .username(user)
                                    .build()

        val httpClient = OkHttpClient.Builder()
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addNetworkInterceptor(NetworkInterceptor())
            .build()

        ApolloClient.builder()
                    .serverUrl("https://api.github.com/graphql")
                    .okHttpClient(httpClient)
                    .addCustomTypeAdapter(CustomType.URI, object: CustomTypeAdapter<String> {
                        override fun encode(value: String): CustomTypeValue<*> {
                            return CustomTypeValue.GraphQLString(value)
                        }

                        override fun decode(value: CustomTypeValue<*>): String {
                            return value.value.toString()
                        }

                    })
                    .build()
                    .query(query)
                    .enqueue(object:ApolloCall.Callback<ReposByUserQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.e("TAG", "onFailure: $e")
                }

                override fun onResponse(response: Response<ReposByUserQuery.Data>) {
                    Log.i("TAG", "onResponse: $response")
                    Log.i("TAG", "onResponse: user $user repositories count: ${response.data()?.user()?.repositories()?.totalCount()}")

                    response.data()?.user()?.repositories()?.nodes()?.forEach {
                        Log.i("REPO", "Name: ${it.name()}, URL: ${it.url()}")
                    }

                }
            })
    }

    private class NetworkInterceptor: Interceptor {

        override fun intercept(chain: Interceptor.Chain?): okhttp3.Response {
            return chain!!.proceed(chain.request().newBuilder().header("Authorization", "Bearer ${BuildConfig.GITHUB_TOKEN}").build())
        }
    }
}