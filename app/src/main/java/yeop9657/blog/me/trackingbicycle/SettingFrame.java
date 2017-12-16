package yeop9657.blog.me.trackingbicycle;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by 양창엽 on 2017-12-10.
 */

public class SettingFrame extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.frame_setting);
        
        setOnPreferenceChange(findPreference("key_name"));
        setOnPreferenceChange(findPreference("key_phone_number"));
        setOnPreferenceChange(findPreference("key_bloodtype"));
        setOnPreferenceChange(findPreference("key_emergency_number"));
    }
    
    
    private void setOnPreferenceChange(Preference mPreference) {
        mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        
        onPreferenceChangeListener.onPreferenceChange(
                mPreference,
                PreferenceManager.getDefaultSharedPreferences(
                        mPreference.getContext()).getString(
                        mPreference.getKey(), ""));
    }
    
    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            
            String stringValue = newValue.toString();
            
            if (preference instanceof EditTextPreference) {
                preference.setSummary(stringValue);
                
            }
            else if (preference instanceof ListPreference) {
                /**
                 * ListPreference의 경우 stringValue가 entryValues이기 때문에 바로 Summary를
                 * 적용하지 못한다 따라서 설정한 entries에서 String을 로딩하여 적용한다
                 */
                
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                
                preference
                        .setSummary(index >= 0 ? listPreference.getEntries()[index]
                                : null);
                
            }
            
            
            return true;
        }
        
    };
    
};

