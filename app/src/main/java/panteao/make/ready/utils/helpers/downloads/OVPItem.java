package panteao.make.ready.utils.helpers.downloads;

import com.kaltura.tvplayer.MediaOptions;
import com.kaltura.tvplayer.OVPMediaOptions;
import com.kaltura.tvplayer.OfflineManager;

public class OVPItem extends KalturaItem {

    private int partnerId;
    private String entryId;
    private String serverUrl;
    private OfflineManager.SelectionPrefs prefs;
    private String title;

    public OVPItem(int partnerId, String entryId, String serverUrl, String title) {
        super(partnerId, serverUrl != null ? serverUrl: "https://cdnapisec.kaltura.com", title);

        this.entryId = entryId;
        this.partnerId = partnerId;
        this.serverUrl = serverUrl != null ? serverUrl: "https://cdnapisec.kaltura.com";
        this.prefs = prefs;
        this.title = title;
    }

    @Override
    MediaOptions mediaOptions() {
        return new OVPMediaOptions(entryId);
    }

    @Override
    String id() {
        OfflineManager.AssetInfo assetInfo= getAssetInfo();
        if (assetInfo == null) {
            return entryId;
        }
        return assetInfo.getAssetId();
    }

    @Override
    public int getPartnerId() {
        return partnerId;
    }

    @Override
    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    @Override
    public String getServerUrl() {
        return serverUrl;
    }

    @Override
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public OfflineManager.SelectionPrefs getPrefs() {
        return prefs;
    }

    @Override
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
