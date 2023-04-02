package com.example.moviesandseries.activities.main

import com.example.moviesandseries.common.models.TvShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
@PrepareForTest(MainViewModelTest::class, MainRepository::class)
class MainViewModelTest {

    private lateinit var repository: MainRepository
    private lateinit var mainViewModel: MainViewModel

    private lateinit var belongsToFavouritesEntries: MutableList<Boolean>
    private lateinit var favouritesShowsEntries: MutableList<List<TvShow>>

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = PowerMockito.mock(MainRepository::class.java)
        mainViewModel = MainViewModel(repository)
        belongsToFavouritesEntries = mutableListOf()
        favouritesShowsEntries = mutableListOf()
    }

    @Test
    fun whenSearch_withEmptyQuery_shouldEmit_ListOfShows() {
        val expectedList = listOf(TvShow(id = 1, title = "show1"), TvShow(id = 2, title = "show2"))
        runTest {
            val job = launch {
                mainViewModel.favouriteShows.toList(favouritesShowsEntries)
            }
            runCurrent()
            assertEquals(1, favouritesShowsEntries.size)
            assertEquals(null, favouritesShowsEntries[0])

            Mockito.`when`(repository.getFavourites()).thenReturn(expectedList)
            mainViewModel.getAllFavourites()
            runCurrent()
            assertEquals(2, favouritesShowsEntries.size)
            assertEquals(expectedList, favouritesShowsEntries[1])
            job.cancel()
        }
    }

    @Test
    fun whenSearch_withAnyQuery_shouldEmit_ListOfShows() {
        val expectedList = listOf(TvShow(id = 1, title = "show1"), TvShow(id = 2, title = "show2"))
        val query = "searchQuery"
        runTest {
            Mockito.`when`(repository.searchInFavourites(query)).thenReturn(expectedList)
            val job = launch {
                mainViewModel.favouriteShows.toList(favouritesShowsEntries)
            }

            runCurrent()
            assertEquals(1, favouritesShowsEntries.size)
            assertEquals(null, favouritesShowsEntries[0])

            mainViewModel.getAllFavourites(query)
            runCurrent()
            assertEquals(2, favouritesShowsEntries.size)
            assertEquals(expectedList, favouritesShowsEntries[1])
            job.cancel()
        }
    }

    @Test
    fun whenSearchIfShowIsFavourite_withSuccess_shouldEmit_true() {
        runTest {
            Mockito.`when`(repository.belongsToFavourites(1)).thenReturn(true)
            val job = launch {
                mainViewModel.belongsToFavourites.toList(belongsToFavouritesEntries)
            }
            runCurrent()
            assertEquals(1, belongsToFavouritesEntries.size)
            assertEquals(false, belongsToFavouritesEntries[0])

            mainViewModel.searchIfShowIsFavourite(1)
            runCurrent()
            assertEquals(2, belongsToFavouritesEntries.size)
            assertEquals(true, belongsToFavouritesEntries[1])

            job.cancel()
        }
    }

    @Test
    fun whenSearchIfShowIsFavourite_withFailure_shouldEmit_false() {
        runTest {
            Mockito.`when`(repository.belongsToFavourites(2)).thenReturn(false)
            val job = launch {
                mainViewModel.belongsToFavourites.toList(belongsToFavouritesEntries)
            }
            runCurrent()
            assertEquals(1, belongsToFavouritesEntries.size)
            assertEquals(false, belongsToFavouritesEntries[0])

            mainViewModel.searchIfShowIsFavourite(2)
            runCurrent()
            assertEquals(1, belongsToFavouritesEntries.size)
            assertEquals(false, belongsToFavouritesEntries[0])

            job.cancel()
        }
    }

    @Test
    fun whenAddToFavourites_shouldEmit_favouritesList() {
        val expectedList = listOf(TvShow(id = 1, title = "title1"), TvShow(id = 2, title = "title2"), TvShow(id = 3, title = "title3"))
        runTest {
            val job = launch {
                mainViewModel.favouriteShows.toList(favouritesShowsEntries)
            }
            runCurrent()
            assertEquals(1, favouritesShowsEntries.size)
            assertEquals(null, favouritesShowsEntries[0])

            Mockito.`when`(repository.getFavourites()).thenReturn(expectedList)
            mainViewModel.addOrRemoveFromFavourites(TvShow(id = 3, title = "title3"))
            runCurrent()
            assertEquals(2, favouritesShowsEntries.size)
            assertEquals(expectedList, favouritesShowsEntries[1])

            job.cancel()
        }
    }

    @Test
    fun whenRemoveFromFavourites_shouldEmit_favouritesList() {
        val expectedList = listOf(TvShow(id = 1, title = "title1"), TvShow(id = 2, title = "title2"))
        runTest {
            val job = launch {
                mainViewModel.favouriteShows.toList(favouritesShowsEntries)
            }
            runCurrent()
            assertEquals(1, favouritesShowsEntries.size)
            assertEquals(null, favouritesShowsEntries[0])

            Mockito.`when`(repository.getFavourites()).thenReturn(expectedList)
            mainViewModel.addOrRemoveFromFavourites(TvShow(id = 3, title = "title3"))
            runCurrent()
            assertEquals(2, favouritesShowsEntries.size)
            assertEquals(expectedList, favouritesShowsEntries[1])

            job.cancel()
        }
    }

}