package panteao.make.ready.utils;

import panteao.make.ready.utils.config.bean.ConfigBean;

public class MediaTypeConstants {
    private static MediaTypeConstants mediaTypeConstantsInstance;
    static String Movie = "MOVIE";
    String Show = "SHOW";
    String Season = "SEASON";
    static String Episode = "EPISODE";
    String Series = "SERIES";
    String Video = "VIDEO";
    String Live = "LIVE";
    String Article = "ARTICLE";
    ConfigBean configBean;
    public static String VIDEO="VIDEO";

    public String getMovie() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getMovie();
       // return Movie;
    }

    public String getShow() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getShow();
    }

    public String getEpisode() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getEpisode();
        //return Episode;
    }

    public String getSeries() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getSeries();
    }

    public String getLive() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getLive();
    }

    public String getTutorial() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getTutorial();
    }

    public String getChapter() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getChapter();
    }

    public String getTrailor() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getTrailer();
    }

    public String getInstructor() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getInstructor();
    }

    public static String getVIDEO() {
        return VIDEO;
    }

    public static MediaTypeConstants getInstance() {
        if (mediaTypeConstantsInstance == null) {
            mediaTypeConstantsInstance = new MediaTypeConstants();
        }
        return (mediaTypeConstantsInstance);
    }

    public void setConfigObject(ConfigBean configBean) {
        this.configBean=configBean;
    }
}
