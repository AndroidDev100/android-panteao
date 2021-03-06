package panteao.make.ready;

import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.config.bean.ConfigBean;

import java.util.ArrayList;
import java.util.List;

public class SDKConfig {
    private static SDKConfig sdkConfigInstance;
    final boolean isTablet = PanteaoApplication.getInstance().getResources().getBoolean(R.bool.isTablet);
    ConfigBean configBean;
    private SDKConfig() {

    }

    public static SDKConfig getInstance() {
        if (sdkConfigInstance == null) {
            sdkConfigInstance = new SDKConfig();
        }
        return (sdkConfigInstance);
    }

    /*qa keys*/
//    public static String CONFIG_BASE_URL = "https://experience-manager-fe-api.beta.enveu.com/app/api/v1/config/";
    public static final String CONFIG_BASE_URL = "https://experience-manager-fe-api.beta.enveu.com/app/api/v1/config/";
    public static final String API_KEY_MOB = "ayfodjbdjeuxsnzvjbskhyuzbspcmcfqnpohspylv";
    public static final String API_KEY_TAB = "jissiylkatgrotfepleryojtlkimilvhqtathxjl";
    public static final int CONFIG_VERSION = 1;
    public static String ApplicationStatus = "disconnected";
    public static String TERMCONDITION = "https://www.makeready.tv/terms-of-use";
    public static String PRIVACYPOLICY = "https://www.makeready.tv/panteao-privacy-policy";
    public static final String WEBP_QUALITY="filters:format(webp):quality(60)/";
    public static int DOWNLOAD_EXPIRY_DAYS=30;
    public static boolean DOWNLOAD_ENABLE=true;
    public static final int PARTNER_ID= 802792;
    public static final String KALTURA_SERVER_URL =  "https://cdnapisec.kaltura.com";
    public void setConfigObject(ConfigBean configResponse) {
        this.configBean=configResponse;
        MediaTypeConstants.getInstance().setConfigObject(configBean);
    }

    public String getBASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getBaseUrl();
    }

    public String getOVP_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getOvpBaseUrl();
    }

    public String getSEARCH_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getSearchBaseUrl();
    }

    public String getSUBSCRIPTION_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getSubscriptionBaseUrl();
    }
    public String getCoupon_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getCouponBaseUrl();
    }
    public String getAboutUs_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getAboutUs();
    }
    public String getPAYMENT_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getPaymentBaseUrl();
    }
    public String getTermCondition_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getTermsConditionUrl();
    }

    public String getPrivay_Policy_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getPrivacyPolicyUrl();
    }

    public String getOvpApiKey() {
        if (isTablet){
            return API_KEY_TAB;
        }else {
            return API_KEY_MOB;
        }
    }

    public String getFirstTabId() {
        return AppCommonMethod.getHomeTabId(configBean,"HOME");
    }

    public String getSecondTabId() {
        return AppCommonMethod.getHomeTabId(configBean,"PREMIUM");

    }
    public String getThirdTabId() {
        return AppCommonMethod.getHomeTabId(configBean,"INSTRUCTOR");
    }
    public String getFourthTabId() {
        return AppCommonMethod.getHomeTabId(configBean,"FREE");
    }

    public String getMovieDetailId() {
        return AppCommonMethod.getHomeTabId(configBean,"MOVIE");
    }

    public String getShowDetailId() {
        return AppCommonMethod.getHomeTabId(configBean,"SHOW DETAIL");
    }

    public String getEpisodeDetailId() {
        return AppCommonMethod.getHomeTabId(configBean,"EPISODE DETAIL");
    }

    public String getSeriesDetailId() {
        return AppCommonMethod.getHomeTabId(configBean,"SERIES DETAIL");
    }
    public String getTutorialDetailId() {
        return AppCommonMethod.getHomeTabId(configBean,"TUTORIAL DETAIL");
    }
    public String getChapterDetailId() {
        return AppCommonMethod.getHomeTabId(configBean,"CHAPTER DETAIL");
    }
    public String getTrailerDetailId() {
        return AppCommonMethod.getHomeTabId(configBean,"TRAILER DETAIL");
    }
    public String getInstructorDetaildId() {
        return AppCommonMethod.getHomeTabId(configBean,"INSTRUCTOR DETAIL");
    }
    public String getLiveDetailId() {
        return AppCommonMethod.getHomeTabId(configBean,"LIVE");
    }

    public String getPopularSearchId() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getPopularSearchId();
    }

    public String getThaiLangCode() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getLanguageCodes().getThai();
    }

    public String getEnglishCode() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getLanguageCodes().getEnglish();
    }

    public List<String> getSupportedCurrencies() {
        return configBean == null ? new ArrayList<>() : configBean.getData().getAppConfig().getSupportedCurrencies();
    }

    public String getWebPUrl() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getImageTransformBaseURL();
    }

    public String getConfigVastTag() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getVastTagUrl();
    }

    public boolean getBingeWatchingEnabled() {
        return configBean != null && configBean.getData().getAppConfig().getBingeWatchingEnabled();
    }

    public int getTimer() {
        return configBean == null ? 0 : configBean.getData().getAppConfig().getTimer();
    }

    public int getDownloadExpiryDays() {
        return DOWNLOAD_EXPIRY_DAYS;
    }

    public boolean isDownloadEnable() {
        return configBean != null && configBean.getData().getAppConfig().isDownloadEnable();
    }
}
