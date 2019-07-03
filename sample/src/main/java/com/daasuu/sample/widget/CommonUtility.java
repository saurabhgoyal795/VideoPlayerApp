package com.daasuu.sample.widget;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by saurabhgoyal on 13/03/18.
 */

public class CommonUtility {
    public static boolean isDebugModeOn = true;
    public static final String TAG = "Sgit";

    public static boolean isConnectedToInternet(Context context) {
        if(context == null)
            return false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if(cm == null)
            return false;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
    public static void printStackTrace(Throwable e) {
        printStackTrace(TAG, e);
    }

    public static void printStackTrace(String tag, Throwable e) {
        if(tag == null || tag.trim().equals("")) {
            tag = TAG;
        }
        String stackTrace = "";
        for(StackTraceElement element: e.getStackTrace()) {
            stackTrace += element.getClassName() + ": " +
                    element.getMethodName() + ": " +
                    element.getLineNumber() + "\n";
        }
        Log.e(tag, "Exception: " + e.getClass());
        Log.e(tag, "Localized Message: " + e.getLocalizedMessage());
        Log.e(tag, "Message: " + e.getMessage());
        Log.e(tag, "StackTrace: " + stackTrace);
        if(e.getCause() != null) {
            printStackTrace(tag, e.getCause());
        }
    }

    public static String replaceVariable(String dataString, JSONObject appStringObject){
        if(dataString!="" &&  dataString!=null){
            try {

                while (dataString.toLowerCase().indexOf("<default-") > -1) {
                    dataString = dataString.replace("<default-", "<");
                }


                while (dataString.toLowerCase().indexOf("<name>") > -1 || dataString.toLowerCase().indexOf("<name-static>") > -1) {
                    dataString = dataString.replace("<name>", appStringObject.getString("1895"));
                    dataString = dataString.replace("<name-static>", appStringObject.getString("1895"));
                }
                while (dataString.toLowerCase().indexOf("<country-name-static>") > -1 || dataString.toLowerCase().indexOf("<country-name>") > -1) {
                    dataString = dataString.replace("<country-name-static>", appStringObject.getString("1896"));
                    dataString = dataString.replace("<country-name>", appStringObject.getString("1896"));
                }
                while (dataString.toLowerCase().indexOf("<country-nationality>") > -1) {
                    dataString = dataString.replace("<country-nationality>", appStringObject.getString("1897"));
                }
                while (dataString.toLowerCase().indexOf("<capital-city-name>") > -1) {
                    dataString = dataString.replace("<capital-city-name>", appStringObject.getString("1898"));
                }
                while (dataString.toLowerCase().indexOf("<country-currency>") > -1) {
                    dataString = dataString.replace("<country-currency>", appStringObject.getString("1899"));
                }
                while (dataString.toLowerCase().indexOf("<country-language-name>") > -1) {
                    dataString = dataString.replace("<country-language-name>", appStringObject.getString("1900"));
                }
                while (dataString.toLowerCase().indexOf("<language-name2>") > -1) {
                    dataString = dataString.replace("<language-name2>", appStringObject.getString("1901"));
                }
                while (dataString.toLowerCase().indexOf("<language-name3>") > -1) {
                    dataString = dataString.replace("<language-name3>", appStringObject.getString("1902"));
                }
                while (dataString.toLowerCase().indexOf("<state-name>") > -1) {
                    dataString = dataString.replace("<state-name>", appStringObject.getString("1903"));
                }
                while (dataString.toLowerCase().indexOf("<company-name>") > -1) {
                    dataString = dataString.replace("<company-name>", appStringObject.getString("1904"));
                }
                while (dataString.toLowerCase().indexOf("<actor-name>") > -1) {
                    dataString = dataString.replace("<actor-name>", appStringObject.getString("1905"));
                }
                while (dataString.toLowerCase().indexOf("<actor-surname>") > -1) {
                    dataString = dataString.replace("<actor-surname>", appStringObject.getString("1906"));
                }
                while (dataString.toLowerCase().indexOf("<actress-name>") > -1) {
                    dataString = dataString.replace("<actress-name>", appStringObject.getString("1907"));
                }
                while (dataString.toLowerCase().indexOf("<actress-surname>") > -1) {
                    dataString = dataString.replace("<actress-surname>", appStringObject.getString("1908"));
                }
                while (dataString.toLowerCase().indexOf("<businessman-name>") > -1) {
                    dataString = dataString.replace("<businessman-name>", appStringObject.getString("1909"));
                }
                while (dataString.toLowerCase().indexOf("<businessman-surname>") > -1) {
                    dataString = dataString.replace("<businessman-surname>", appStringObject.getString("1910"));
                }
                while (dataString.toLowerCase().indexOf("<actress-name2>") > -1) {
                    dataString = dataString.replace("<actress-name2>", appStringObject.getString("1911"));
                }
                while (dataString.toLowerCase().indexOf("<actress-surname2>") > -1) {
                    dataString = dataString.replace("<actress-surname2>", appStringObject.getString("1912"));
                }
                while (dataString.toLowerCase().indexOf("<city-name-static>") > -1 || dataString.toLowerCase().indexOf("<city-name>") > -1) {
                    dataString = dataString.replace("<city-name-static>", appStringObject.getString("1913"));
                    dataString = dataString.replace("<city-name>", appStringObject.getString("1913"));
                }
                while (dataString.toLowerCase().indexOf("<city-area>") > -1) {
                    dataString = dataString.replace("<city-area>", appStringObject.getString("1914"));
                }
                while (dataString.toLowerCase().indexOf("<city-landmark>") > -1) {
                    dataString = dataString.replace("<city-landmark>", appStringObject.getString("1915"));
                }
                while (dataString.toLowerCase().indexOf("<city-name1>") > -1) {
                    dataString = dataString.replace("<city-name1>", appStringObject.getString("1916"));
                }
                while (dataString.toLowerCase().indexOf("<city-name2>") > -1) {
                    dataString = dataString.replace("<city-name2>", appStringObject.getString("1917"));
                }
                while (dataString.toLowerCase().indexOf("<city-name3>") > -1) {
                    dataString = dataString.replace("<city-name3>", appStringObject.getString("1918"));
                }
                while (dataString.toLowerCase().indexOf("<female-name>") > -1) {
                    dataString = dataString.replace("<female-name>", appStringObject.getString("1919"));
                }
                while (dataString.toLowerCase().indexOf("<female-surname>") > -1) {
                    dataString = dataString.replace("<female-surname>", appStringObject.getString("1920"));
                }
                while (dataString.toLowerCase().indexOf("<female-name2>") > -1) {
                    dataString = dataString.replace("<female-name2>", appStringObject.getString("1921"));
                }
                while (dataString.toLowerCase().indexOf("<female-name3>") > -1) {
                    dataString = dataString.replace("<female-name3>", appStringObject.getString("1922"));
                }
                while (dataString.toLowerCase().indexOf("<male-name>") > -1) {
                    dataString = dataString.replace("<male-name>", appStringObject.getString("1923"));
                }
                while (dataString.toLowerCase().indexOf("<male-surname>") > -1) {
                    dataString = dataString.replace("<male-surname>", appStringObject.getString("1924"));
                }
                while (dataString.toLowerCase().indexOf("<male-name2>") > -1) {
                    dataString = dataString.replace("<male-name2>", appStringObject.getString("1925"));
                }
                while (dataString.toLowerCase().indexOf("<male-name3>") > -1) {
                    dataString = dataString.replace("<male-name3>", appStringObject.getString("1926"));
                }
                while (dataString.toLowerCase().indexOf("<friend-name-static>") > -1 || dataString.toLowerCase().indexOf("<friend-name>") > -1) {
                    dataString = dataString.replace("<friend-name-static>", appStringObject.getString("1927"));
                    dataString = dataString.replace("<friend-name>", appStringObject.getString("1927"));
                }
                while (dataString.toLowerCase().indexOf("<friend-name>") > -1) {
                    dataString = dataString.replace("<friend-name>", appStringObject.getString("1927"));
                    dataString = dataString.replace("<FRIEND-NAME>", appStringObject.getString("1927"));
                }
                while (dataString.toLowerCase().indexOf("<foreigncountry-name>") > -1) {
                    dataString = dataString.replace("<foreigncountry-name>", appStringObject.getString("1928"));
                }
                while (dataString.toLowerCase().indexOf("<smallcar-name>") > -1) {
                    dataString = dataString.replace("<smallcar-name>", appStringObject.getString("1929"));
                }
                while (dataString.toLowerCase().indexOf("<famousmale-name>") > -1) {
                    dataString = dataString.replace("<famousmale-name>", appStringObject.getString("1930"));
                }
                //end

                //replace default native strings
                while (dataString.toLowerCase().indexOf("<name-static-native>") > -1 || dataString.toLowerCase().indexOf("<name-native>") > -1) {
                    dataString = dataString.replace("<name-static-native>", appStringObject.getString("1931"));
                    dataString = dataString.replace("<name-native>", appStringObject.getString("1931"));
                }
                while (dataString.toLowerCase().indexOf("<country-name-native>") > -1 || dataString.toLowerCase().indexOf("<country-name-static-native>") > -1) {
                    dataString = dataString.replace("<country-name-native>", appStringObject.getString("1932"));
                    dataString = dataString.replace("<country-name-static-native>", appStringObject.getString("1932"));
                }
                while (dataString.toLowerCase().indexOf("<country-nationality-native>") > -1) {
                    dataString = dataString.replace("<country-nationality-native>", appStringObject.getString("1933"));
                }
                while (dataString.toLowerCase().indexOf("<capital-city-name-native>") > -1) {
                    dataString = dataString.replace("<capital-city-name-native>", appStringObject.getString("1934"));
                }
                while (dataString.toLowerCase().indexOf("<country-currency-native>") > -1) {
                    dataString = dataString.replace("<country-currency-native>", appStringObject.getString("1935"));
                }
                while (dataString.toLowerCase().indexOf("<country-language-name-native>") > -1) {
                    dataString = dataString.replace("<country-language-name-native>", appStringObject.getString("1936"));
                }
                while (dataString.toLowerCase().indexOf("<language-name2-native>") > -1) {
                    dataString = dataString.replace("<language-name2-native>", appStringObject.getString("1937"));
                }
                while (dataString.toLowerCase().indexOf("<language-name3-native>") > -1) {
                    dataString = dataString.replace("<language-name3-native>", appStringObject.getString("1938"));
                }
                while (dataString.toLowerCase().indexOf("<state-name-native>") > -1) {
                    dataString = dataString.replace("<state-name-native>", appStringObject.getString("1939"));
                }
                while (dataString.toLowerCase().indexOf("<company-name-native>") > -1) {
                    dataString = dataString.replace("<company-name-native>", appStringObject.getString("1940"));
                }
                while (dataString.toLowerCase().indexOf("<actor-name-native>") > -1) {
                    dataString = dataString.replace("<actor-name-native>", appStringObject.getString("1941"));
                }
                while (dataString.toLowerCase().indexOf("<actor-surname-native>") > -1) {
                    dataString = dataString.replace("<actor-surname-native>", appStringObject.getString("1942"));
                }
                while (dataString.toLowerCase().indexOf("<actress-name-native>") > -1) {
                    dataString = dataString.replace("<actress-name-native>", appStringObject.getString("1943"));
                }
                while (dataString.toLowerCase().indexOf("<actress-surname-native>") > -1) {
                    dataString = dataString.replace("<actress-surname-native>", appStringObject.getString("1944"));
                }
                while (dataString.toLowerCase().indexOf("<businessman-name-native>") > -1) {
                    dataString = dataString.replace("<businessman-name-native>", appStringObject.getString("1945"));
                }
                while (dataString.toLowerCase().indexOf("<businessman-surname-native>") > -1) {
                    dataString = dataString.replace("<businessman-surname-native>", appStringObject.getString("1946"));
                }
                while (dataString.toLowerCase().indexOf("<actress-name2-native>") > -1) {
                    dataString = dataString.replace("<actress-name2-native>", appStringObject.getString("1947"));
                }
                while (dataString.toLowerCase().indexOf("<actress-surname2-native>") > -1) {
                    dataString = dataString.replace("<actress-surname2-native>", appStringObject.getString("1948"));
                }
                while (dataString.toLowerCase().indexOf("<city-name-static-native>") > -1 || dataString.toLowerCase().indexOf("<city-name-native>") > -1) {
                    dataString = dataString.replace("<city-name-static-native>", appStringObject.getString("1949"));
                    dataString = dataString.replace("<city-name-native>", appStringObject.getString("1949"));
                }
                while (dataString.toLowerCase().indexOf("<city-area-native>") > -1) {
                    dataString = dataString.replace("<city-area-native>", appStringObject.getString("1950"));
                }
                while (dataString.toLowerCase().indexOf("<city-landmark-native>") > -1) {
                    dataString = dataString.replace("<city-landmark-native>", appStringObject.getString("1951"));
                }
                while (dataString.toLowerCase().indexOf("<city-name1-native>") > -1) {
                    dataString = dataString.replace("<city-name1-native>", appStringObject.getString("1952"));
                }
                while (dataString.toLowerCase().indexOf("<city-name2-native>") > -1) {
                    dataString = dataString.replace("<city-name2-native>", appStringObject.getString("1953"));
                }
                while (dataString.toLowerCase().indexOf("<city-name3-native>") > -1) {
                    dataString = dataString.replace("<city-name3-native>", appStringObject.getString("1954"));
                }
                while (dataString.toLowerCase().indexOf("<female-name-native>") > -1) {
                    dataString = dataString.replace("<female-name-native>", appStringObject.getString("1955"));
                }

                while (dataString.toLowerCase().indexOf("<female-surname-native>") > -1) {
                    dataString = dataString.replace("<female-surname-native>", appStringObject.getString("1956"));
                }
                while (dataString.toLowerCase().indexOf("<female-name2-native>") > -1) {
                    dataString = dataString.replace("<female-name2-native>", appStringObject.getString("1957"));
                }
                while (dataString.toLowerCase().indexOf("<female-name3-native>") > -1) {
                    dataString = dataString.replace("<female-name3-native>", appStringObject.getString("1958"));
                }
                while (dataString.toLowerCase().indexOf("<male-name-native>") > -1) {
                    dataString = dataString.replace("<male-name-native>", appStringObject.getString("1959"));
                }
                while (dataString.toLowerCase().indexOf("<male-surname-native>") > -1) {
                    dataString = dataString.replace("<male-surname-native>", appStringObject.getString("1960"));
                }
                while (dataString.toLowerCase().indexOf("<male-name2-native>") > -1) {
                    dataString = dataString.replace("<male-name2-native>", appStringObject.getString("1961"));
                }
                while (dataString.toLowerCase().indexOf("<male-name3-native>") > -1) {
                    dataString = dataString.replace("<male-name3-native>", appStringObject.getString("1962"));
                }
                while (dataString.toLowerCase().indexOf("<friend-name-static-native>") > -1 || dataString.toLowerCase().indexOf("<friend-name-native>") > -1) {
                    dataString = dataString.replace("<friend-name-static-native>", appStringObject.getString("1963"));
                    dataString = dataString.replace("<friend-name-native>", appStringObject.getString("1963"));
                }
                while (dataString.toLowerCase().indexOf("<foreigncountry-name-native>") > -1) {
                    dataString = dataString.replace("<foreigncountry-name-native>", appStringObject.getString("1964"));
                }
                while (dataString.toLowerCase().indexOf("<smallcar-name-native>") > -1) {
                    dataString = dataString.replace("<smallcar-name-native>", appStringObject.getString("1965"));
                }
                while (dataString.toLowerCase().indexOf("<famousmale-name-native>") > -1) {
                    dataString = dataString.replace("<famousmale-name-native>", appStringObject.getString("1966"));
                }
                //end

                while (dataString.toLowerCase().indexOf("<country-name>") > -1) {
                    dataString = dataString.replace("<country-name>", "<span class='mycountryClass variableClassForUrdu'></span>");
                    dataString = dataString.replace("<COUNTRY-NAME>", "<span class='mycountryClass variableClassForUrdu'></span>");
                }
                while (dataString.toLowerCase().indexOf("<email-address>") > -1) {
                    dataString = dataString.replace("<email-address>", "<span class='myemailClass variableClassForUrdu'></span>");
                    dataString = dataString.replace("<EMAIL-ADDRESS>", "<span class='myemailClass variableClassForUrdu'></span>");
                }
                while (dataString.toLowerCase().indexOf("<phone-number>") > -1) {
                    dataString = dataString.replace("<phone-number>", "<span class='myphonenumberClass variableClassForUrdu'></span>");
                    dataString = dataString.replace("<PHONE-NUMBER>", "<span class='myphonenumberClass variableClassForUrdu'></span>");
                }
                while (dataString.toLowerCase().indexOf("<profession>") > -1) {
                    dataString = dataString.replace("<profession>", "<span class='professionClass variableClassForUrdu'></span>");
                    dataString = dataString.replace("<PROFESSION>", "<span class='professionClass variableClassForUrdu'></span>");
                }
                while (dataString.toLowerCase().indexOf("<profession-native>") > -1) {
                    dataString = dataString.replace("<profession-native>", "<span class='professionNativeClass variableClassForUrdu'></span>");
                    dataString = dataString.replace("<PROFESSION-NATIVE>", "<span class='professionNativeClass variableClassForUrdu'></span>");
                }
                while (dataString.toLowerCase().indexOf("<profession-article>") > -1) {
                    dataString = dataString.replace("<profession-article>", "<span class='professionArticleClass variableClassForUrdu'></span>");
                    dataString = dataString.replace("<PROFESSION-ARTICLE>", "<span class='professionArticleClass variableClassForUrdu'></span>");
                }
                while (dataString.toLowerCase().indexOf("<profession-article-wrong>") > -1) {
                    dataString = dataString.replace("<profession-article-wrong>", "<span class='professionArticleWrongClass variableClassForUrdu'></span>");
                    dataString = dataString.replace("<PROFESSION-ARTICLE-WRONG>", "<span class='professionArticleWrongClass variableClassForUrdu'></span>");
                }
            }catch (Exception e){

            }
        }
        dataString = "<span>"+dataString+"</span>";
        return dataString;

    }

}
