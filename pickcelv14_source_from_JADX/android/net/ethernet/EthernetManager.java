package android.net.ethernet;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.mstar.android.ethernet.EthernetDevInfo;
import com.mstar.android.ethernet.IEthernetManager;
import com.mstar.android.ethernet.IEthernetManager.Stub;

public class EthernetManager {
    public static final int ETHERNET_DEVICE_SCAN_RESULT_READY = 0;
    public static final String ETHERNET_STATE_CHANGED_ACTION = "com.mstar.android.ethernet.ETHERNET_STATE_CHANGED";
    public static final int ETHERNET_STATE_DISABLED = 1;
    public static final int ETHERNET_STATE_ENABLED = 2;
    public static final int ETHERNET_STATE_UNKNOWN = 0;
    public static final String EXTRA_ETHERNET_STATE = "ETHERNET_state";
    public static final String EXTRA_LINK_CAPABILITIES = "linkCapabilities";
    public static final String EXTRA_LINK_PROPERTIES = "linkProperties";
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_PREVIOUS_ETHERNET_STATE = "previous_ETHERNET_state";
    public static final String NETWORK_STATE_CHANGED_ACTION = "com.mstar.android.ethernet.STATE_CHANGE";
    public static final String TAG = "EthernetManager";
    static EthernetManager mInstance;
    static final Object mInstanceSync;
    IEthernetManager mService;

    static {
        mInstanceSync = new Object();
        mInstance = null;
    }

    private EthernetManager(IEthernetManager service) {
        this.mService = null;
        this.mService = service;
    }

    public static EthernetManager getInstance() {
        if (mInstance == null) {
            synchronized (mInstanceSync) {
                if (mInstance == null) {
                    mInstance = new EthernetManager(Stub.asInterface(ServiceManager.getService("ethernet")));
                }
            }
        }
        return mInstance;
    }

    public boolean isConfigured() {
        try {
            return this.mService.isConfigured();
        } catch (RemoteException e) {
            Slog.i(TAG, "Can not check eth config state");
            return false;
        }
    }

    public EthernetDevInfo getSavedConfig() {
        try {
            EthernetDevInfo info_new = this.mService.getSavedConfig();
            EthernetDevInfo info = new EthernetDevInfo();
            info.setIfName(info_new.getIfName());
            info.setIpAddress(info_new.getIpAddress());
            info.setNetMask(info_new.getNetMask());
            info.setRouteAddr(info_new.getRouteAddr());
            info.setDnsAddr(info_new.getDnsAddr());
            info.setDns2Addr(info_new.getDns2Addr());
            info.setConnectMode(info_new.getConnectMode());
            info.setProxyOn(info_new.getProxyOn());
            info.setProxyHost(info_new.getProxyHost());
            info.setProxyPort(info_new.getProxyPort());
            info.setProxyExclusionList(info_new.getProxyExclusionList());
            return info;
        } catch (RemoteException e) {
            Slog.i(TAG, "Can not get eth config");
            return null;
        }
    }

    public void updateDevInfo(EthernetDevInfo info) {
        try {
            EthernetDevInfo info_new = new EthernetDevInfo();
            info_new.setIfName(info.getIfName());
            info_new.setIpAddress(info.getIpAddress());
            info_new.setNetMask(info.getNetMask());
            info_new.setRouteAddr(info.getRouteAddr());
            info_new.setDnsAddr(info.getDnsAddr());
            info_new.setDns2Addr(info.getDns2Addr());
            info_new.setConnectMode(info.getConnectMode());
            info_new.setProxyOn(info.getProxyOn());
            info_new.setProxyHost(info.getProxyHost());
            info_new.setProxyPort(info.getProxyPort());
            info_new.setProxyExclusionList(info.getProxyExclusionList());
            this.mService.updateDevInfo(info_new);
        } catch (RemoteException e) {
            Slog.i(TAG, "Can not update ethernet device info");
        }
    }

    public String[] getDeviceNameList() {
        try {
            return this.mService.getDeviceNameList();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setEnabled(boolean enable) {
        try {
            this.mService.setState(enable ? ETHERNET_STATE_ENABLED : ETHERNET_STATE_DISABLED);
        } catch (RemoteException e) {
            Slog.i(TAG, "Can not set new state");
        }
    }

    public int getState() {
        try {
            return this.mService.getState();
        } catch (RemoteException e) {
            return ETHERNET_STATE_UNKNOWN;
        }
    }

    public int getTotalInterface() {
        try {
            return this.mService.getTotalInterface();
        } catch (RemoteException e) {
            return ETHERNET_STATE_UNKNOWN;
        }
    }

    public void setDefaultConf() {
        try {
            this.mService.setMode(EthernetDevInfo.ETHERNET_CONN_MODE_DHCP);
        } catch (RemoteException e) {
        }
    }

    public boolean isNetworkConnected() {
        try {
            return this.mService.isNetworkConnected();
        } catch (RemoteException e) {
            Slog.i(TAG, "Can not check eth connected state");
            return false;
        }
    }

    public boolean isCableConnected() {
        try {
            return this.mService.isCableConnected();
        } catch (RemoteException e) {
            Slog.i(TAG, "Can not check eth cable state");
            return false;
        }
    }
}
