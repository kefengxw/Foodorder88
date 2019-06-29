package com.foodorder.order.view.adapter

import android.os.Parcel
import android.os.Parcelable
import com.foodorder.order.model.firebase.DataUnitRemoteImageFb
import com.foodorder.order.model.firebase.FoodDataUnitRemoteFb

class OverviewItem : Parcelable {
    var uniqueId: String = ""//default key
    var remoteInfo: FoodDataUnitRemoteFb = FoodDataUnitRemoteFb()
    var remoteImage: DataUnitRemoteImageFb = DataUnitRemoteImageFb()

    constructor() {} // Needed for Firebase

    constructor(parcel: Parcel) {
        uniqueId = parcel.readString()!!
        remoteInfo.name = parcel.readString()!!
        remoteInfo.price = parcel.readString()!!
        remoteInfo.description = parcel.readString()!!
        remoteInfo.category = parcel.readString()!!
        remoteImage.imageRemoteAddr = parcel.readString()!!
        remoteImage.imageRemotePath = parcel.readString()!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.run {
            writeString(uniqueId)
            writeString(remoteInfo.name)
            writeString(remoteInfo.price)
            writeString(remoteInfo.description)
            writeString(remoteInfo.category)
            writeString(remoteImage.imageRemoteAddr)
            writeString(remoteImage.imageRemotePath)
        }
    }

    companion object CREATOR : Parcelable.Creator<OverviewItem> {
        override fun createFromParcel(it: Parcel): OverviewItem {
            return OverviewItem(it)
        }

        override fun newArray(size: Int): Array<OverviewItem?> {
            return arrayOfNulls(size)
        }
    }
}
