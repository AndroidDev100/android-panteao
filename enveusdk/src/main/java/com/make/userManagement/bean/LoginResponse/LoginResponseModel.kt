package com.make.userManagement.bean.LoginResponse

class LoginResponseModel {

    //@SerializedName("data")
    var data: Data? = null

    //@SerializedName("responseCode")
    var responseCode: Int = 0
    var isStatus: Boolean = false
    private var debugMessage: String? = null

    fun getDebugMessage(): Any? {
        return debugMessage
    }

    fun setDebugMessage(debugMessage: String) {
        this.debugMessage = debugMessage
    }

    override fun toString(): String {
        return "LoginResponseModel{" +
                "data = '" + data + '\''.toString() +
                ",responseCode = '" + responseCode + '\''.toString() +
                "}"
    }
}