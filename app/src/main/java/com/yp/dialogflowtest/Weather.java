package com.yp.dialogflowtest;

import java.util.ArrayList;

public class Weather {

    ArrayList<HeWeather> HeWeather6;

    public class HeWeather {

        Basic basic;
        ArrayList<DailyForecast> daily_forecast;

        public class Basic {

            String location;
        }

        public class DailyForecast{

            int cond_code_d;
            String cond_txt_d;
            String date;
            String wind_dir;
            String wind_spd;
            String vis;
            String tmp_max;
            String tmp_min;
        }
    }
}
