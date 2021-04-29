package panteao.make.ready.callbacks.commonCallbacks;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.beanModel.popularSearch.ItemsItem;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;

public interface SearchClickCallbacks {
    void onEnveuItemClicked(EnveuVideoItemBean itemValue);
    void onShowAllItemClicked(RailCommonData itemValue);
    void onPopularSearchItemClicked(ItemsItem itemValue);
}
