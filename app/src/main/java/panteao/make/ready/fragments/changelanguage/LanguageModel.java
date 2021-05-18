package panteao.make.ready.fragments.changelanguage;

public class LanguageModel {

    private String languageId;
    private String languageName;
    private boolean isSelected;

    public LanguageModel(String languageId, String languageName, boolean isSelected) {
        this.languageId = languageId;
        this.languageName = languageName;
        this.isSelected = isSelected;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getLanguageName() {
        return languageName;
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
