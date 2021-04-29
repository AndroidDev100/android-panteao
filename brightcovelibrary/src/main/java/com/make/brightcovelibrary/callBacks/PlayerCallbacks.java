package com.make.brightcovelibrary.callBacks;

import android.widget.ImageView;

public interface PlayerCallbacks {

    void playPause();
    void Forward();
    void Rewind();
    void skipIntro();
    void bingeWatch();
    void SeekbarLastPosition(long position);
    void subtitlePopup();
    void audioTrackPopup();
    void finishPlayer();
    void checkOrientation(ImageView id);
    void replay();
    void showPlayerController(boolean isVisible);
    void changeBitRateRequest(String title,int position);
    void bitRateRequest();
}
