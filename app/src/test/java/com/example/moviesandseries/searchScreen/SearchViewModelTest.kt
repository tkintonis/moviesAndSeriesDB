package com.example.moviesandseries.searchScreen;

import android.content.res.Resources
import com.example.moviesandseries.common.dto.BasePaginatedResponse
import com.example.moviesandseries.common.dto.TvShowDto
import com.example.moviesandseries.common.enums.ShowType
import com.example.moviesandseries.common.models.TvShow
import com.example.moviesandseries.network.Resource
import com.example.moviesandseries.network.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import kotlin.math.sign

@RunWith(PowerMockRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
@PrepareForTest(SearchViewModelTest::class, Resources::class ,SearchRepository::class)
class SearchViewModelTest {

    private lateinit var repository: SearchRepository
    private lateinit var resources: Resources
    private lateinit var viewModel: SearchViewModel

    private lateinit var searchResultEntries: MutableList<UiState<MutableList<TvShow>>>

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        resources = PowerMockito.mock(Resources::class.java)
        repository = PowerMockito.mock(SearchRepository::class.java)
        viewModel = SearchViewModel(repository, resources)
        searchResultEntries = mutableListOf()
    }

    @Test
    fun whenSearch_withSameQuery_andNotLoadNextPage_andNotRetrying_shouldExecuteOneTime() {
        runTest {
            val job = launch {
                viewModel.searchResult.toList(searchResultEntries)
            }
            runCurrent()
            assertEquals(1, searchResultEntries.size)
            assertEquals(UiState.Success(null), searchResultEntries[0])

            Mockito.`when`(repository.searchTvShow("search", 1)).thenReturn(Resource.Success(data = BasePaginatedResponse(1, listOf(TvShowDto()), 10)))
            viewModel.search("search", loadNextPage = false, retrying = false)
            runCurrent()
            assertEquals(2, searchResultEntries.size)
            assertEquals(UiState.Success(listOf<TvShow>()), searchResultEntries[1])

            viewModel.search("search", loadNextPage = false, retrying = false)
            runCurrent()
            verify(repository, times(1)).searchTvShow(any(), any())
            assertEquals(2, searchResultEntries.size)
            assertEquals(UiState.Success(listOf<TvShow>()), searchResultEntries[1])

            job.cancel()
        }
    }

    @Test
    fun whenSearch_withSameQuery_andLoadingNextPage_shouldEmitSuccessResult() {
        val resourceList1 = listOf(TvShowDto(_id = 1, _title = "show1", _mediaType = "tv"))
        val expectedList1 = listOf(TvShow(id = 1, title = "show1", mediaType = ShowType.SERIES))

        val resourceList2 = listOf(TvShowDto(_id = 2, _title = "show2", _mediaType = "tv"))
        val expectedList2 = listOf(TvShow(id = 1, title = "show1", mediaType = ShowType.SERIES), TvShow(id = 2, title = "show2", mediaType = ShowType.SERIES))
        runTest {
            val job = launch {
                viewModel.searchResult.toList(searchResultEntries)
            }
            runCurrent()
            assertEquals(1, searchResultEntries.size)
            assertEquals(UiState.Success(null), searchResultEntries[0])

            Mockito.`when`(repository.searchTvShow("search", 1)).thenReturn(Resource.Success(data = BasePaginatedResponse(1, resourceList1, 10)))
            Mockito.`when`(repository.searchTvShow("search", 2)).thenReturn(Resource.Success(data = BasePaginatedResponse(2, resourceList2, 10)))
            viewModel.search("search", loadNextPage = false, retrying = false)
            runCurrent()
            assertEquals(2, searchResultEntries.size)
            assertEquals(UiState.Success(expectedList1), searchResultEntries[1])

            viewModel.search("search", loadNextPage = true, retrying = false)
            runCurrent()
            assertEquals(2, searchResultEntries.size)
            assertEquals(UiState.Success(expectedList2), searchResultEntries[1])

            job.cancel()
        }
    }

    @Test
    fun whenSearch_withRetry_shouldEmitSuccessResult() {
        val exception = Exception("error")
        val resourceList1 = listOf(TvShowDto(_id = 1, _title = "show1", _mediaType = "tv"))
        val expectedList1 = listOf(TvShow(id = 1, title = "show1", mediaType = ShowType.SERIES))
        runTest {
            val job = launch {
                viewModel.searchResult.toList(searchResultEntries)
            }
            runCurrent()
            assertEquals(1, searchResultEntries.size)
            assertEquals(UiState.Success(null), searchResultEntries[0])

            Mockito.`when`(repository.searchTvShow("search", 1)).thenReturn(Resource.Error(exception))
            viewModel.search("search", loadNextPage = false, retrying = false)
            runCurrent()
            assertEquals(2, searchResultEntries.size)
            assertEquals(UiState.Error(exception), searchResultEntries[1])

            Mockito.`when`(repository.searchTvShow("search", 1)).thenReturn(Resource.Success(data = BasePaginatedResponse(1, resourceList1, 10)))
            viewModel.search("search", loadNextPage = false, retrying = true)
            runCurrent()
            assertEquals(3, searchResultEntries.size)
            assertEquals(UiState.Success(expectedList1), searchResultEntries[2])

            verify(repository, times(2)).searchTvShow("search", 1)
            job.cancel()
        }
    }

    @Test
    fun whenSearch_withNewQuery_shouldResetPagination() {
        val resourceList1 = listOf(TvShowDto(_id = 1, _title = "show1", _mediaType = "tv"))
        val resourceList2 = listOf(TvShowDto(_id = 2, _title = "show2", _mediaType = "movie"))

        val expectedList1 = listOf(TvShow(id = 1, title = "show1", mediaType = ShowType.SERIES))
        val expectedList2 = listOf(TvShow(id = 2, title = "show2", mediaType = ShowType.MOVIE))

        runTest {
            val job = launch {
                viewModel.searchResult.toList(searchResultEntries)
            }
            runCurrent()
            assertEquals(1, searchResultEntries.size)
            assertEquals(UiState.Success(null), searchResultEntries[0])

            Mockito.`when`(repository.searchTvShow("search1", 1)).thenReturn(Resource.Success(BasePaginatedResponse(1, resourceList1, 10)))
            viewModel.search("search1", loadNextPage = false, retrying = false)
            runCurrent()
            assertEquals(2, searchResultEntries.size)
            assertEquals(UiState.Success(expectedList1), searchResultEntries[1])

            Mockito.`when`(repository.searchTvShow("search2", 1)).thenReturn(Resource.Success(data = BasePaginatedResponse(1, resourceList2, 10)))
            viewModel.search("search2", loadNextPage = false, retrying = false)
            assertEquals(1, viewModel.pageNumber)
            assertEquals(0, viewModel.totalPages)
            assertEquals(0, viewModel.pageSize)
            runCurrent()

            assertEquals(1, viewModel.pageNumber)
            assertEquals(10, viewModel.totalPages)
            assertEquals(1, viewModel.pageSize)

            assertEquals(2, searchResultEntries.size)
            assertEquals(UiState.Success(expectedList2), searchResultEntries[1])

            verify(repository, times(2)).searchTvShow(any(), any())
            job.cancel()
        }
    }

    @Test
    fun whenSearch_withErrorResponse_shouldEmitErrorUiState() {
        val exception = Exception("error")
        runTest {
            val job = launch {
                viewModel.searchResult.toList(searchResultEntries)
            }
            runCurrent()
            assertEquals(1, searchResultEntries.size)
            assertEquals(UiState.Success(null), searchResultEntries[0])

            Mockito.`when`(repository.searchTvShow("search", 1)).thenReturn(Resource.Error(exception))
            viewModel.search("search", loadNextPage = false, retrying = false)
            runCurrent()
            assertEquals(2, searchResultEntries.size)
            assertEquals(UiState.Error(exception), searchResultEntries[1])

            verify(repository, times(1)).searchTvShow(any(), any())
            job.cancel()
        }
    }
}
