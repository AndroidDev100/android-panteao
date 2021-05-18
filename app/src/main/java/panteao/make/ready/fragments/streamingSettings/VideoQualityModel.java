package panteao.make.ready.fragments.streamingSettings;

public class VideoQualityModel {

    private int languageId;
    private String languageName;
    private boolean isSelected;

    public VideoQualityModel(int languageId, String languageName, boolean isSelected) {
        this.languageId = languageId;
        this.languageName = languageName;
        this.isSelected = isSelected;
    }

    public String getLanguageName() {
        return languageName;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
