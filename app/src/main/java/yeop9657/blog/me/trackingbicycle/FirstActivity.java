package yeop9657.blog.me.trackingbicycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 양창엽 on 2017-12-16.
 */

public class FirstActivity extends AppCompatActivity {

    /* POINT - : TimerTask */
    private TimerTask mWaitTask = null;
    private Timer mTotalTimer = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        /* POINT - : Glide */
        final ImageView mBgImageView = (ImageView) findViewById(R.id.iv_FirstBg);
        Glide.with(this).load(R.drawable.bg_background).into(mBgImageView);

        mWaitTask = new TimerTask() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        };

        mTotalTimer = new Timer();
        mTotalTimer.schedule(mWaitTask, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mTotalTimer.cancel();
    }
}
