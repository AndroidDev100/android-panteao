package panteao.make.ready.utils;

import panteao.make.ready.beanModel.responseModels.landingTabResponses.railData.PlaylistRailData;

import java.util.Comparator;

public class SortRailsList implements Comparator<PlaylistRailData> {

    @Override
    public int compare(PlaylistRailData listOne, PlaylistRailData listTwo) {

        return (int) (listOne.getData().getIndex() - listTwo.getData().getIndex());
    }
}
