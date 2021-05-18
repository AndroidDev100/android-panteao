package panteao.make.ready.callbacks.commonCallbacks

interface OnTabBaseFragmentListener {
    fun showNoDataFoundView(show: Boolean, msg: String)
    fun showProgressBarView(show: Boolean)
}