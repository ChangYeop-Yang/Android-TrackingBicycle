package yeop9657.blog.me.trackingbicycle;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import yeop9657.blog.me.trackingbicycle.PublicData.FrameData;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    /* MARK - : Context */
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* POINT - : Context */
        mContext = MainActivity.this;

        /* POINT - : TabLayout */
        final TabLayout mTabLayout = (TabLayout)findViewById(R.id.MainTabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("홈").setTag(FrameData.FRAME_HOME_NUM));
        mTabLayout.addTab(mTabLayout.newTab().setText("지도").setTag(FrameData.FRAME_MAP_NUM));
        mTabLayout.addTab(mTabLayout.newTab().setText("경로").setTag(FrameData.FRAME_PATH_NUM));
        mTabLayout.addTab(mTabLayout.newTab().setText("설정").setTag(FrameData.FRAME_SETTING_NUM));
        mTabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FF4081"));
        mTabLayout.setOnTabSelectedListener(this);

        /* POINT - : Main FrameLayout */
        getSupportFragmentManager().beginTransaction().replace(R.id.MainFrameLayout, new HomeFrame()).commit();
    }

    /* TODO - : TabLayout setOnTabSelectedListener Listener */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        /* POINT - : Toast */
        Toast.makeText(mContext, tab.getText().toString(), Toast.LENGTH_SHORT).show();

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
}
