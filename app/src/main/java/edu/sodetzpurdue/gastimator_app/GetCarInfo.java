package edu.sodetzpurdue.gastimator_app;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * {insert meaningful descrition here}
 *
 * @author Ken Sodetz
 * @since 9/30/2017
 */

public class GetCarInfo{

    private final String APIKEY = "A8874x8oBWR0GdYXGccI2tYFFULXur7a";

    /**
     * Default Constructor
     */
    public GetCarInfo(){

    }

    /**
     * Sends the HTML request to get the vehicle data based on given parameters
     * @param make, make of the vehicle
     * @param model, model of the vehicle
     * @param year, year of the vehicle
     * @return responseBody, string response of the API call
     */
    public String shineConnect (String make, String model, String year)
    {
        String charset = "UTF-8";
        InputStream response = null;
        URLConnection connection;
        String url = "https://apis.solarialabs.com/shine/v1/vehicle-stats/specs?";
        try {
            connection = new URL(url+"make="+make+"&model="+model+"&year="+year+"&apikey="+APIKEY).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            response = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(Scanner scanner = new Scanner(response)) {
                 return scanner.useDelimiter("\\A").next();
        } catch (Exception e){
            System.err.println("Response is null");
            return "[]";
        }
    }


    /**
     * Parses City MPG from api call
     * @param apiCallString api string to be parsed
     * @return city mpg as a double
     */
    public double getCityMPG(String apiCallString)
    {
        String response = apiCallString.substring(apiCallString.indexOf("City_Unadj_Conventional_Fuel"), apiCallString.indexOf("Hwy_Unadj_Conventional_Fuel"));
        response = response.substring(response.indexOf(":") + 1, response.indexOf(","));
        return Double.parseDouble(response);
    }

    /**
     * Parses Highway MPG from api call
     * @param apiCallString api string to be parsed
     * @return highway mpg as a double
     */
    public double getHighwayMPG(String apiCallString)
    {
        String response = apiCallString.substring(apiCallString.indexOf("Hwy_Unadj_Conventional_Fuel"), apiCallString.indexOf("Air_AspirMethod"));
        response = response.substring(response.indexOf(":") + 1, response.indexOf(","));
        return Double.parseDouble(response);
    }
}
