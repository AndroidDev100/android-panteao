package panteao.make.ready.activities.videoquality.viewModel;

import android.app.Application;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import panteao.make.ready.R;
import panteao.make.ready.activities.videoquality.bean.LanguageItem;
import panteao.make.ready.activities.videoquality.bean.TrackItem;
import panteao.make.ready.utils.cropImage.helpers.Logger;

import java.util.ArrayList;

import panteao.make.ready.activities.videoquality.bean.LanguageItem;
import panteao.make.ready.activities.videoquality.bean.TrackItem;

public class VideoQualityViewModel extends AndroidViewModel {
    public VideoQualityViewModel(@NonNull Application application) {
        super(application);
    }


    public ArrayList<LanguageItem> getLanguageList() {
        Logger.e("Locale", getApplication().getString(R.string.language_english));
        ArrayList<LanguageItem> trackItems=new ArrayList<>();
        for (int i=0;i<3;i++){
            if (i==0){
                LanguageItem languageItem=new LanguageItem();
                languageItem.setLanguageName(getApplication().getString(R.string.language_english));
                trackItems.add(languageItem);
            }
            else if (i==1){
                LanguageItem languageItem=new LanguageItem();
                languageItem.setLanguageName(getApplication().getString(R.string.language_thai));
                trackItems.add(languageItem);
            }
        }
        return trackItems;
    }

    public ArrayList<TrackItem> getQualityList(Resources resources) {
        ArrayList<TrackItem> trackItems=new ArrayList<>();
        for (int i=0;i<4;i++){
            if (i==0){
                trackItems.add(new TrackItem(resources.getString(R.string.auto),"Auto"));

            }
            else if (i==1){
                trackItems.add(new TrackItem(resources.getString(R.string.low), "Low"));

            }else if (i==2){
                trackItems.add(new TrackItem(resources.getString(R.string.medium), "Medium"));
            }else if (i==3){
                {
                    trackItems.add(new TrackItem(resources.getString(R.string.high), "High"));
                }
            }

        }
        return trackItems;
    }
}