package yeop9657.blog.me.trackingbicycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmResults;
import yeop9657.blog.me.trackingbicycle.Database.LocationAdaptor;

/**
 * Created by 양창엽 on 2017-12-09.
 */

public class PathFrame extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.frame_path, container, false);

        final RealmResults<LocationAdaptor> mRealmResults = Realm.getDefaultInstance().where(LocationAdaptor.class).findAll();

        return mView;
    }
}
