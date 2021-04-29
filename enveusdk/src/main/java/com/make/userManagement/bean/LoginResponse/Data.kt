package com.make.userManagement.bean.LoginResponse

import com.google.gson.annotations.SerializedName

class Data {

    @SerializedName("profileStep")
    var profileStep: String? = null

    @SerializedName("gender")
    var gender: String? = null

    @SerializedName("verified")
    var isVerified: Boolean = false

    @SerializedName("dateOfBirth")
    var dateOfBirth: Long = 0

    @SerializedName("verificationDate")
    var verificationDate: Any? = null

    @SerializedName("expiryDate")
    var expiryDate: Long = 0

    @SerializedName("password")
    var password: Any? = null

    @SerializedName("phoneNumber")
    var phoneNumber: String? = null

    @SerializedName("profilePicURL")
    var profilePicURL: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("email")
    var email: String? = null

    @SerializedName("status")
    var status: String? = null

    override fun toString(): String {
        return "DataResponseRegister{" +
                "profileStep = '" + profileStep + '\''.toString() +
                ",gender = '" + gender + '\''.toString() +
                ",verified = '" + isVerified + '\''.toString() +
                ",dateOfBirth = '" + dateOfBirth + '\''.toString() +
                ",verificationDate = '" + verificationDate + '\''.toString() +
                ",expiryDate = '" + expiryDate + '\''.toString() +
                ",password = '" + password + '\''.toString() +
                ",phoneNumber = '" + phoneNumber + '\''.toString() +
                ",profilePicURL = '" + profilePicURL + '\''.toString() +
                ",name = '" + name + '\''.toString() +
                ",id = '" + id + '\''.toString() +
                ",email = '" + email + '\''.toString() +
                ",status = '" + status + '\''.toString() +
                "}"
    }
}