package com.yp.dialogflowtest.utils;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class SpeechUtil {


    public static Toast mToast;

    //初始化合成语音参数
    public static void setSynParam(SpeechSynthesizer mStt){
        mStt.setParameter(SpeechConstant.PARAMS,null);

        mStt.setParameter(SpeechConstant.SPEED,"60");
        mStt.setParameter(SpeechConstant.VOICE_NAME,"xiaolin");
        mStt.setParameter(SpeechConstant.PITCH,"50");
        mStt.setParameter(SpeechConstant.VOLUME,"50");

        // 设置播放合成音频打断音乐播放，默认为true
        mStt.setParameter(SpeechConstant.KEY_REQUEST_FOCUS,"true");
        //设置播放器音频流类型
        mStt.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mStt.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mStt.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/dialogflowtest/tts.wav");
    }

    public static SynthesizerListener mSynthesizerListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            showTip("开始合成");
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
//            bufferProgress = i;
//            showTip(String.format("合成进度%d%%,播放进度为%d%%",bufferProgress,playProgress));
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onSpeakProgress(int i, int beginPos, int endPos) {
//            playProgress = i;
//            showTip(String.format("合成进度%d%%,播放进度为%d%%",bufferProgress,playProgress));
//
//            SpannableStringBuilder style = new SpannableStringBuilder(voiceText.getText().toString());
//            style.setSpan(new ForegroundColorSpan(Color.CYAN),beginPos,(endPos+1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((EditText) findViewById(R.id.voice_text)).setText(style);
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            if (speechError == null) {
                showTip("播放完成");
            } else if (speechError != null) {
                showTip(speechError.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    public static void showTip(String message){
        mToast.setText(message);
        mToast.show();
    }
}
