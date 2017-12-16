package yeop9657.blog.me.trackingbicycle;

import android.Manifest;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import yeop9657.blog.me.trackingbicycle.PublicData.FrameData;

public class MainActivity extends AppCompatActivity {

    /* MARK - : Context */
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* POINT - : Context */
        mContext = MainActivity.this;

        /* POINT - : Resource */
        final Resources mResources = mContext.getResources();

        /* POINT - : TedPermission */
        TedPermission.with(this).setPermissionListener(mPermissionListener).setDeniedMessage(mResources.getString(R.string.ERROR_PERMISSION_ACCEPT))
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE).check();

        /* POINT - : WindowManager */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /* TODO - : TedPermission Listener */
    private final PermissionListener mPermissionListener = new PermissionListener() {

        @Override
        public void onPermissionGranted() {

            /* POINT - : TabLayout */
            final TabLayout mTabLayout = (TabLayout)findViewById(R.id.MainTabLayout);
            mTabLayout.addTab(mTabLayout.newTab().setText("홈").setTag(FrameData.FRAME_HOME_NUM));
            mTabLayout.addTab(mTabLayout.newTab().setText("지도").setTag(FrameData.FRAME_MAP_NUM));
            mTabLayout.addTab(mTabLayout.newTab().setText("경로").setTag(FrameData.FRAME_PATH_NUM));
            mTabLayout.addTab(mTabLayout.newTab().setText("설정").setTag(FrameData.FRAME_SETTING_NUM));
            mTabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FF4081"));
            mTabLayout.setOnTabSelectedListener(mOnTabSelectedListener);

            /* POINT - : Main FrameLayout */
            getSupportFragmentManager().beginTransaction().replace(R.id.MainFrameLayout, new HomeFrame()).commit();

            /* POINT - : Realm */
            Realm.init(mContext);
            RealmConfiguration mRealmConfiguration = new RealmConfiguration.Builder().name("location.realm").build();
            Realm.setDefaultConfiguration(mRealmConfiguration);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

        }
    };

    /* TODO - : TabLayout setOnTabSelectedListener Listener */
    private final TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            /* POINT - : Toast */
            Toast.makeText(mContext, tab.getText().toString(), Toast.LENGTH_SHORT).show();

            /* POINT - : FrameLayout */
            final FrameLayout mFrameLayout = (FrameLayout) findViewById(R.id.MainFrameLayout);
            mFrameLayout.removeAllViews();

            switch ((int) tab.getTag()) {

                case (FrameData.FRAME_HOME_NUM) : {
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFrameLayout, new HomeFrame()).commit();
                    break;
                }

                case (FrameData.FRAME_MAP_NUM) : {
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFrameLayout, new MapFrame()).commit();
                    break;
                }

                case (FrameData.FRAME_PATH_NUM) : {
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainFrameLayout, new PathFrame()).commit();
                    break;
                }

                case (FrameData.FRAME_SETTING_NUM) : {
                    getFragmentManager().beginTransaction().replace(R.id.MainFrameLayout, new SettingFrame()).commit();
                    break;
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
}
