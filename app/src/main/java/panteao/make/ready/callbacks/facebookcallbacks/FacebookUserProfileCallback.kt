package panteao.make.ready.callbacks.facebookcallbacks

import panteao.make.ready.beanModel.facebook.userprofileresponse.FacebookUserProfileResponse


interface FacebookUserProfileCallback {
    fun configurationSuccess(facebookUserProfileResponse: FacebookUserProfileResponse?)
    fun configurationFailure(message: String);
}