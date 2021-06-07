package panteao.make.ready.activities.detailspage;

import java.util.ArrayList;

import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;


public class SeasonSelectionManager {

    private static SeasonSelectionManager mInstance;
    private ArrayList<EnveuVideoItemBean> seasonEpisodeList;


    private SeasonSelectionManager() {

    }

    public static synchronized SeasonSelectionManager getInstance() {
        if (mInstance == null) {
            mInstance = new SeasonSelectionManager();
        }
        return mInstance;
    }

    public ArrayList<EnveuVideoItemBean> getSeasonEpisodeList() {
        return seasonEpisodeList;
    }

    public void setSeasonEpisodeList(ArrayList<EnveuVideoItemBean> seasonEpisodeList) {
        this.seasonEpisodeList = seasonEpisodeList;
    }

    public void clearVideoDetailsCache() {
        seasonEpisodeList = null;
    }
}
