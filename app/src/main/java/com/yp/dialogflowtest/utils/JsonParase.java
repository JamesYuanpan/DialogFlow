package com.yp.dialogflowtest.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.yp.dialogflowtest.Voice;
import com.yp.dialogflowtest.Weather;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParase {

    //解析天气json
    public static Weather paraseWeather(String json){
        Gson gson = new Gson();
        Weather weather = gson.fromJson(json,Weather.class);
        if(weather != null){
            return weather;
        }
        return null;
    }

    //解析DialogFlow json
    public static String paraseDialogFlow(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject queryResult = jsonObject.optJSONObject("queryResult");
        return queryResult.optString("fulfillmentText");
    }

    //解析语音json
    public static Voice paraseSpeech(String json){
        Gson gson = new Gson();
        Voice voice = gson.fromJson(json, Voice.class);
        return voice;
    }
}
