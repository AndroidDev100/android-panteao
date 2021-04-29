package panteao.make.ready.activities.videoquality.bean;

public class LanguageItem {

    String languageName;
    int position;
    String defaultLangageName;

    public String getDefaultLangageName() {
        return defaultLangageName;
    }

    public void setDefaultLangageName(String defaultLangageName) {
        this.defaultLangageName = defaultLangageName;
    }


    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
