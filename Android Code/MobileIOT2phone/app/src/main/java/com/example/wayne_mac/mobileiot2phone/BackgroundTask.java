package com.example.wayne_mac.mobileiot2phone;

/**
 * Created by wayne-mac on 2018/2/2.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class BackgroundTask extends AsyncTask<String,String,String> {

    public static AsyncResponse delegate = null;


    //  private ProgressDialog pDialog;
    //    public static final int progress_bar_type = 0;

    Context ctx;
    BackgroundTask(Context ctx)
    {
        this.ctx =ctx;
    }


    protected void onPreExecute() {
        super.onPreExecute();
        //        showDialog(progress_bar_type);
    }


    @Override
    protected String doInBackground(String... params)
    {

        //     for DQE NAS  Web test



  //xxx.xxx.xx  your Owner DNS adress.
        String SearchIOTStatus_url = "https://xxx.xxx.xx/ESP8266/SearchIOTStatus.php";




        String method = params[0];








        if(method.equals("SearchIOTStatus"))
        {

            //   String login_pass = params[2];

            try {
                URL url = new URL(SearchIOTStatus_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
              //  httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                /*
                String data = URLEncoder.encode("uuid","UTF-8")+"="+ URLEncoder.encode(UUID,"UTF-8");


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                */
                InputStream inputStream = httpURLConnection.getInputStream();


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                //  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"ISO-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response+= line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return response;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }      // if(method.equals("SearchUUIDforStock"))





        return null;
    } // protected String doInBackground(String... params)

	/*
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case progress_bar_type: // we set this to 0
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
            return pDialog;
        default:
            return null;
        }
    }

	*/






    @Override
    protected void onProgressUpdate(String... progress)
    {
        super.onProgressUpdate(progress);
        //     pDialog.setProgress(Integer.parseInt(progress[0]));

        //  Toast.makeText(ctx, "Data Inserting.....,", Toast.LENGTH_LONG).show();



    }

    @Override
    protected void onPostExecute(String result)
    {


            if(result.equals("****?êÂ?‰øÆÊîπFoodResume*****"))
        {
            delegate.processFinish(result);
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }


        else
        {
            delegate.processFinish(result);

//	     alertDialog.setMessage(result);
//	      alertDialog.show();


        }
    }

}//public class BackgroundTask extends   AsyncTask<String,String,String>








