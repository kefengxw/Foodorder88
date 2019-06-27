package com.foodorder.order.view.adapter

import android.os.Parcel
import android.os.Parcelable

class OverviewItem : Parcelable {
    var uniqueId: String = ""//default key
    var name: String = ""
    var imageAddr: String = ""//only the firebase picture address
    var imagePath: String = ""//the firebase folder and picture name
    var price: String = ""
    var description: String = ""
    var category: String = ""

    constructor() {} // Needed for Firebase

    constructor(id: String, name: String, addr: String, path: String, price: String, desc: String, cate: String) {
        this.uniqueId = id
        this.name = name
        this.imageAddr = addr
        this.imagePath = path
        this.price = price
        this.description = desc
        this.category = cate
    }

    constructor(parcel: Parcel) {
        uniqueId = parcel.readString()!!
        name = parcel.readString()!!
        imageAddr = parcel.readString()!!
        imagePath = parcel.readString()!!
        price = parcel.readString()!!
        description = parcel.readString()!!
        category = parcel.readString()!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.run {
            writeString(uniqueId)
            writeString(name)
            writeString(imageAddr)
            writeString(imagePath)
            writeString(price)
            writeString(description)
            writeString(category)
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