package com.example.moviesandseries.common.enums

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.moviesandseries.R
import com.example.moviesandseries.common.constants.Constants

enum class ShowType(
    private val value: String,
    @StringRes val typeText: Int,
    @ColorRes val containerColour: Int,
    @ColorRes val textColour: Int
) {
    MOVIE(Constants.MOVIE_VALUE, R.string.movie_type, R.color.marsDark, R.color.white),
    SERIES(Constants.TV_VALUE, R.string.series_type, R.color.white, R.color.marsDark),
    UNDEFINED("", 0, 0, 0);

    companion object {
        private val map = values().associateBy(ShowType::value)
        fun create(type: String?): ShowType = map[type] ?: UNDEFINED
    }
}