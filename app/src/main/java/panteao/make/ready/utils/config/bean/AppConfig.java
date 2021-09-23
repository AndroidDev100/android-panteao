
package panteao.make.ready.utils.config.bean;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppConfig {

    @SerializedName("baseUrl")
    @Expose
    private String baseUrl;

    @SerializedName("couponBaseUrl")
    @Expose
    private String couponBaseUrl;

    @SerializedName("vastTagUrl")
    @Expose
    private String vastTagUrl;

    @SerializedName("bingeWatchingEnabled")
    @Expose
    private boolean bingeWatchingEnabled;

    @SerializedName("downloadEnable")
    @Expose
    private boolean downloadEnable;

    @SerializedName("timer")
    @Expose
    private int timer;

    @SerializedName("ovpBaseUrl")
    @Expose
    private String ovpBaseUrl;

    @SerializedName("termsConditionUrl")
    @Expose
    private String termsConditionUrl;

    @SerializedName("privacyPolicyUrl")
    @Expose
    private String privacyPolicyUrl;

    @SerializedName("aboutUsUrl")
    @Expose
    private String aboutUsUrl;

    @SerializedName("subscriptionBaseUrl")
    @Expose
    private String subscriptionBaseUrl;
    @SerializedName("searchBaseUrl")
    @Expose
    private String searchBaseUrl;
    @SerializedName("paymentBaseUrl")
    @Expose
    private String paymentBaseUrl;
    @SerializedName("imageTransformBaseURL")
    @Expose
    private String imageTransformBaseURL;
    @SerializedName("supportedCurrencies")
    @Expose
    private List<String> supportedCurrencies = null;
    @SerializedName("popularSearchId")
    @Expose
    private String popularSearchId;
    @SerializedName("primaryLanguage")
    @Expose
    private String primaryLanguage;
    @SerializedName("brightcoveAccountId")
    @Expose
    private Long brightcoveAccountId;
    @SerializedName("brightcovePolicyKey")
    @Expose
    private String brightcovePolicyKey;
    @SerializedName("version")
    @Expose
    private Version version;
    @SerializedName("mediaTypes")
    @Expose
    private MediaTypes mediaTypes;
    @SerializedName("languageCodes")
    @Expose
    private LanguageCodes languageCodes;
    @SerializedName("navScreens")
    @Expose
    private List<NavScreen> navScreens = null;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getOvpBaseUrl() {
        return ovpBaseUrl;
    }

    public void setOvpBaseUrl(String ovpBaseUrl) {
        this.ovpBaseUrl = ovpBaseUrl;
    }

    public String getSubscriptionBaseUrl() {
        return subscriptionBaseUrl;
    }

    public String getCouponBaseUrl() {
        return couponBaseUrl;
    }

    public void setCouponBaseUrl(String couponBaseUrl) {
        this.couponBaseUrl = couponBaseUrl;
    }

    public void setSubscriptionBaseUrl(String subscriptionBaseUrl) {
        this.subscriptionBaseUrl = subscriptionBaseUrl;
    }

    public String getSearchBaseUrl() {
        return searchBaseUrl;
    }

    public void setSearchBaseUrl(String searchBaseUrl) {
        this.searchBaseUrl = searchBaseUrl;
    }

    public String getPopularSearchId() {
        return popularSearchId;
    }

    public void setPopularSearchId(String popularSearchId) {
        this.popularSearchId = popularSearchId;
    }

    public Long getBrightcoveAccountId() {
        return brightcoveAccountId;
    }

    public void setBrightcoveAccountId(Long brightcoveAccountId) {
        this.brightcoveAccountId = brightcoveAccountId;
    }

    public String getTermsConditionUrl() {
        return termsConditionUrl;
    }

    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    public String getAboutUs() {
        return aboutUsUrl;
    }

    public void setAboutUsUrl(String aboutus) {
        this.aboutUsUrl = aboutus;
    }

    public String getBrightcovePolicyKey() {
        return brightcovePolicyKey;
    }

    public void setBrightcovePolicyKey(String brightcovePolicyKey) {
        this.brightcovePolicyKey = brightcovePolicyKey;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public MediaTypes getMediaTypes() {
        return mediaTypes;
    }

    public void setMediaTypes(MediaTypes mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    public LanguageCodes getLanguageCodes() {
        return languageCodes;
    }

    public void setLanguageCodes(LanguageCodes languageCodes) {
        this.languageCodes = languageCodes;
    }

    public List<NavScreen> getNavScreens() {
        return navScreens;
    }

    public void setNavScreens(List<NavScreen> navScreens) {
        this.navScreens = navScreens;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public String getPaymentBaseUrl() {
        return paymentBaseUrl;
    }

    public void setPaymentBaseUrl(String paymentBaseUrl) {
        this.paymentBaseUrl = paymentBaseUrl;
    }

    public List<String> getSupportedCurrencies() {
        return supportedCurrencies;
    }

    public void setSupportedCurrencies(List<String> supportedCurrencies) {
        this.supportedCurrencies = supportedCurrencies;
    }

    public String getImageTransformBaseURL() {
        return imageTransformBaseURL;
    }

    public void setImageTransformBaseURL(String imageTransformBaseURL) {
        this.imageTransformBaseURL = imageTransformBaseURL;
    }

    public void setVastTagUrl(String vastTagUrl) {
        this.vastTagUrl = vastTagUrl;
    }

    public String getVastTagUrl() {
        return vastTagUrl;
    }

    public boolean getBingeWatchingEnabled() {
        return bingeWatchingEnabled;
    }

    public void setBingeWatchingEnabled(boolean bingeWatchingEnabled) {
        this.bingeWatchingEnabled = bingeWatchingEnabled;
    }

    public void setDownloadEnable(boolean downloadEnable) {
        this.downloadEnable = downloadEnable;
    }

    public boolean isDownloadEnable() {
        return downloadEnable;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getTimer() {
        return timer;
    }
}
