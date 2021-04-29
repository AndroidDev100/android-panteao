package panteao.make.ready.beanModel.AssetHistoryContinueWatching;

public class ItemsItem {
    private int duration;
    private int id;
    private int position;
    private String type;
    private boolean finishedWatching;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFinishedWatching() {
        return finishedWatching;
    }

    public void setFinishedWatching(boolean finishedWatching) {
        this.finishedWatching = finishedWatching;
    }

    @Override
    public String toString() {
        return
                "ItemsItem{" +
                        "duration = '" + duration + '\'' +
                        ",id = '" + id + '\'' +
                        ",position = '" + position + '\'' +
                        ",type = '" + type + '\'' +
                        ",finishedWatching = '" + finishedWatching + '\'' +
                        "}";
    }
}
