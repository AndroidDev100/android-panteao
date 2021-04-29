package panteao.make.ready.utils;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;

import java.util.Comparator;

public class SortSearchList implements Comparator<RailCommonData> {

    @Override
    public int compare(RailCommonData searchedKeywords, RailCommonData t1) {

        return (searchedKeywords.getLayoutType() - t1.getLayoutType());
    }
}
