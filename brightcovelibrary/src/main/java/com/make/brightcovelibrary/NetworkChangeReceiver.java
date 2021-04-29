package com.make.brightcovelibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final long NETWORK_BROADCAST_DELAY = 1000;
    private ConnectivityReceiverListener connectivityReceiverListener;
    private static NetworkChangeReceiver networkChangeReceiver;

    private  NetworkChangeReceiver(){

    }
    public static NetworkChangeReceiver getInstance(){
        if(networkChangeReceiver == null){
            networkChangeReceiver = new NetworkChangeReceiver();
        }
        return networkChangeReceiver;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (Network.HaveNetworkConnection(context)) {
                    if (connectivityReceiverListener != null) {
                        connectivityReceiverListener.onNetworkConnectionChanged(true);
                    }
                } else {
                    if(Network.isMobileDataNetworkAvailable(context) || Network.checkConnectivityTypeMobile(context))
                    {
                        if (connectivityReceiverListener != null) {
                            connectivityReceiverListener.onNetworkConnectionChanged(true);
                        }
                    }else
                    {
                        if (connectivityReceiverListener != null) {
                            connectivityReceiverListener.onNetworkConnectionChanged(false);
                        }
                    }
                }
            }, NETWORK_BROADCAST_DELAY);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setConnectivityReceiverListener(ConnectivityReceiverListener listener){
        connectivityReceiverListener = listener;
    }
    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}