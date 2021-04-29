package panteao.make.ready.activities.listing.callback;

import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;

public interface ItemClickListener {
    void onRowItemClicked(EnveuVideoItemBean itemValue, int position);

    default void onDeleteWatchListClicked(int assetId, int position){

    }
    default void onDeleteWatchHistoryClicked(int assetId, int position){

    }
}
