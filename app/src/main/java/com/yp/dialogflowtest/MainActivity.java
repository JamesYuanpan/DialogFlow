package com.yp.dialogflowtest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.Voice;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.yp.dialogflowtest.utils.JsonParase;
import com.yp.dialogflowtest.utils.SpeechUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //dialogflow URL
    private static final String URL = "https://dialogflow.googleapis.com/v2/projects/foooofirstagent/agent/sessions/e067fc25-a1ed-d81b-f38e-ba088e37e667:detectIntent";
    //天气api URL
    private static final String WEATHER_URL = "https://free-api.heweather.net/s6/weather/forecast";
    //天气key
    private static final String WEATHER_KEY = "f9680f064aaf4647a7bb58ec6c8d3898";

    private HashMap<String, String> map = new LinkedHashMap<>();

    EditText editText;
    RecyclerView chatRecycler;
    Button btn_press,btn_send;
    ImageButton textTospeech,speechTotext;
    List<Message> messageList = new ArrayList<>();
    TalkAdapter adapter;
    String string;
    Weather weather;
    String dialogFlowDate;
    SpeechSynthesizer mStt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpeechUtility.createUtility(this,SpeechConstant.APPID+"=5cad5f74");

        requestPermissions();

        initView();


    }

    //初始化view
    private void initView(){
//        mToast = Toast.makeText(MainActivity.this,"",Toast.LENGTH_LONG);

        editText = (EditText) findViewById(R.id.edit_query);
        chatRecycler = (RecyclerView) findViewById(R.id.chat_recycler);

        mStt = SpeechSynthesizer.createSynthesizer(this,mInitListener);

        adapter = new TalkAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        chatRecycler.setLayoutManager(layoutManager);
        chatRecycler.setHasFixedSize(true);
        chatRecycler.setAdapter(adapter);

        textTospeech = (ImageButton) findViewById(R.id.textTospeech);
        speechTotext = (ImageButton) findViewById(R.id.speechTotext);
        btn_press = (Button) findViewById(R.id.btn_press);
        btn_send = (Button) findViewById(R.id.btn_send);

        textTospeech.setOnClickListener(this);
        speechTotext.setOnClickListener(this);
        btn_press.setOnClickListener(this);
        btn_send.setOnClickListener(this);
//                new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                String str = editText.getText().toString();
////                if(TextUtils.isEmpty(str)){
////                    Toast.makeText(MainActivity.this,"你输入的内容为空",Toast.LENGTH_LONG).show();
////                }else {
////                    setShowText(str,"USER");
////                    editText.setText("");
////                    getResponseText(str);
////                }
//                initSpeech(MainActivity.this);
//            }

    }

    //初始化识别UI
    public void initSpeech(final Context context){

        if (mStt.isSpeaking()){
            mStt.stopSpeaking();
        }

        final RecognizerDialog mDialog = new RecognizerDialog(context,mInitListener);
        map.clear();

        mDialog.setParameter(SpeechConstant.PARAMS,null);

        mDialog.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);
        mDialog.setParameter(SpeechConstant.RESULT_TYPE,"json");

        mDialog.setParameter(SpeechConstant.VAD_BOS,"5000");
        mDialog.setParameter(SpeechConstant.VAD_EOS,"1000");
        mDialog.setParameter(SpeechConstant.ASR_PTT,"1");

        mDialog.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mDialog.setParameter(SpeechConstant.ASR_AUDIO_PATH,Environment.getExternalStorageDirectory()+"/dialogflowtest/iat.wav");

        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT,"mandarin");
