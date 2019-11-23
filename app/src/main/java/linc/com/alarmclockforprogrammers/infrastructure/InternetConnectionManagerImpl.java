package linc.com.alarmclockforprogrammers.infrastructure;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import linc.com.alarmclockforprogrammers.domain.interactor.alarms.InternetConnectionManager;

public class InternetConnectionManagerImpl implements InternetConnectionManager {

    private Context context;

    public InternetConnectionManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        for (NetworkInfo network : cm.getAllNetworkInfo()) {
            if(network.isConnected()) {
                return true;
            }
        }
        return false;
    }
}
