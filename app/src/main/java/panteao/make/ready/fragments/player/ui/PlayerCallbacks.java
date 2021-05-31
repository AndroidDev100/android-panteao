package panteao.make.ready.fragments.player.ui;

import android.widget.ImageView;

public interface PlayerCallbacks {
    void playPause(ImageView id);
    void Forward();
    void Rewind();
    void finishPlayer();
    void checkOrientation(ImageView id);
    void replay();
    void SeekbarLastPosition(long position);
    void showPlayerController(boolean isVisible);
    void changeBitRateRequest(String title,int position);
    void bitRateRequest();

}
