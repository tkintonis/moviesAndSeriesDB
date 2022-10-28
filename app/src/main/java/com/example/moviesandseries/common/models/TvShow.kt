package com.example.moviesandseries.common.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviesandseries.common.enums.ShowType
import com.example.moviesandseries.common.dto.TvShowDto
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "favourites")
@Keep
@Parcelize
data class TvShow(
    @PrimaryKey
    val id: Int = -1,
    val title: String = "",
    val posterImage: String = "",
    val overView: String = "",
    val mediaType: ShowType = ShowType.UNDEFINED,
    val releaseDate: String = "",
    val voteAverage: Float = 0f,
    var isFavourite: Boolean = false
) : Parcelable {

    constructor(dto: TvShowDto) : this(
        id = dto.id,
        title = dto.title,
        posterImage = dto.posterImage.ifEmpty { dto.backgroundImage },
        overView = dto.overview,
        mediaType = ShowType.create(dto.mediaType),
        releaseDate = dto.releaseDate,
        voteAverage = dto.voteAverage
    )
}