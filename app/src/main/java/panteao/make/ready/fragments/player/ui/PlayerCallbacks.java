package panteao.make.ready.fragments.player.ui;

import android.widget.ImageView;

public interface PlayerCallbacks {
    void playPause(ImageView id);
    void Forward();
    void Rewind();
    void finishPlayer();
    void skipIntro();
    void bingeWatch();
    void checkOrientation(ImageView id);
    void QualitySettings();
    void SeekbarLastPosition(long position);
    void showPlayerController(boolean isVisible);
    void sendPlayPauseId(ImageView id);
    void changeBitRateRequest(String title,int position);
    void bitRateRequest();

}
