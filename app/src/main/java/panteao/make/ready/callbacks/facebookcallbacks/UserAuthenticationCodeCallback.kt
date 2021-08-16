package panteao.make.ready.callbacks.facebookcallbacks

interface UserAuthenticationCodeCallback {
    fun userAuthenticationCode(code: String)
}