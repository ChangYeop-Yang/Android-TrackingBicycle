package yeop9657.blog.me.trackingbicycle.Database;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by 양창엽 on 2017-12-13.
 */

public class PathAdapter extends RealmObject {

    /* MARK - : Double */
    private double dLatitude = 0.0;
    private double dLongitude = 0.0;

    /* MARK - : Float */
    private float fSpeed = 0;

    /* MARK - : Date */
    private Date mCurrentDate = null;

    /* TODO - : Set/Get PathAdapter Method */
    public void setLatitude(final double dLatitude) { this.dLatitude = dLatitude; }
    public void setLongitude(final double dLongitude) { this.dLongitude = dLongitude; }
    public void setfSpeed(final float fSpeed) { this.fSpeed = fSpeed; }
    public void setCurrentDate(final Date mCurrentDate) { this.mCurrentDate = mCurrentDate; }

    public double getLatitude() { return this.dLatitude; }
    public double getLongitude() { return this.dLongitude; }
    public float getfSpeed() { return this.fSpeed; }
    public Date getCurrentDate() { return this.mCurrentDate; }
}
