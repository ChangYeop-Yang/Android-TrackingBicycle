package yeop9657.blog.me.trackingbicycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmResults;
import yeop9657.blog.me.trackingbicycle.Database.LocationAdaptor;
import yeop9657.blog.me.trackingbicycle.RecyclerAdapter.PathAdapter;

/**
 * Created by 양창엽 on 2017-12-09.
 */

public class PathFrame extends Fragment {

    /* MARK - : View */
    private View mView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* POINT - : View */
        mView = inflater.inflate(R.layout.frame_path, container, false);

        /* POINT - : RecyclerView */
        final RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.PathRecyclerView);
        setRecyclerView(mRecyclerView);

        return mView;
    }

    /* TODO - : setRecyclerView Method */
    private void setRecyclerView(final RecyclerView mRecyclerView)
    {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        /* POINT - : RealmResults<LocationAdaptor> */
        final RealmResults<LocationAdaptor> mRealmResults = Realm.getDefaultInstance().where(LocationAdaptor.class).findAll();
        if (mRealmResults != null) {
            mRecyclerView.setAdapter(new PathAdapter(mView, mRealmResults));
        }
    }
}
