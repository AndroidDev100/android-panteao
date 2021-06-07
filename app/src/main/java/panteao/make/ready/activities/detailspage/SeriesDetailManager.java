package panteao.make.ready.activities.detailspage;


import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;

public class SeriesDetailManager {

    private static SeriesDetailManager mInstance;
    private EnveuVideoItemBean selectedSeries;


    private SeriesDetailManager() {

    }


    public static synchronized SeriesDetailManager getInstance() {
        if (mInstance == null) {
            mInstance = new SeriesDetailManager();
        }
        return mInstance;
    }

    public EnveuVideoItemBean getSelectedSeries() {
        return selectedSeries;
    }

    public void setSelectedSeries(EnveuVideoItemBean selectedSeries) {
        this.selectedSeries = selectedSeries;
    }
}
