package com.example.wayne_mac.mobileiot2phone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements AsyncResponse {

    private TextView Location,Temp,Humidity,CurrenTime,PM25,CO2;
    private String method="";
    private int TT=1;


    String[] mLocaiton = new String[0];
    String[] mMac =new String[0];
    String[] mTemp = new String[0];
    String[] mHumidity =new String[0];
    String[] mTime = new String[0];
    String[] mPM25 =new String[0];
    String[] mco2 =new String[0];

    private int dataindex =0;
    private int DataRecords=0;


    float x1,y1;
    float x2,y2;


    private Timer timer;
    private TimerTask timerTask = new TimerTask()
    {

        @Override
        public void run()
        {

                // Go to Search data from PHP Server by BackgroudTask
                BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this);
                method = "SearchIOTStatus";
                backgroundTask.execute(method);


        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        BackgroundTask.delegate = this;
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Context context = this;

        Location = (TextView) findViewById(R.id.LocationValue);

        Temp = (TextView) findViewById(R.id.Tempeature);
        Humidity = (TextView) findViewById(R.id.Humidity);
        CurrenTime = (TextView) findViewById(R.id.Time);

        PM25 = (TextView) findViewById(R.id.PM251);

        CO2 = (TextView) findViewById(R.id.co2);

        if (InternetConnection.checkConnection(context))

        {
            // Go to Search data from PHP Server by BackgroudTask
            BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this);
            method = "SearchIOTStatus";
            backgroundTask.execute(method);



            start();




        } else   if (!InternetConnection.checkConnection(context))
        {


            Toast.makeText(this, "Can't Access Internet, Please check your Setting !! ", Toast.LENGTH_SHORT).show();


        }




    }


    public boolean onTouchEvent(MotionEvent touchevent)

    {


            switch (touchevent.getAction()) {
// when user first touches the screen we get x and y coordinate
                case MotionEvent.ACTION_DOWN: {
                    x1 = touchevent.getX();
                    y1 = touchevent.getY();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    x2 = touchevent.getX();
                    y2 = touchevent.getY();
//if left to right sweep event on screen
                    if (x1 < x2) {


                        dataindex--;
                        if (dataindex <= 0) {
                            Toast.makeText(this, "最前 IOT Status!!!", Toast.LENGTH_SHORT).show();
                            dataindex = 0;

                        }

                        Float fTmp1 = Float.parseFloat(mTemp[dataindex]);

                        Float fHum1 = Float.parseFloat(mHumidity[dataindex]);


                        if (fTmp1 >= 27)
                            Temp.setTextColor(Color.parseColor("#FF0000"));
                        else
                            Temp.setTextColor(Color.parseColor("#0000FF"));


                        if (fHum1 >= 72)
                            Humidity.setTextColor(Color.parseColor("#FF0000"));
                        else
                            Humidity.setTextColor(Color.parseColor("#0000FF"));


                        Temp.setText(mTemp[dataindex] + " ℃");
                        Humidity.setText(mHumidity[dataindex] + "  %");
                        Location.setText(mLocaiton[dataindex]);
                        PM25.setText(mPM25[dataindex]);
                        CO2.setText(mco2[dataindex]);

                        CurrenTime.setText(mTime[dataindex]);

                        //      timer.cancel();
                        //      timer = null;
                        //      Intent go = new Intent(MainActivity.this,SecondIOTPage.class);
                        //      startActivityForResult(go,0);
                    }
// if right to left sweep event on screen
                    if (x1 > x2) {

                        dataindex++;

                        if (dataindex > DataRecords)

                        {
                            Toast.makeText(this, "最後 IOT Status", Toast.LENGTH_SHORT).show();
                            dataindex = DataRecords;
                        }

                        Float fTmp = Float.parseFloat(mTemp[dataindex]);

                        Float fHum = Float.parseFloat(mHumidity[dataindex]);


                        if (fTmp >= 27)
                            Temp.setTextColor(Color.parseColor("#FF0000"));
                        else
                            Temp.setTextColor(Color.parseColor("#0000FF"));


                        if (fHum >= 72)
                            Humidity.setTextColor(Color.parseColor("#FF0000"));
                        else
                            Humidity.setTextColor(Color.parseColor("#0000FF"));

                        Temp.setText(mTemp[dataindex] + " ℃");
                        Humidity.setText(mHumidity[dataindex] + "  %");
                        Location.setText(mLocaiton[dataindex]);
                        PM25.setText(mPM25[dataindex]);
                        CO2.setText(mco2[dataindex]);
                        CurrenTime.setText(mTime[dataindex]);

                        //   Intent go = new Intent(MainActivity.this,SecondIOTPage.class);
                        //  startActivityForResult(go,0);
                    }
// if UP to Down sweep event on screen
                    if (y1 < y2) {
//Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                    }
//if Down to UP sweep event on screen
                    if (y1 > y2) {
// Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
            return false;



    }





    @Override
    public void onBackPressed() {

    }





    public void start() {
        if(timer != null) {


            return;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 10000);

    }

    public void stop() {
        timer.cancel();
        timer = null;
    }


    @Override
    public void processFinish(String output)
    {
        if (method.equals("SearchIOTStatus"))

        {

            JSONArray jArray = null;
            try {
                jArray = new JSONArray(output);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject json_data = null;

            mLocaiton = new String[jArray.length()];
            mMac = new String[jArray.length()];
            mTemp = new String[jArray.length()];
            mHumidity = new String[jArray.length()];
            mTime = new String[jArray.length()];
            mPM25 = new String[jArray.length()];
            mco2 = new String[jArray.length()];
            DataRecords =  jArray.length()-1;



            //    $sql = "SELECT temp,hum,mac,location FROM `esp2` ORDER BY `esp2`.`id` ASC ;";


            for (int index = 0; index < jArray.length(); index++)
            {
                try {
                    json_data = jArray.getJSONObject(index);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                try {

                    mTime[index] = json_data.getString("time");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                try {

                    mTemp[index] = json_data.getString("temp");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    mHumidity[index] = json_data.getString("hum");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    mMac[index] = json_data.getString("mac");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                try {
                    mco2[index] = json_data.getString("co2");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

                try {
                    mPM25[index] = json_data.getString("pm25");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }



                try {
                    mLocaiton[index] = json_data.getString("location");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

            }//for(int i=0; i < jArray.length() ; i++)


            Float fTmp= Float.parseFloat( mTemp[dataindex]);

            Float fHum= Float.parseFloat( mHumidity[dataindex]);



            if(fTmp>=27)
                Temp.setTextColor(Color.parseColor("#FF0000"));
            else
                Temp.setTextColor(Color.parseColor("#0000FF"));




            if(fHum>=72)
                Humidity.setTextColor(Color.parseColor("#FF0000"));
            else
                Humidity.setTextColor(Color.parseColor("#0000FF"));



            Calendar calendar = Calendar.getInstance();
            String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime());

            //  CurrenTime1.setText(mTime[1]);


            Temp.setText(mTemp[dataindex]+" ℃");
            Humidity.setText(mHumidity[dataindex]+"  %");
            Location.setText(mLocaiton[dataindex]);
            PM25.setText(mPM25[dataindex]);
            CO2.setText(mco2[dataindex]);
               CurrenTime.setText(mTime[dataindex]);

         //   CurrenTime.setText(timeNow);


        }// if (method.equals("SearchIOTStatus"))




    }//	public void processFinish(String output)



}



