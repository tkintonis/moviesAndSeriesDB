package com.example.moviesandseries.common.enums

import com.example.moviesandseries.R
import com.example.moviesandseries.common.constants.Constants
import org.junit.Assert.*
import org.junit.Test

class ShowTypeTest {

    private val tvValue = Constants.TV_VALUE
    private val movieValue = Constants.MOVIE_VALUE

    private lateinit var sut: ShowType

    @Test
    fun givenThat_value_is_Movie_ShowTypeShould() {
        sut = ShowType.create(movieValue)
        assertEquals(R.string.movie_type, sut.typeText)
        assertEquals(R.color.marsDark, sut.containerColour)
        assertEquals(R.color.white, sut.textColour)
    }

    @Test
    fun givenThat_value_is_TV_ShowTypeShould() {
        sut = ShowType.create(tvValue)
        assertEquals(R.string.series_type, sut.typeText)
        assertEquals(R.color.white, sut.containerColour)
        assertEquals(R.color.marsDark, sut.textColour)
    }

    @Test
    fun givenThat_value_is_Undefined_ShowTypeShould() {
        sut = ShowType.create("")
        assertEquals(0, sut.typeText)
        assertEquals(0, sut.containerColour)
        assertEquals(0, sut.textColour)
    }
}