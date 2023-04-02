package com.example.moviesandseries.common.dto

import com.google.gson.annotations.SerializedName

data class TvShowDto(
    @SerializedName("id") private val _id: Int? = -1,
    @SerializedName("poster_path") private val _posterImage: String? = "",
    @SerializedName("backdrop_path") private val _backgroundImage: String? = "",
    @SerializedName("overview") private val _overview: String? = "",
    @SerializedName("name", alternate = ["title"]) private val _title: String? = "",
    @SerializedName("media_type") private val _mediaType: String? = "",
    @SerializedName("first_air_date", alternate = ["release_date"]) private val _releaseDate: String? = "",
    @SerializedName("vote_average") private val _voteAverage: Float? = 0f
) {
    val id: Int
        get() = _id ?: -1

    val posterImage: String
        get() = _posterImage ?: ""

    val backgroundImage: String
        get() = _backgroundImage ?: ""

    val overview: String
        get() = _overview ?: ""

    val title: String
        get() = _title ?: ""

    val mediaType: String
        get() = _mediaType ?: ""

    val releaseDate: String
        get() = _releaseDate ?: ""

    val voteAverage: Float
        get() = _voteAverage ?: 0f

}