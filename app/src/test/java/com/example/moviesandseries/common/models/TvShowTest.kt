package com.example.moviesandseries.common.models

import com.example.moviesandseries.common.dto.TvShowDto
import com.example.moviesandseries.common.enums.ShowType
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class TvShowTest {

    private lateinit var sut: TvShow
    private val dto = TvShowDto(1, "poster", _overview = "blabla", _title = "title", _mediaType = "tv")

    @Before
    fun setUp() {
        sut = TvShow(dto)
    }

    @Test
    fun okConstructorTest() {
        val expected = TvShow(1, "title", "poster", "blabla", ShowType.SERIES)
        assertEquals(expected, sut)
    }

    @Test
    fun failConstructorTest() {
        val expected = TvShow(1, "title1", "poster1", "blabla1", ShowType.MOVIE)
        assertNotEquals(expected, sut)
    }

    @Test
    fun okDefaultConstructorTest() {
        sut = TvShow()
        assertEquals(-1, sut.id)
        assertEquals("", sut.title)
        assertEquals("", sut.posterImage)
        assertEquals("", sut.overView)
        assertEquals(ShowType.UNDEFINED, sut.mediaType)
        assertEquals("", sut.releaseDate)
        assertEquals(0f, sut.voteAverage)
        assertFalse(sut.isFavourite)
    }
}