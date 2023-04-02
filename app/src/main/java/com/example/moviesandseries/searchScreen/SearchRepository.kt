package com.example.moviesandseries.searchScreen

import android.content.res.Resources
import android.net.ConnectivityManager
import com.example.moviesandseries.R
import com.example.moviesandseries.common.dto.BasePaginatedResponse
import com.example.moviesandseries.common.dto.TvShowDto
import com.example.moviesandseries.common.helpers.DispatcherProvider
import com.example.moviesandseries.common.models.TvShow
import com.example.moviesandseries.network.NetworkServiceApi
import com.example.moviesandseries.network.Resource
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: NetworkServiceApi,
    private val connectivityManager: ConnectivityManager,
    private val dispatcherProvider: DispatcherProvider,
    private val resources: Resources
) : SearchRepositoryIf {

    override suspend fun searchTvShow(searchQuery: String, pageNumber: Int): Resource<BasePaginatedResponse<TvShowDto>> {
        if (!hasInternetConnectivity()) return Resource.Error(Exception(resources.getString(R.string.internet_connection_message)))

        return withContext(dispatcherProvider.ioDispatcher) {
            try {
                withTimeout(TIMEOUT_THRESHOLD_MILLIS) {
                    val response = api.searchTvShow(searchQuery = searchQuery, pageNumber = pageNumber)
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        Resource.Success(result)
                    } else {
                        Resource.Error(Exception(response.message()))
                    }
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    private fun hasInternetConnectivity(): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    companion object {
        private const val TIMEOUT_THRESHOLD_MILLIS = Long.MAX_VALUE
    }
}