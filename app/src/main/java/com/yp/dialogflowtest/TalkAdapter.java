package com.yp.dialogflowtest;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TalkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int USER_MSG = 1;
    private final int ROBOT_NORMAL_MSG = 2;
    private final int ROBOT_WEATHER_MSG = 3;

    List<Message> datas;

    public TalkAdapter(List<Message> datas){
        this.datas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if(i == USER_MSG){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_message_item, viewGroup, false);
            return new UserMsg(view);
        }else if(i == ROBOT_NORMAL_MSG){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.normal_robot_message_item, viewGroup, false);
            return new RobotNormalMsg(view);
        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_robot_message_item, viewGroup, false);
            return new RobotWeatherMsg(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if(getItemViewType(i) == USER_MSG){
            ((UserMsg) viewHolder).user.setText(datas.get(i).getName());
            ((UserMsg) viewHolder).userMsg.setText(datas.get(i).getMsg());
        }else if(getItemViewType(i) == ROBOT_NORMAL_MSG) {
            ((RobotNormalMsg) viewHolder).robot.setText(datas.get(i).getName());
            ((RobotNormalMsg) viewHolder).robotMsg.setText(datas.get(i).getMsg());
        }else if(viewHolder instanceof RobotWeatherMsg) {
            Message msg = datas.get(i);

            WeatherInfo weatherInfo = msg.getWeatherInfo();

            String name = msg.getName();

            int imageIconId = getWeatherIcon(weatherInfo);
            ((RobotWeatherMsg) viewHolder).weatherIcon.setImageResource(imageIconId);

            String temp = weatherInfo.getTmp_min()+"°C/"+weatherInfo.getTmp_max()+"°C";
            ((RobotWeatherMsg) viewHolder).weatherTemp.setText(temp);

            String month = weatherInfo.getDate().split("-")[1];
            String day = weatherInfo.getDate().split("-")[2];

            String smallDesc = month+"月"+day+"日 | "+weatherInfo.getLocation()+" | 能见度"+weatherInfo.getVis()+"公里";
            ((RobotWeatherMsg) viewHolder).weatherSmallDesc.setText(smallDesc);

            String desc = "  "+weatherInfo.getLocation()+" "+month+"月"+day+"日 "+weatherInfo.getCond_txt_d()+","+weatherInfo.getTmp_min()+"度到"
                    +weatherInfo.getTmp_max()+"度,"+weatherInfo.getWind_dir()+",风速为"+weatherInfo.getWind_spd()+"公里/小时";
            ((RobotWeatherMsg) viewHolder).weatherDesc.setText(desc);
        }
    }

    @Override
    public int getItemCount() {
        if(datas.size() == 0){
            return 0;
        }else {
            return datas.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
            if(datas.get(position).getType() == Message.Type.USER){
                return USER_MSG;
            }else if(datas.get(position).getType() == Message.Type.ROBOT_NORMAL){
                return ROBOT_NORMAL_MSG;
            }else {
                return ROBOT_WEATHER_MSG;
            }
    }

    private int getWeatherIcon(WeatherInfo weatherInfo){

        int imageIconId = 100;
        switch (weatherInfo.getCond_code_d()){
            case 100:
                imageIconId = R.drawable.cond_icon_100;
                break;
            case 101:
                imageIconId = R.drawable.cond_icon_101;
                break;
            case 102:
                imageIconId = R.drawable.cond_icon_102;
                break;
            case 103:
                imageIconId = R.drawable.cond_icon_103;
                break;
            case 104:
                imageIconId = R.drawable.cond_icon_104;
                break;
            case 200:
                imageIconId = R.drawable.cond_icon_200;
                break;
            case 201:
                imageIconId = R.drawable.cond_icon_201;
                break;
            case 202:
                imageIconId = R.drawable.cond_icon_202;
                break;
            case 203:
                imageIconId = R.drawable.cond_icon_203;
                break;
            case 204:
                imageIconId = R.drawable.cond_icon_204;
                break;
            case 205:
                imageIconId = R.drawable.cond_icon_205;
                break;
            case 206:
                imageIconId = R.drawable.cond_icon_206;
                break;
            case 207:
                imageIconId = R.drawable.cond_icon_207;
                break;
            case 208:
                imageIconId = R.drawable.cond_icon_208;
                break;
            case 209:
                imageIconId = R.drawable.cond_icon_209;
                break;
            case 210:
                imageIconId = R.drawable.cond_icon_210;
                break;
            case 211:
                imageIconId = R.drawable.cond_icon_210;
                break;
            case 213:
                imageIconId = R.drawable.cond_icon_210;
                break;
            case 300:
                imageIconId = R.drawable.cond_icon_300;
                break;
            case 301:
                imageIconId = R.drawable.cond_icon_301;
                break;
            case 302:
                imageIconId = R.drawable.cond_icon_302;
                break;
            case 303:
                imageIconId = R.drawable.cond_icon_303;
                break;
            case 304:
                imageIconId = R.drawable.cond_icon_304;
                break;
            case 305:
                imageIconId = R.drawable.cond_icon_305;
                break;
            case 306:
                imageIconId = R.drawable.cond_icon_306;
                break;
            case 307:
                imageIconId = R.drawable.cond_icon_307;
                break;
            case 308:
                imageIconId = R.drawable.cond_icon_308;
                break;
            case 309:
                imageIconId = R.drawable.cond_icon_309;
                break;
            case 310:
                imageIconId = R.drawable.cond_icon_310;
                break;
            case 311:
                imageIconId = R.drawable.cond_icon_311;
                break;
            case 312:
                imageIconId = R.drawable.cond_icon_312;
                break;
            case 313:
                imageIconId = R.drawable.cond_icon_313;
                break;
            case 314:
                imageIconId = R.drawable.cond_icon_314;
                break;
            case 315:
                imageIconId = R.drawable.cond_icon_315;
                break;
            case 316:
                imageIconId = R.drawable.cond_icon_316;
                break;
            case 317:
                imageIconId = R.drawable.cond_icon_316;
                break;
            case 318:
                imageIconId = R.drawable.cond_icon_316;
                break;
            case 399:
                imageIconId = R.drawable.cond_icon_399;
                break;
            case 400:
                imageIconId = R.drawable.cond_icon_400;
                break;
            case 401:
                imageIconId = R.drawable.cond_icon_401;
                break;
            case 402:
                imageIconId = R.drawable.cond_icon_402;
                break;
            case 403:
                imageIconId = R.drawable.cond_icon_403;
                break;
            case 404:
                imageIconId = R.drawable.cond_icon_404;
                break;
            case 405:
                imageIconId = R.drawable.cond_icon_405;
                break;
            case 406:
                imageIconId = R.drawable.cond_icon_406;
                break;
            case 407:
                imageIconId = R.drawable.cond_icon_407;
                break;
            case 408:
                imageIconId = R.drawable.cond_icon_408;
                break;
            case 409:
                imageIconId = R.drawable.cond_icon_409;
                break;
            case 410:
                imageIconId = R.drawable.cond_icon_410;
                break;
            case 499:
                imageIconId = R.drawable.cond_icon_499;
                break;
            case 500:
                imageIconId = R.drawable.cond_icon_500;
                break;
            case 501:
                imageIconId = R.drawable.cond_icon_501;
                break;
            case 502:
                imageIconId = R.drawable.cond_icon_502;
                break;
            case 503:
                imageIconId = R.drawable.cond_icon_503;
                break;
            case 504:
                imageIconId = R.drawable.cond_icon_504;
                break;
            case 505:
                imageIconId = R.drawable.cond_icon_504;
                break;
            case 506:
                imageIconId = R.drawable.cond_icon_504;
                break;
            case 507:
                imageIconId = R.drawable.cond_icon_507;
                break;
            case 508:
                imageIconId = R.drawable.cond_icon_508;
                break;
            case 509:
                imageIconId = R.drawable.cond_icon_509;
                break;
            case 510:
                imageIconId = R.drawable.cond_icon_510;
                break;
            case 511:
                imageIconId = R.drawable.cond_icon_511;
                break;
            case 512:
                imageIconId = R.drawable.cond_icon_512;
                break;
            case 513:
                imageIconId = R.drawable.cond_icon_513;
                break;
            case 514:
                imageIconId = R.drawable.cond_icon_514;
                break;
            case 515:
                imageIconId = R.drawable.cond_icon_514;
                break;
            case 900:
                imageIconId = R.drawable.cond_icon_900;
                break;
            case 901:
                imageIconId = R.drawable.cond_icon_901;
                break;
            case 999:
                imageIconId = R.drawable.cond_icon_999;
                break;
            default:
                break;
        }
        return imageIconId;
    }

    class RobotNormalMsg extends RecyclerView.ViewHolder{

        TextView robot;
        TextView robotMsg;

        public RobotNormalMsg(@NonNull View itemView) {
            super(itemView);

            robot = (TextView) itemView.findViewById(R.id.robot);
            robotMsg = (TextView) itemView.findViewById(R.id.robot_msg);
        }
    }

    class UserMsg extends RecyclerView.ViewHolder{

        TextView user;
        TextView userMsg;

        public UserMsg(@NonNull View itemView) {
            super(itemView);

            user = (TextView) itemView.findViewById(R.id.user);
            userMsg = (TextView) itemView.findViewById(R.id.user_msg);
        }
    }

    class RobotWeatherMsg extends RecyclerView.ViewHolder{

        TextView robotWeather;
        ImageView weatherIcon;
        TextView weatherTemp;
        TextView weatherSmallDesc;
        TextView weatherDesc;

        public RobotWeatherMsg(@NonNull View itemView) {
            super(itemView);

            robotWeather = (TextView) itemView.findViewById(R.id.robot_weather);
            weatherIcon = (ImageView) itemView.findViewById(R.id.weather_icon);
            weatherTemp = (TextView) itemView.findViewById(R.id.weather_temp);
            weatherSmallDesc = (TextView) itemView.findViewById(R.id.weather_small_desc);
            weatherDesc = (TextView) itemView.findViewById(R.id.weather_desc);
        }
    }
}
