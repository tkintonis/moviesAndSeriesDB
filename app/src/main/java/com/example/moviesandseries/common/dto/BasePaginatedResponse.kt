package com.example.moviesandseries.common.dto

import com.google.gson.annotations.SerializedName

data class BasePaginatedResponse<T>(
    @SerializedName("page") private val _pageNumber: Int? = 0,
    @SerializedName("results") private val _results: List<T>? = emptyList(),
    @SerializedName("total_pages") private val _totalPages: Int? = 0
) {

    val pageNumber: Int
        get() = _pageNumber ?: 0

    val tvShowsList: List<T>
        get() = _results ?: emptyList()

    val totalPages: Int
        get() = _totalPages ?: 0
}