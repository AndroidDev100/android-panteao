package com.make.baseClient

import android.content.Context
import android.content.pm.PackageManager
import com.make.logging.EnveuLogs

class BaseClient {
    private var currentPlatform: BaseGateway? = null
    private var BASE_URL: String? = ""
    private var USER_MNGMT_BASE_URL: String? = ""
    private var API_KEY: String? = ""
    private var OVP_API_KEY: String? = ""
    private var DEVICE_TYPE: String? = ""
    private var PLATFORM: String? = ""
    private var SDK_VERSION: String? = ""
    private var API_VERSION: String? = ""
    private var GATEWAY_TYPE: String? = ""
    private var IS_TAB: Boolean? = false
    private var UDID: String? = ""


    private fun getOvpKey(context: Context): String {
        var ovpKey: String? = null
        try {
            val ai = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val bundle = ai.metaData
            if (bundle != null) {
                ovpKey = bundle.getString("ovp_api_key")

            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw  Throwable(e.message)
        } catch (e: NullPointerException) {
            throw  Throwable(e.message)

        }
        return ovpKey ?: ""
    }

    private fun getApiKey(context: Context, deviceTYPE: String): String {
        var apiKey: String? = null
        try {
            val ai = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val bundle = ai.metaData
            if (bundle != null) {
                if (deviceTYPE == BaseDeviceType.tablet.name)
                    apiKey = bundle.getString("api_key_tab")
                else if(deviceTYPE == BaseDeviceType.mobile.name)
                    apiKey = bundle.getString("api_key_mobile")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw  Throwable(e.message)
        } catch (e: NullPointerException) {
            throw  Throwable(e.message)

        }
        return apiKey ?: ""
    }

    constructor(context: Context, currentPlatform: BaseGateway, baseURL: String, ovpBaseURL: String, deviceTYPE: String,apiKey: String, platform: String, isTablet: Boolean, udid: String) {
        when (currentPlatform) {
            BaseGateway.ENVEU -> {
                EnveuLogs.printWarning(currentPlatform.name + " " + baseURL + " " + deviceTYPE + "  "+apiKey +"  "+ platform + " " + isTablet + " " + udid)
                BASE_URL = baseURL
                DEVICE_TYPE = deviceTYPE
                PLATFORM = platform
                SDK_VERSION = ""
                API_VERSION = ""
                OVP_API_KEY = apiKey
                API_KEY = apiKey
                USER_MNGMT_BASE_URL = ovpBaseURL
                GATEWAY_TYPE = currentPlatform.name
                IS_TAB = isTablet
                UDID = udid
                EnveuLogs.printWarning(currentPlatform.name + " " + baseURL + " " + deviceTYPE + "  "+OVP_API_KEY+"  "+API_KEY +"  "+ platform + " " + isTablet + " " + udid)
            }
            BaseGateway.KALTURA -> {
                EnveuLogs.printWarning(currentPlatform.name);
            }
            BaseGateway.OVP -> {
                EnveuLogs.printWarning(currentPlatform.name);

            }
            BaseGateway.OTHER -> {
                EnveuLogs.printWarning(currentPlatform.name);
            }
        }
    }

    fun getUdid(): String? {
        return UDID
    }

    fun getBaseUrl(): String? {
        return BASE_URL
    }

    fun getUserMngmntBaseUrl(): String? {
        return USER_MNGMT_BASE_URL
    }

    fun getDeviceType(): String? {
        return DEVICE_TYPE
    }

    fun getPlatform(): String? {
        return PLATFORM
    }

    fun getApiKey(): String? {
        return API_KEY
    }

    fun getOVPApiKey(): String? {
        return OVP_API_KEY
    }

    fun getGateway(): String? {
        return GATEWAY_TYPE
    }

    fun getIsTablet(): Boolean? {
        return IS_TAB;
    }

}