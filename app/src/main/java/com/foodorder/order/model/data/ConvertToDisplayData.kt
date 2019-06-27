package com.foodorder.order.model.data

import com.foodorder.order.model.remote.ArtistItem
import com.foodorder.order.model.remote.RemoteBean
import com.foodorder.order.model.repository.DisplayData
import com.foodorder.order.model.repository.DisplayItem

object ConvertToDisplayData {

    fun convert(data: RemoteBean): DisplayData {

        var resultDisplay = DisplayData()
        val artists = data.artists

        //for next query
        resultDisplay.mItem = null

        if (artists.total == 0 || artists.items == null || artists.items.isEmpty()) {
            return resultDisplay
        }

        val tmpList = ArrayList<DisplayItem>()
        val eachArtist: List<ArtistItem> = artists.items

        for (item in eachArtist) {

            val url = if (item.images.isNullOrEmpty()) "" else (item.images.first().url)
            tmpList.add(DisplayItem(item.name, url))
        }
        resultDisplay.mTotal = artists.total
        resultDisplay.mItem = tmpList
        if (artists.previous != null) resultDisplay.mPrev = artists.previous
        if (artists.next != null) resultDisplay.mNext = artists.next

        return resultDisplay
    }
}