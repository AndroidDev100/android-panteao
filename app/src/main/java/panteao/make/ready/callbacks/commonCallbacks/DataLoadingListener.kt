package panteao.make.ready.callbacks.commonCallbacks

import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean


interface DataLoadingListener {
    fun onDataLoading(item: EnveuVideoItemBean, setAsset: Boolean)
    fun onLoadingOfFirstRow()
    fun onDataLoaded(boolean: Boolean)
    fun onTrailerLoaded(asset: Any?)
    fun onCardSelected(position: Int, enveuVideoItemBean: EnveuVideoItemBean)
}
