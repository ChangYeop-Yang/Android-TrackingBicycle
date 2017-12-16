package yeop9657.blog.me.trackingbicycle.Network;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import yeop9657.blog.me.trackingbicycle.HomeFrame;
import yeop9657.blog.me.trackingbicycle.Location.LocationSystem;
import yeop9657.blog.me.trackingbicycle.MainActivity;
import yeop9657.blog.me.trackingbicycle.R;

/**
 * Created by 양창엽 on 2017-12-13.
 */

public class WeatherParsing extends AsyncTask<View, Void, View> {

    private Context mContext;
    private View mView;
    String sUrlStrForm1 =  "http://www.kma.go.kr/wid/queryDFS.jsp?gridx=";
    String sUrlStrForm2 =  "&gridy=";
    String sGridX,sGridY;
    String sHour,sDay,sTemp,sTmx,sTmn,sSky,sPty,sWfKor,sWfEn,sPop,sR12,sS12,sWs,sWd,sWdKor,sWdEn,sReh, sPm10Grade,sStationName;
    private TextView vMainWeatherState,vMainWeatherContents;
    private ImageView vMainWeather;

    private Pair<Double, Double> mCoordinate = null;

    public static int TO_GRID = 0;
    public static int TO_GPS = 1;
    double dLat, dLong;

    public WeatherParsing(Context mContext, View mView, double dLatitude, double dLongitude){
        this.mContext = mContext;
        this.mView = mView;
        //this.mCoordinate = new Pair<>(dLatitude, dLongitude);
        dLat = dLatitude;
        dLong = dLongitude;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(dLat!= 0 && dLong!= 0) {
            LatXLngY tmp = convertGRID_GPS(TO_GRID, dLat, dLong);
            sGridX = new Double(tmp.x).toString();
            sGridY = new Double(tmp.y).toString();
            Log.d("GridX",sGridX);
            Log.d("GridY",sGridY);
            vMainWeatherContents = (TextView)mView.findViewById(R.id.tv_MainWeatherContents);
            vMainWeatherState = (TextView)mView.findViewById(R.id.tv_MainWeatherState);
            vMainWeather = (ImageView)mView.findViewById(R.id.IV_MainWeather);
        }
    }

