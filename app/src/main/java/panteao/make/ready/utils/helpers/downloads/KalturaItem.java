package panteao.make.ready.utils.helpers.downloads;

import com.kaltura.tvplayer.MediaOptions;
import com.kaltura.tvplayer.OfflineManager;

public abstract class KalturaItem extends Item {

    private int partnerId;
    private String serverUrl;
    private OfflineManager.SelectionPrefs prefs;
    private String title;

    abstract MediaOptions mediaOptions();

    @Override
    String title() {
        return title + "(" + id() + "@" + partnerId + ")";
    }

    public KalturaItem(int partnerId, String serverUrl, String title) {
        super(title);

        this.partnerId = partnerId;
        this.serverUrl = serverUrl;
        this.prefs = prefs;
        this.title = title;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public OfflineManager.SelectionPrefs getPrefs() {
        return prefs;
    }

    public void setPrefs(OfflineManager.SelectionPrefs prefs) {
        this.prefs = prefs;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }
}