//        mDialog.setParameter(SpeechConstant.ACCENT,"cantonese");

        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {

                String recogStr = printResult(recognizerResult);

                Log.d("MainMain",recognizerResult.getResultString());

                if(!b){
                    if(!TextUtils.isEmpty(recogStr)){
//                        setShowText(recogStr,"USER");
                        Message message = new Message("USER",recogStr,Message.Type.USER);
                        messageList.add(message);
                        adapter.notifyDataSetChanged();
                        chatRecycler.smoothScrollToPosition(adapter.getItemCount()-1);

                        Log.d("MainMain",recogStr);
                        getResponseText(recogStr);
                    }
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                if(speechError == null){
//                    showTip("合成成功");
                }else {
//                    showTip(speechError.getPlainDescription(true));
                }
            }
        });
        mDialog.show();
        TextView txt = mDialog.getWindow().getDecorView().findViewWithTag("textlink");
        txt.setText("点下试试");
        txt.setTextColor(Color.CYAN);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"别闹，赶紧说!",Toast.LENGTH_LONG).show();
            }
        });
    }

    InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int i) {
            if(i != ErrorCode.SUCCESS){
//                showTip("初始化失败，错误码:"+i);
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mStt != null){
            mStt.destroy();
        }
    }

    //输出文字到文本框
    public String printResult(RecognizerResult recognizerResult){
        parseVoice(recognizerResult);

        StringBuffer sb = new StringBuffer();
        for(String key : map.keySet()){
            sb.append(map.get(key));
        }
        return sb.toString();
    }

    //解析识别结果
    public void parseVoice (RecognizerResult recognizerResult){

        com.yp.dialogflowtest.Voice voice = JsonParase.paraseSpeech(recognizerResult.getResultString());

        StringBuffer sb = new StringBuffer();

        ArrayList<com.yp.dialogflowtest.Voice.WSBean> wsBeans = voice.ws;

        for (com.yp.dialogflowtest.Voice.WSBean wsBean : wsBeans){
            sb.append(wsBean.cw.get(0).w);
        }

        String sn = voice.sn;

        map.put(sn,sb.toString());

    }



    //显示内容
//    private void setShowText(String msg, String person){
//        showText.append(person+"说："+msg+"\n");
//    }

    //得到返回text
//    private String getText(String json) throws JSONException {
//        JSONObject jsonObject = new JSONObject(json);
//        JSONObject queryResult = jsonObject.optJSONObject("queryResult");
//        return queryResult.optString("fulfillmentText");
//    }

    //得到dialogflow返回信息
    private void getResponseText(String msg){

        String param = "{\"queryInput\":{\"text\":{\"text\":\""+msg+"\",\"languageCode\":\"zh-cn\"}},\"queryParams\":{\"timeZone\":\"Asia/Shanghai\"}}";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，

        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, param);

