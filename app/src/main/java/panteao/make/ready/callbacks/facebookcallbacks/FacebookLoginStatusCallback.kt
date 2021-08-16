package panteao.make.ready.callbacks.facebookcallbacks

import panteao.make.ready.beanModel.facebook.loginstatusresponse.LoginStatusResponse


interface FacebookLoginStatusCallback {
    fun configurationSuccess(loginStatusResponse: LoginStatusResponse?)
    fun configurationFailure(message: String);
}