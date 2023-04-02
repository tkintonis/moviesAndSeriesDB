package com.example.moviesandseries.common.enums

import androidx.annotation.StringRes
import com.example.moviesandseries.R
import com.example.moviesandseries.common.constants.Constants

enum class ShowType(private val value: String, @StringRes  val typeText: Int) {
    MOVIE(Constants.MOVIE_VALUE, R.string.movie_type),
    SERIES(Constants.TV_VALUE, R.string.series_type),
    UNDEFINED("", 0);

    companion object {
        private val map = values().associateBy(ShowType::value)
        fun create(type: String?): ShowType = map[type] ?: UNDEFINED
    }
}