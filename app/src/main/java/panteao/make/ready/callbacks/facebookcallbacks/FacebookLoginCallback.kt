package panteao.make.ready.callbacks.facebookcallbacks

import panteao.make.ready.beanModel.facebook.loginresponse.FacebookLoginResponse


interface FacebookLoginCallback {
    fun configurationSuccess(facebookLoginResponse: FacebookLoginResponse?)
    fun configurationFailure(message: String);
}