    @Override
    protected View doInBackground(View... voids) {
        if(sGridX != null || sGridY!= null) {
            try {
                URL url = new URL(sUrlStrForm1 + sGridX + sUrlStrForm2 + sGridY);
                boolean bDone = false;
                XmlPullParserFactory dBuilderFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = dBuilderFactory.newPullParser();
                InputStream stream = url.openStream();
                xmlPullParser.setInput(stream, "UTF-8");
                int iEventType = xmlPullParser.getEventType();
                Boolean itemFlag = false;
                String name = null;
                while (iEventType != XmlPullParser.END_DOCUMENT && !bDone) {
                    if (iEventType == XmlPullParser.START_TAG) {
                        name = xmlPullParser.getName();
                        if (name.equals("hour")||name.equals("day")||name.equals("temp")||name.equals("tmx")||name.equals("tmn")||name.equals("sky")
                                ||name.equals("pty")||name.equals("wfKor")||name.equals("wfEn")||name.equals("pop")||name.equals("r12")||name.equals("s12")
                                ||name.equals("ws")||name.equals("wd")||name.equals("wdKor")||name.equals("wdEn")||name.equals("reh")) {
                            itemFlag = true;
                        }
                    }else if (iEventType == XmlPullParser.TEXT && itemFlag) {
                        switch (name) {
                            case "hour":
                                sHour = xmlPullParser.getText();
                                break;
                            case "day":
                                sDay = xmlPullParser.getText();
                                break;
                            case "temp":
                                sTemp = xmlPullParser.getText();
                                break;
                            case "tmx":
                                sTmx = xmlPullParser.getText();
                                break;
                            case "tmn":
                                sTmn = xmlPullParser.getText();
                                break;
                            case "sky":
                                sSky = xmlPullParser.getText();
                                break;
                            case "pty":
                                sPty = xmlPullParser.getText();
                                break;
                            case "wfKor":
                                sWfKor = xmlPullParser.getText();
                                break;
                            case "wfEn":
                                sWfEn = xmlPullParser.getText();
                                break;
                            case "pop":
                                sPop = xmlPullParser.getText();
                                break;
                            case "r12":
                                sR12 = xmlPullParser.getText();
                                break;
                            case "s12":
                                sS12 = xmlPullParser.getText();
                                break;
                            case "ws":
                                sWs = xmlPullParser.getText();
                                break;
                            case "wd":
                                sWd = xmlPullParser.getText();
                                break;
                            case "wdKor":
                                sWdKor = xmlPullParser.getText();
                                break;
                            case "wdEn":
                                sWdEn = xmlPullParser.getText();
                                break;
                            case "reh":
                                sReh = xmlPullParser.getText();
                                bDone = true;
                                break;
                        }
                    }else if(iEventType == xmlPullParser.END_TAG){
                        itemFlag = false;
                    }
                    iEventType = xmlPullParser.next();
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception ee){
                ee.printStackTrace();
            }


            try {
                URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?tmX="
                        +sGridX+"&tmY="+sGridY+"&pageNo=1&numOfRows=10&ServiceKey=y5LIAOJYjMEL7PDenIKZgvnPIGwUF7g9PsEeFKdHMKQoIFTp8z6L9FtiJP96O5KH9ZZXMo0j%2FLxJBXXhDJwAVQ%3D%3D");
                boolean bDone = false;
                XmlPullParserFactory dBuilderFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = dBuilderFactory.newPullParser();
                InputStream stream = url.openStream();
                xmlPullParser.setInput(stream, "UTF-8");
                int iEventType = xmlPullParser.getEventType();
                Boolean itemFlag = false;
                String name = null;
                while (iEventType != XmlPullParser.END_DOCUMENT && !bDone) {
                    if (iEventType == XmlPullParser.START_TAG) {
                        name = xmlPullParser.getName();
                        if (name.equals("stationName")) {
                            itemFlag = true;
                        }
                    }else if (iEventType == XmlPullParser.TEXT && itemFlag) {
                        switch (name) {
                            case "stationName":
                                sStationName = xmlPullParser.getText();
                                break;
                        }
                    }else if(iEventType == xmlPullParser.END_TAG){
                        itemFlag = false;
                    }
                    iEventType = xmlPullParser.next();
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception ee){
                ee.printStackTrace();
            }






            try {
                URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName="
                        +sStationName+"&dataTerm=month&pageNo=1&numOfRows=10&ServiceKey=y5LIAOJYjMEL7PDenIKZgvnPIGwUF7g9PsEeFKdHMKQoIFTp8z6L9FtiJP96O5KH9ZZXMo0j%2FLxJBXXhDJwAVQ%3D%3D");
                boolean bDone = false;
                XmlPullParserFactory dBuilderFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = dBuilderFactory.newPullParser();
                InputStream stream = url.openStream();
                xmlPullParser.setInput(stream, "UTF-8");
                int iEventType = xmlPullParser.getEventType();
                Boolean itemFlag = false;
                String name = null;
                while (iEventType != XmlPullParser.END_DOCUMENT && !bDone) {
                    if (iEventType == XmlPullParser.START_TAG) {
                        name = xmlPullParser.getName();
                        if (name.equals("pm10Grade")) {
                            itemFlag = true;
                        }
                    }else if (iEventType == XmlPullParser.TEXT && itemFlag) {
                        switch (name) {
                            case "pm10Grade":
                                sPm10Grade = xmlPullParser.getText();
                                break;
                        }
                    }else if(iEventType == xmlPullParser.END_TAG){
                        itemFlag = false;
                    }
                    iEventType = xmlPullParser.next();
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception ee){
                ee.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(View aVoid) {
        super.onPostExecute(aVoid);
        vMainWeatherState.setText(sWfKor);
        String sDustNow = null;
        switch (sPm10Grade){
            case "1":
                sDustNow = "좋음";
                break;
            case "2":
                sDustNow = "보통";
                break;
            case "3":
                sDustNow = "나쁨";
                break;
            case "4":
                sDustNow = "매우나쁨";
                break;
        }

        vMainWeatherContents.setText("※ 미세먼지:  "+sDustNow+"\r\n"
                +"※ 현재온도:  "+sTemp+" °C\r\n"
                +"※ 습도:  "+sReh+"%\r\n"
                +"※ 강수확률:  "+sPop+"%\r\n"
                + "※ 풍향:  "+sWdKor+"풍\r\n"
                + "※ 풍속:  " + sWs+"m/s");

        switch (sWfKor){
            case "맑음":
                int iHour = Integer.parseInt(sHour);
                if(iHour<7 || iHour>19)
                    Glide.with(mView).load(R.drawable.sunny_night_icon).into(vMainWeather);
                else
                    Glide.with(mView).load(R.drawable.sunny_icon).into(vMainWeather);
                //vMainWeather.setBackground();
                break;
            case "구름 조금":
            case "구름 많음":
                int iHour2 = Integer.parseInt(sHour);
                if(iHour2<7 || iHour2>19)
                    Glide.with(mView).load(R.drawable.little_cloudy_night_icon).into(vMainWeather);
                else
                    Glide.with(mView).load(R.drawable.little_cloudy_icon).into(vMainWeather);
                break;
            case "흐림":
                Glide.with(mView).load(R.drawable.cloudy_icon).into(vMainWeather);
                break;
            case "비":
                Glide.with(mView).load(R.drawable.rain_icon).into(vMainWeather);
                break;
            case "눈/비":
            case "눈":
                Glide.with(mView).load(R.drawable.snow_icon).into(vMainWeather);
                break;
        }

    }

    public interface WeatherParsingListener{
        public void setTVMainWeatherState(String result);
        public void setTVMainWeatherContents(String result);
    }




    /* TODO - : GPS to Grid Data , */
    private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y ) {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기준점 Y좌표(GRID)


        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == TO_GRID) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }

    class LatXLngY {
        public double lat;
        public double lng;

        public double x;
        public double y;
    }


}
