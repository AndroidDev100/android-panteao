package panteao.make.ready.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import panteao.make.ready.utils.cropImage.helpers.Logger;

public class Network {
    static boolean isNetwork = false;

    public static boolean HaveNetworkConnection(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else {
            return false;
        }
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = Network.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }


    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = Network.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }


    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = Network.getNetworkInfo(context);
        return (info != null && info.isConnected() && Network.isConnectionFast(info.getType(), info.getSubtype()));
    }


    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return true;
        }
    }

    public static String CheckNetworkType(Context context, NetworkInfo active_network) {
        if (active_network == null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            active_network = connectivityManager.getActiveNetworkInfo();
        }
        final String tag = "CheckNetworkType";
        String networkType = "wifi";

        if (active_network != null && active_network.isConnected()) {
            if (active_network.getType() == ConnectivityManager.TYPE_WIFI) {
                Logger.v(tag, "WIFI");
                networkType = "wifi";

            } else {

                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);

                if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE) {
                    // Network type is 2G

                    Logger.v(tag, "2G or GSM");
                    networkType = "gprs";
                } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_CDMA) {
                    // Network type is 2G
                    Logger.v(tag, "2G or CDMA");
                    networkType = "gprs";
                } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
                    // Network type is 3G
                    Logger.v(tag, "3G Network available.");
                    networkType = "3G";
                } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS) {
                    // Network type is 3G
                    Logger.v(tag, "GPRS Network available.");
                    networkType = "gprs";
                }
            }
        } else {
            networkType = "NA";
        }
        return networkType;
    }


    public static boolean checkConnectivityTypeMobile(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		boolean isWifiConn = networkInfo.isConnected();
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
//		Logger.d("DEBUG_TAG", "Wifi connected: " + isWifiConn);
        Logger.d("DEBUG_TAG", "Mobile connected: " + isMobileConn);
        return isMobileConn;
    }

    public static boolean isMobileDataNetworkAvailable(Context context) {
        ConnectivityManager connManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null) {
            return false;
        }

        NetworkInfo info = connManager.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_MOBILE && info.isConnectedOrConnecting();


    }
}
