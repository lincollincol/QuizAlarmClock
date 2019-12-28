package linc.com.alarmclockforprogrammers.infrastructure;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import linc.com.alarmclockforprogrammers.infrastructure.service.AdminReceiver;

public class DeviceAdminManager {

    private DevicePolicyManager deviceManger ;
    private ComponentName compName ;

    public DeviceAdminManager(Context context) {
        compName = new ComponentName(context, AdminReceiver.class);
        deviceManger = (DevicePolicyManager)
                context.getSystemService(Context.DEVICE_POLICY_SERVICE ) ;
    }

    public void disableAdmin() {
        if(isAdminActive()) {
            deviceManger.removeActiveAdmin(compName);
        }
    }

    public void lockPhone () {
        deviceManger.lockNow() ;
    }

    public boolean isAdminActive() {
        return deviceManger.isAdminActive(compName);
    }
}