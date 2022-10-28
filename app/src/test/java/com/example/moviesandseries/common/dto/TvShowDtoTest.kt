package com.example.moviesandseries.common.dto

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class TvShowDtoTest {

    private val gson = Gson()

    @Test
    fun okTest() {
        val tvShowDto: TvShowDto = gson.fromJson(
            "{\n" + "  \"backdrop_path\": \"/pHBEWkI0s4DRheoVUmGDtzeqiMn.jpg\",\n" +
                    "  \"first_air_date\": \"2017-08-03\",\n" + "  \"id\": 74521,\n" +
                    "  \"media_type\": \"tv\",\n" +
                    "  \"name\": \"Impractical Jokers: After Party\",\n" + "  \"overview\": \"Joey Fatone hosts a behind the scenes look at Impractical Jokers.\",\n" +
                    "  \"poster_path\": \"/nzYTQFrUE68fOXdi1426U6lksd2.jpg\",\n" + "  \"vote_average\": 8\n" + "}",
            TvShowDto::class.java
        )

        assertNotNull(tvShowDto)
        assertNotNull(tvShowDto.id)
        assertNotNull(tvShowDto.overview)
        assertNotNull(tvShowDto.posterImage)
        assertNotNull(tvShowDto.backgroundImage)
        assertNotNull(tvShowDto.title)
        assertNotNull(tvShowDto.mediaType)
        assertNotNull(tvShowDto.releaseDate)
        assertNotNull(tvShowDto.voteAverage)

        assertEquals(74521, tvShowDto.id)
        assertEquals("/pHBEWkI0s4DRheoVUmGDtzeqiMn.jpg", tvShowDto.backgroundImage)
        assertEquals("/nzYTQFrUE68fOXdi1426U6lksd2.jpg", tvShowDto.posterImage)
        assertEquals("tv", tvShowDto.mediaType)
        assertEquals("2017-08-03", tvShowDto.releaseDate)
        assertEquals("Impractical Jokers: After Party", tvShowDto.title)
        assertEquals("Joey Fatone hosts a behind the scenes look at Impractical Jokers.", tvShowDto.overview)
        assertEquals(8f, tvShowDto.voteAverage)
    }

    @Test
    fun nullFieldsTest() {
        val tvShowDto: TvShowDto = gson.fromJson(
            "{\n" + "  \"poster_path\": null,\n" + "  \"backdrop_path\": null,\n" +
                    "  \"first_air_date\": null,\n" + "  \"id\": null,\n" +
                    "  \"media_type\": null,\n" + "  \"name\": null,\n" +
                    "  \"overview\": null,\n" + "  \"vote_average\": null\n" + "}",
            TvShowDto::class.java
        )
        assertNotNull(tvShowDto)
        assertNotNull(tvShowDto.id)
        assertNotNull(tvShowDto.overview)
        assertNotNull(tvShowDto.posterImage)
        assertNotNull(tvShowDto.backgroundImage)
        assertNotNull(tvShowDto.title)
        assertNotNull(tvShowDto.mediaType)
        assertNotNull(tvShowDto.releaseDate)
        assertNotNull(tvShowDto.voteAverage)
    }

    @Test
    fun nullResponseTest() {
        val tvShowDto: TvShowDto = gson.fromJson("{}", TvShowDto::class.java)
        assertNotNull(tvShowDto)
        assertNotNull(tvShowDto.id)
        assertNotNull(tvShowDto.overview)
        assertNotNull(tvShowDto.posterImage)
        assertNotNull(tvShowDto.backgroundImage)
        assertNotNull(tvShowDto.title)
        assertNotNull(tvShowDto.mediaType)
        assertNotNull(tvShowDto.releaseDate)
        assertNotNull(tvShowDto.voteAverage)
    }
}