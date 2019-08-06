package com.facundomr.github.browser.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import com.facundomr.github.browser.BuildConfig
import com.facundomr.github.browser.ReposByUserQuery
import com.facundomr.github.browser.type.CustomType
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class GitHubGraphQLDataSource {

    private val httpClient = OkHttpClient.Builder()
        .writeTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addNetworkInterceptor(NetworkInterceptor())
        .build()

    private val client: ApolloClient by lazy {
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
    }

    suspend fun searchRepositories(user: String) : Response<ReposByUserQuery.Data> {

        val repositoriesQuery = ReposByUserQuery.builder()
            .username(user)
            .build()

        return client.query(repositoriesQuery)
            .toDeferred()
            .await()
    }

}

private class NetworkInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain?): okhttp3.Response {
        return chain!!.proceed(chain.request().newBuilder().header("Authorization", "Bearer ${BuildConfig.GITHUB_TOKEN}").build())
    }
}