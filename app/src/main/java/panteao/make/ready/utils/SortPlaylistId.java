package panteao.make.ready.utils;

import panteao.make.ready.beanModel.responseModels.landingTabResponses.playlistResponse.PlaylistsItem;

import java.util.Comparator;

public class SortPlaylistId implements Comparator<PlaylistsItem> {

    @Override
    public int compare(PlaylistsItem listOne, PlaylistsItem listTwo) {

        return (int) (listOne.getId() - listTwo.getId());
    }
}