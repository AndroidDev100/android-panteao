package panteao.make.ready.activities.listing.callback;

public interface ItemClickListener {
    void onRowItemClicked();

    default void onDeleteWatchListClicked(){

    }
    default void onDeleteWatchHistoryClicked(){

    }
}