//            MultipartBody multipartBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("params",body)
//                    .build();

            Request request = new Request.Builder()
                    .url(URL)
                    .addHeader("Authorization","Bearer ya29.c.EloIB6WoaPzJqD3KIuUZgDZSy50fhHOY3BwmO0Ba1_yiW4t1dQxUOR-eYiSvBJKlvya36-xDojfaNhWn04QP2pprZy64-KgMSC7FPv2XOsP85XMjv2JIqMTVSxc")
                    .addHeader("Content-Type","application/json;charset:utf-8")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Toast.makeText(MainActivity.this,"出错了",Toast.LENGTH_LONG).show();
                    Log.d("MainMain","失败");
                }

                @Override
                public void onResponse(Call call, final Response response){
                    if (response.isSuccessful()){
                        Log.d("MainMain","成功");
                        try {
                            string = JsonParase.paraseDialogFlow(response.body().string());
                            Log.d("MainMain",string);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if(string.contains("Weather")){
                                        String cityName = string.split(":")[1];
                                        dialogFlowDate = string.split(":")[2];
                                        getWeatherInfo(cityName);
                                    }else {
//                                        setShowText(string,"ROBOT");

                                        if (mStt.isSpeaking()){
                                            mStt.stopSpeaking();
                                        }

                                        if (string == null){
                                            string = "不好意思，我听不懂这句话";
                                        }

                                        Message message = new Message("ROBOT",string,Message.Type.ROBOT_NORMAL);
                                        messageList.add(message);
                                        adapter.notifyDataSetChanged();
                                        chatRecycler.smoothScrollToPosition(adapter.getItemCount()-1);

                                        SpeechUtil.setSynParam(mStt);
                                        mStt.startSpeaking(message.getMsg(),SpeechUtil.mSynthesizerListener);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            response.body().close();
                        }

                    }else {
                        Log.d("MainMain","失败");
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //得到天气信息
    private void getWeatherInfo(String cityName){

        try {
            String wUrl = WEATHER_URL+"?location="+URLEncoder.encode(cityName,"utf-8")+"&key="+WEATHER_KEY;

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(wUrl)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(MainActivity.this,"查询天气出错了",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        weather = JsonParase.paraseWeather(response.body().string());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                setShowText(string,"ROBOT");
                                buildWeatherMsg(weather, dialogFlowDate);
                            }
                        });
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void buildWeatherMsg(Weather weather, String dialogFlowDate){

        boolean hasDate = false;

        for (Weather.HeWeather.DailyForecast dailyForecast : weather.HeWeather6.get(0).daily_forecast){
            if (dialogFlowDate.contains(dailyForecast.date)){

                WeatherInfo weatherInfo = new WeatherInfo();

                weatherInfo.setLocation(weather.HeWeather6.get(0).basic.location);
                weatherInfo.setCond_code_d(dailyForecast.cond_code_d);
                weatherInfo.setCond_txt_d(dailyForecast.cond_txt_d);
                weatherInfo.setDate(dailyForecast.date);
                weatherInfo.setTmp_max(dailyForecast.tmp_max);
                weatherInfo.setTmp_min(dailyForecast.tmp_min);
                weatherInfo.setVis(dailyForecast.vis);
                weatherInfo.setWind_spd(dailyForecast.wind_spd);
                weatherInfo.setWind_dir(dailyForecast.wind_dir);

                if (mStt.isSpeaking()){
                    mStt.stopSpeaking();
                }

                Message message = new Message("ROBOT","",Message.Type.ROBOT_WEATHER);
                message.setWeatherInfo(weatherInfo);

                messageList.add(message);
                adapter.notifyDataSetChanged();
                chatRecycler.smoothScrollToPosition(adapter.getItemCount()-1);

                String month = weatherInfo.getDate().split("-")[1];
                String day = weatherInfo.getDate().split("-")[2];
                String desc = "  "+weatherInfo.getLocation()+" "+month+"月"+day+"日 "+weatherInfo.getCond_txt_d()+","+weatherInfo.getTmp_min()+"度到"
                        +weatherInfo.getTmp_max()+"度,"+weatherInfo.getWind_dir()+",风速为"+weatherInfo.getWind_spd()+"公里/小时";

                SpeechUtil.setSynParam(mStt);
                mStt.startSpeaking(desc,SpeechUtil.mSynthesizerListener);

                hasDate = true;
            }
        }

        if (hasDate == false){

            if (mStt.isSpeaking()){
                mStt.stopSpeaking();
            }

            Message message = new Message("ROBOT","您只能查询最近三天的天气哦！",Message.Type.ROBOT_NORMAL);
            messageList.add(message);
            adapter.notifyDataSetChanged();
            chatRecycler.smoothScrollToPosition(adapter.getItemCount()-1);

            SpeechUtil.setSynParam(mStt);
            mStt.startSpeaking(message.getMsg(),SpeechUtil.mSynthesizerListener);
        }
    }

    //请求运行时权限
    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS},0x0010);
                }

                if(permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textTospeech:
                textTospeech.setVisibility(View.GONE);
                speechTotext.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);
                btn_press.setVisibility(View.VISIBLE);
                btn_send.setClickable(false);
                break;
            case R.id.speechTotext:
                textTospeech.setVisibility(View.VISIBLE);
                speechTotext.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                btn_press.setVisibility(View.GONE);
                btn_send.setClickable(true);
                break;
            case R.id.btn_press:
                initSpeech(MainActivity.this);
                break;
            case R.id.btn_send:
                String inputStr = editText.getText().toString();
                if(!TextUtils.isEmpty(inputStr)){
                    Message message = new Message("USER",inputStr,Message.Type.USER);
                    messageList.add(message);
                    adapter.notifyDataSetChanged();
                    chatRecycler.smoothScrollToPosition(adapter.getItemCount()-1);

                    getResponseText(inputStr);
                }
                editText.setText("");
                break;
                default:
                    break;
        }
    }

}


