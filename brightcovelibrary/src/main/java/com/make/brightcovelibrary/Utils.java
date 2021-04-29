package com.make.brightcovelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.Format;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

class Utils {


    private static final int PROGRESS_BAR_MAX = 100;

    public static String stringForTime(long timeMs) {
      StringBuilder formatBuilder = new StringBuilder();
      Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

      long totalSeconds = (timeMs + 500) / 1000;
      long seconds = totalSeconds % 60;
      long minutes = (totalSeconds / 60) % 60;
      long hours = totalSeconds / 3600;
      formatBuilder.setLength(0);
      return hours > 0 ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
              : formatter.format("%02d:%02d", minutes, seconds).toString();
  }


    public static int progressBarValue(long position, long duration) {
        int progressValue = 0;
        if (duration > 0L) {
            progressValue = Math.round((float)(position * (long)PROGRESS_BAR_MAX / duration));
        }

        return progressValue;
    }

    public static void setParamstoSeekBarControl(View seekBarControl) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 60);
        seekBarControl.setLayoutParams(params);
    }

    public static void setParamstoPlayerSettingControl(View settingControl) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 80);
        params.gravity = Gravity.CENTER;
        settingControl.setLayoutParams(params);
    }

    public static void setParamstoBackArrow(ImageView backArrow) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 60, 0, 0);
        backArrow.setLayoutParams(params);
    }


    public static void setParamstoSkipButton(LinearLayout backArrow) {
        /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 60, 0, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        backArrow.setLayoutParams(params);*/
    }

    public static void setParamsResetSkipButton(LinearLayout backArrow) {
        /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 10, 10, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        backArrow.setLayoutParams(params);*/
    }

    public static int getSkipPosition(String metadata) {
        int position=0;
        try {
            String[] arrSplit = metadata.split(";");

            position=Integer.parseInt(arrSplit[arrSplit.length-1]);
            if (position>0){
                position=position*1000;
            }
            Log.w("metadataVa",metadata+"  "+arrSplit+"  "+position);
        }catch (Exception ignored){
            position=0;
        }

        return position;
    }

    public static int getSkipHideTime(String metadata) {
        int position=0;
        try {
            String[] arrSplit = metadata.split(";");

            position=Integer.parseInt(arrSplit[1]);
            if (position>0){
                position=position*1000;
            }
            Log.w("metadataVa-->>",metadata+"  "+arrSplit+"  "+position);
        }catch (Exception ignored){
            position=0;
        }

        return position;
    }

    public static String getSkipBtnText(String metadata) {
        String title="";
        try {
            String[] arrSplit = metadata.split(";");

            title=arrSplit[2];

        }catch (Exception ignored){

        }

        return title;
    }

    static boolean track1,track2,track3=false;
    public static ArrayList<TrackItem> createTrackList(List<Format> videoTrackArray, Activity context) {
        track1=false;
        track2=false;
        track3=false;
        ArrayList<TrackItem> arrayList = new ArrayList<>();

        try {
           // mActivity.getResources().getString(R.string.caption_selection)
            arrayList.add(new TrackItem(context.getResources().getString(R.string.auto), 0, context.getString(R.string.auto_description),0,"Auto"));

            for (int i = 0; i < videoTrackArray.size(); i++) {
                Format videoTrackInfo = videoTrackArray.get(i);
                Log.e("tracksVideoBitrate",videoTrackArray.get(i).bitrate+"");
                if (videoTrackInfo.height>=270 && videoTrackInfo.height<360 && !track1){
                    track1=true;
                    arrayList.add(new TrackItem(context.getResources().getString(R.string.low), videoTrackArray.get(i).bitrate, context.getString(R.string.low_description),i+1,"Low"));
                }
                else if (videoTrackInfo.height>=540 && videoTrackInfo.height<720 && !track2){
                    track2=true;
                    arrayList.add(new TrackItem(context.getResources().getString(R.string.medium), videoTrackInfo.bitrate, context.getString(R.string.medium_description),i+1,"Medium"));
                }
                else if (videoTrackInfo.height>=1080 && !track3){
                    track3=true;
                    arrayList.add(new TrackItem(context.getResources().getString(R.string.high), videoTrackArray.get(i).bitrate, context.getString(R.string.high_description),i+1,"High"));
                }
            }
        }catch (Exception e){

        }

        return arrayList;
    }

    public static int getSelectedTrackPosition(String selected_track,Activity context) {
        int pos=0;
        try {
            if (selected_track.equalsIgnoreCase(context.getResources().getString(R.string.auto))){
                pos=0;
            }else if (selected_track.equalsIgnoreCase(context.getResources().getString(R.string.low))){
                pos=1;
            }
            else if (selected_track.equalsIgnoreCase(context.getResources().getString(R.string.medium))){
                pos=2;
            }
            else if (selected_track.equalsIgnoreCase(context.getResources().getString(R.string.high))){
                pos=3;
            }
        }catch (Exception ignored){

        }

        return pos;
    }

    public static void setParamstoSettinIcon(LinearLayout playerSettingIcon) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 67, 0, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        playerSettingIcon.setLayoutParams(params);
    }

    public static void setParamstoSetingButton(LinearLayout playerSettingIcon) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 60, 0, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        playerSettingIcon.setLayoutParams(params);
    }

    public static boolean matched(String selectedTrack, String compareName) {
        if(selectedTrack.equals(compareName)){

        }
        return false;
    }

    public static void updateLanguage(String language, Context context) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
