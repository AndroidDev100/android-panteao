package com.make.baseCollection.baseCategoryModel

import android.os.Parcel
import android.os.Parcelable

class BaseCategory() : Parcelable {
  //own pojo return to view
    var screen: String? = ""
    var responseCode: Int? = 0
    var name: Any? = ""
    var type: String? = ""
    var contentID: String? = ""
    var referenceName: String? = ""
    var layout: String? = ""
    var railCardType: String? = ""
    var railCardSize: String? = ""
    var adPlatformType: String? = ""
    var adType: String? = ""
    var adID: String? = ""
    var displayOrder: Int? = 0
    var height: Any? = 0
    var width: Any? = 0
    var contentImageType: String? = ""
    var showHeader: Boolean? = false
    var autoRotate: Boolean? = false
    var contentSize: Int? = 0
    var autoRotateDuration: Int? = 0
    var contentShowMoreButton: Boolean? = false
    var contentListinglayout: String? = ""
    var contentPlayListType: String? = ""
    var contentPlayListArray: Array<Any>? = null
    var imageSource: String? = ""
    var imageURL: String? = ""
    var manualImageAssetId: String? = ""
    var landingPageType: String? = ""
    var landingPageAssetId: String? = ""
    var landingPagePlayListId: String? = ""
    var landingPagetarget: String? = ""
    var contentIndicator: String? = ""
    var htmlLink: String? = ""
    var morePageSize: Int? = 0
    var isSortable: Boolean? = false
    var filter: List<Any?>? = null
    var predefPlaylistType: String? = ""
    var isAnonymousUser: Any? = false
    var isLoggedInUser: Any? = false
    var landingPageTitle: String? = ""
    var message: String? = ""
    var status: Boolean? = false
    var widgetImageType: String? = ""
    var isProgram: Boolean? = false
        get() = field
        set(value) {
            field = value
        }

  constructor(parcel: Parcel) : this() {
    screen = parcel.readString()
    responseCode = parcel.readValue(Int::class.java.classLoader) as? Int
    type = parcel.readString()
    contentID = parcel.readString()
    referenceName=parcel.readString()
    layout = parcel.readString()
    railCardType = parcel.readString()
    railCardSize = parcel.readString()
    adPlatformType = parcel.readString()
    adType = parcel.readString()
    adID = parcel.readString()
    displayOrder = parcel.readValue(Int::class.java.classLoader) as? Int
    contentImageType = parcel.readString()
    showHeader = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    autoRotate = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    contentSize = parcel.readValue(Int::class.java.classLoader) as? Int
    autoRotateDuration = parcel.readValue(Int::class.java.classLoader) as? Int
    contentShowMoreButton = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    contentListinglayout = parcel.readString()
    contentPlayListType = parcel.readString()
    imageSource = parcel.readString()
    imageURL = parcel.readString()
    manualImageAssetId = parcel.readString()
    landingPageType = parcel.readString()
    landingPageAssetId = parcel.readString()
    landingPagePlayListId = parcel.readString()
    landingPagetarget = parcel.readString()
    contentIndicator = parcel.readString()
    htmlLink = parcel.readString()
    morePageSize = parcel.readValue(Int::class.java.classLoader) as? Int
    isSortable = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    predefPlaylistType = parcel.readString()
    landingPageTitle = parcel.readString()
    message = parcel.readString()
    widgetImageType=parcel.readString()
    status = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(screen)
    parcel.writeValue(responseCode)
    parcel.writeString(type)
    parcel.writeString(contentID)
    parcel.writeString(referenceName)
    parcel.writeString(layout)
    parcel.writeString(railCardType)
    parcel.writeString(railCardSize)
    parcel.writeString(adPlatformType)
    parcel.writeString(adType)
    parcel.writeString(adID)
    parcel.writeValue(displayOrder)
    parcel.writeString(contentImageType)
    parcel.writeValue(showHeader)
    parcel.writeValue(autoRotate)
    parcel.writeValue(contentSize)
    parcel.writeValue(autoRotateDuration)
    parcel.writeValue(contentShowMoreButton)
    parcel.writeString(contentListinglayout)
    parcel.writeString(contentPlayListType)
    parcel.writeString(imageSource)
    parcel.writeString(imageURL)
    parcel.writeString(manualImageAssetId)
    parcel.writeString(landingPageType)
    parcel.writeString(landingPageAssetId)
    parcel.writeString(landingPagePlayListId)
    parcel.writeString(landingPagetarget)
    parcel.writeString(contentIndicator)
    parcel.writeString(htmlLink)
    parcel.writeValue(morePageSize)
    parcel.writeValue(isSortable)
    parcel.writeString(predefPlaylistType)
    parcel.writeString(landingPageTitle)
    parcel.writeString(message)
    parcel.writeString(widgetImageType)
    parcel.writeValue(status)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<BaseCategory> {
    override fun createFromParcel(parcel: Parcel): BaseCategory {
      return BaseCategory(parcel)
    }

    override fun newArray(size: Int): Array<BaseCategory?> {
      return arrayOfNulls(size)
    }
  }
}