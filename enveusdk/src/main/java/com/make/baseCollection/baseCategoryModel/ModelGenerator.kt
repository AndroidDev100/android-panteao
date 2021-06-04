package com.make.baseCollection.baseCategoryModel

import com.make.baseClient.BaseConfiguration
import com.make.baseClient.BaseGateway
import com.watcho.enveu.bean.EnveuCategory
import com.watcho.enveu.bean.WidgetsItem
import retrofit2.Response

class ModelGenerator{

    companion object{
        val instance=ModelGenerator()
    }

    fun setGateWay(gatewayType: String?) : ModelGenerator{

        return instance;
    }

    fun createModel(response: Response<EnveuCategory>) : List<BaseCategory>{

        val list=ArrayList<BaseCategory>()
        for (i in response.body()?.data?.widgets ?: ArrayList<WidgetsItem>()){
            val cat=BaseCategory();
            cat.status=true
            cat.screen=response.body()?.data?.screen
            cat.responseCode=response.body()?.responseCode
            cat.name=i?.item?.title
            cat.type=i?.type

            when(BaseConfiguration.instance.clients.getGateway()){
                BaseGateway.ENVEU.name -> {
                    cat.contentID=i?.item?.playlist?.kEntryId
                    cat.widgetImageType=i?.widgetImageType;
                }

                BaseGateway.KALTURA.name -> {
                    cat.contentID=i?.item?.playlist?.kalturaChannelId
                    cat.widgetImageType=i?.kalturaOttImageType;
                }
            }
            cat.layout=i?.layout
            cat.referenceName=i?.item?.playlist?.referenceName
            cat.adPlatformType=i?.item?.platform
            cat.adPlatformType=i?.item?.platform
            cat.adID=i?.item?.adUnitInfo?.adId
            cat.displayOrder=i?.displayOrder
            cat.height=i?.height
            cat.width=i?.width
            cat.contentImageType=i?.item?.imageType
            cat.showHeader=i?.item?.showHeader
            cat.autoRotate=i?.item?.autoRotate
            cat.contentSize=i?.item?.listingLayoutContentSize
            cat.railCardType=i?.item?.railCardType
            cat.railCardSize=i?.item?.railCardSize
            cat.autoRotateDuration=i?.item?.autoRotateDuration
            cat.contentShowMoreButton=i?.item?.showMoreButton
            cat.contentListinglayout=i?.item?.listingLayout
            cat.contentPlayListType=i?.item?.playlist?.type
            cat.imageSource=i?.item?.imageSource
            cat.imageURL=i?.item?.imageURL
            cat.manualImageAssetId=i?.item?.assetId
            cat.landingPageType=i?.item?.landingPage?.type
            cat.landingPageAssetId=i?.item?.landingPage?.assetID
            when(BaseConfiguration.instance.clients.getGateway()){
                BaseGateway.ENVEU.name -> {
                    cat.landingPagePlayListId=i?.item?.landingPage?.playlist?.brightcovePlaylistId
                }

                BaseGateway.KALTURA.name -> {
                    cat.landingPagePlayListId=i?.item?.landingPage?.playlist?.kalturaChannelId
                }
            }


            cat.landingPagetarget=i?.item?.landingPage?.target
            cat.contentIndicator=i?.item?.contentIndicator
            cat.htmlLink=i?.item?.landingPage?.link
            cat.morePageSize=i?.item?.pageSize
            cat.isSortable=i?.item?.moreViewConfig?.sortable
            cat.filter= i?.item?.moreViewConfig?.filters
            cat.predefPlaylistType= i?.item?.playlist?.predefPlaylistType
            cat.isAnonymousUser=i?.item?.playlist?.forAnonymousUser
            cat.isLoggedInUser=i?.item?.playlist?.forLoggedInUser
            cat.landingPageTitle=i?.item?.landingPage?.landingPageTitle
            cat.isProgram=i?.item?.isProgram
            cat.imageSource=i?.item?.imageSource
            cat.imageURL=i?.item?.imageURL
            cat.manualImageAssetId=i?.item?.assetId
            cat.landingPageType=i?.item?.landingPage?.type
            cat.landingPageAssetId=i?.item?.landingPage?.assetID
            cat.landingPagetarget=i?.item?.landingPage?.target
            cat.contentIndicator=i?.item?.contentIndicator
            cat.htmlLink=i?.item?.landingPage?.link
            cat.morePageSize=i?.item?.pageSize
            cat.isSortable=i?.item?.moreViewConfig?.sortable
            cat.filter= i?.item?.moreViewConfig?.filters
            cat.predefPlaylistType= i?.item?.playlist?.predefPlaylistType
            cat.isAnonymousUser=i?.item?.playlist?.forAnonymousUser
            cat.isLoggedInUser=i?.item?.playlist?.forLoggedInUser
            cat.landingPageTitle=i?.item?.landingPage?.landingPageTitle
            cat.isProgram=i?.item?.isProgram


            list.add(cat)
        }

        return list
    }

    fun createModel(t: Throwable) : List<BaseCategory>{
        val cat=BaseCategory();
        val list=ArrayList<BaseCategory>()
        cat.status=false
        cat.message=t?.message
        return list
    }


}