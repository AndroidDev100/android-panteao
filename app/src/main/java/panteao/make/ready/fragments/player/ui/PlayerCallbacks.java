package panteao.make.ready.fragments.player.ui;

import android.widget.ImageView;

public interface PlayerCallbacks {
    void playPause();
    void Forward();
    void Rewind();
    void finishPlayer();
    void checkOrientation(ImageView id);
    void replay();
    void showPlayerController(boolean isVisible);
    void changeBitRateRequest(String title,int position);
    void bitRateRequest();

}
