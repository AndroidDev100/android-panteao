package panteao.make.ready.beanModel.selectedSeason;

public class SelectedSeasonModel {
    String list;
    int selectedId;
    boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public SelectedSeasonModel(String list, int selectedId,boolean isSelected) {
        this.list = list;
        this.selectedId = selectedId;
        this.selected=isSelected;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }
}
