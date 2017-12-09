package yeop9657.blog.me.trackingbicycle;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by 양창엽 on 2017-12-10.
 */

public class SettingFrame extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.frame_setting);
    }
}
