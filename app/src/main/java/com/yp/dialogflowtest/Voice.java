package com.yp.dialogflowtest;

import java.util.ArrayList;

//Json对应的类
public class Voice {
    public ArrayList<WSBean> ws;
    public String sn;

    public class WSBean {
        public ArrayList<CWBean> cw;
    }

    public class CWBean {
        public String w;
    }
}

