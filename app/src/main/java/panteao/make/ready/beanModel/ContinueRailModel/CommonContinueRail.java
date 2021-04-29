package panteao.make.ready.beanModel.ContinueRailModel;

import panteao.make.ready.beanModel.AssetHistoryContinueWatching.ItemsItem;
import panteao.make.ready.userAssetList.ContentsItem;
import panteao.make.ready.beanModel.AssetHistoryContinueWatching.ItemsItem;

public class CommonContinueRail {


    private int id;
    private ItemsItem userAssetStatus;
    private ContentsItem userAssetDetail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemsItem getUserAssetStatus() {
        return userAssetStatus;
    }

    public void setUserAssetStatus(ItemsItem userAssetStatus) {
        this.userAssetStatus = userAssetStatus;
    }

    public ContentsItem getUserAssetDetail() {
        return userAssetDetail;
    }

    public void setUserAssetDetail(ContentsItem userAssetDetail) {
        this.userAssetDetail = userAssetDetail;
    }

    @Override
    public String toString() {
        return "CommonContinueRail{" +
                "id=" + id +
                ", userAssetStatus=" + userAssetStatus +
                ", userAssetDetail=" + userAssetDetail +
                '}';
    }
}
