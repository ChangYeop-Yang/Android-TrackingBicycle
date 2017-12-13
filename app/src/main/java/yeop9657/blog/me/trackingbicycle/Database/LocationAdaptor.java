package yeop9657.blog.me.trackingbicycle.Database;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by 양창엽 on 2017-12-13.
 */

public class LocationAdaptor extends RealmObject {

    /* MARK - : String */
    private String sAddress = null;
    private String sBikeName = null;

    /* MARK - : Date */
    private Date mSaveDate = null;

    /* MARK - : RealmList */
    private RealmList<PathAdapter> mMyPath = new RealmList<>();

    /* TODO - : Set/Get LocationAdatper Method */
    public void setAddress(final String sAddress) { this.sAddress = sAddress; }
    public void setBikeName(final String sBikeName) { this.sBikeName = sBikeName; }
    public void setSaveDate(final Date mStartDate) { this.mSaveDate = mStartDate; }
    public void setmMyPath(final PathAdapter mPathAdapter) { this.mMyPath.add(mPathAdapter); }

    public String getAddress() { return this.sAddress; }
    public String getBikeName() { return this.sBikeName; }
    public Date getSaveDate() { return this.mSaveDate; }
    public RealmList<PathAdapter> getMyPath() { return this.mMyPath; }
 }