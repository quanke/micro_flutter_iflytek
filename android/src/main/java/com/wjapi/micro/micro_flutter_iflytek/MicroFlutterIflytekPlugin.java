package com.wjapi.micro.micro_flutter_iflytek;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * MicroFlutterIflytekPlugin
 */
public class MicroFlutterIflytekPlugin implements MethodCallHandler {

    private static String TAG = MicroFlutterIflytekPlugin.class.getSimpleName();
    // 语音听写对象
    private SpeechRecognizer mIat;

    // 语音听写UI
    private RecognizerDialog mIatDialog;

    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private String resultType = "json";

    private boolean cyclic = false;//音频流识别是否循环调用

    private boolean activeEnd = false;

    private int cyclicDelayMillis;

    private StringBuffer buffer = new StringBuffer();

    private static final String PREFER_NAME = "com.iflytek.setting";

    private Handler han = new MyHandler(this);


    /**
     * 跳转到科大讯飞语音识别页面
     */
    private static final String METHOD_RECOGNIZER = "recognizer";
    private static final String METHOD_STOP_RECOGNIZER = "stopRecognizer";

    private static final String METHOD_INIT = "init";
    private static final String METHOD_ON_INIT = "onInit";

    private static final String METHOD_ON_BEGIN_OF_SPEECH = "onBeginOfSpeech";
    private static final String METHOD_ON_ERROR = "onError";

    private static final String METHOD_ON_END_OF_SPEECH = "onEndOfSpeech";
    private static final String METHOD_ON_RESULT = "onResult";
    private static final String METHOD_ON_VOLUME_CHANGED = "onVolumeChanged";

    private static final String METHOD_ON_ERROR_DIALOG = "onErrorDialog";
    private static final String METHOD_ON_RESULT_DIALOG = "onResultDialog";

    private static final String ARGUMENT_KEY_APP_ID = "appId";
    private static final String ARGUMENT_KEY_SHOW_DIALOG = "showDialog";
    private static final String ARGUMENT_KEY_CYCLIC = "cyclic";
    private static final String ARGUMENT_KEY_CYCLIC_DELAY_MILLIS = "cyclicDelayMillis";


    private final Registrar registrar;
    private final MethodChannel channel;

    private Boolean showDialog = Boolean.TRUE;

    public MicroFlutterIflytekPlugin(Registrar registrar, MethodChannel channel) {
        this.registrar = registrar;
        this.channel = channel;
    }

    //支持功能
//    所有科大讯飞的配置
//    初始化
//    带UI的语音识别
//    不带UI语音识别 支持循环识别
//    返回结果
//    结束识别

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        Log.e(TAG, "registerWith");

        final MethodChannel channel = new MethodChannel(registrar.messenger(), "micro_flutter_iflytek");
        channel.setMethodCallHandler(new MicroFlutterIflytekPlugin(registrar, channel));
    }

    private void iatStartListening() {
        mIat.startListening(mRecognizerListener);

    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        //跳转到科大讯飞语音识别页面
        if (METHOD_RECOGNIZER.equals(call.method)) {

            boolean cyclic = call.argument(ARGUMENT_KEY_CYCLIC);
            int cyclicDelayMillis = call.argument(ARGUMENT_KEY_CYCLIC_DELAY_MILLIS);

            beginRecognize(cyclic, cyclicDelayMillis, null);
            result.success(null);
        } else if (METHOD_INIT.equals(call.method)) {
            String appId = call.argument(ARGUMENT_KEY_APP_ID);
            Boolean showDialog = call.argument(ARGUMENT_KEY_SHOW_DIALOG);
            init(appId, showDialog);
            result.success(null);
        } else if (METHOD_STOP_RECOGNIZER.equals(call.method)) {
            stopRecognizer();
            result.success(null);
        } else {
            result.notImplemented();
        }

    }

    private void init(String appId, Boolean showDialog) {
        Log.e(TAG, "init----------------");
        this.showDialog = showDialog;
//        5cc03524
        SpeechUtility.createUtility(registrar.activity(), "appid=" + appId);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(registrar.activity(), mInitListener);
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(registrar.activity(), mInitListener);

        mSharedPreferences = registrar.activity().getSharedPreferences(PREFER_NAME,
                Activity.MODE_PRIVATE);
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            channel.invokeMethod(METHOD_ON_INIT, code);
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//            showTip("开始说话");
            channel.invokeMethod(METHOD_ON_BEGIN_OF_SPEECH, null);
        }

        @Override
        public void onError(SpeechError error) {

            Map<String, Object> result = new HashMap<>();
            result.put("errorDescription", error.getErrorDescription());
            result.put("errorCode", error.getErrorCode());
            channel.invokeMethod(METHOD_ON_ERROR, result);

            if (!activeEnd) {
                if (cyclic && error.getErrorCode() == 10118) {
                    Message message = Message.obtain();
                    message.what = 0x001;
                    han.sendMessageDelayed(message, cyclicDelayMillis);
                }
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            showTip("结束说话");
            channel.invokeMethod(METHOD_ON_END_OF_SPEECH, null);
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = JsonParser.parseIatResult(results.getResultString());

            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (sn == null) {
                return;
            }
            Map<String, Object> result = new HashMap<>();
//            result.put("results", results.getResultString());
            Map<String, Object> snMap = new HashMap<>();
            result.put("isLast", isLast);
            snMap.put(sn, text);
            result.put("ws", snMap);

            channel.invokeMethod(METHOD_ON_RESULT, result);

            if (!activeEnd) {
                if (isLast && cyclic) {
                    Message message = Message.obtain();
                    message.what = 0x001;
                    han.sendMessageDelayed(message, cyclicDelayMillis);
                }
            }

        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {

            Map<String, Object> result = new HashMap<>();
            result.put("volume", String.valueOf(volume));
            result.put("data", data);

            channel.invokeMethod(METHOD_ON_VOLUME_CHANGED, result);

//            showTip("当前正在说话，音量大小：" + volume);
//            Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {


            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    /**
     * 开始识别
     */
    private void beginRecognize(boolean cyclic, int cyclicDelayMillis, Map<String, String> param) {
        this.cyclic = cyclic;
        this.cyclicDelayMillis = cyclicDelayMillis;
        this.activeEnd = false;
        Log.e(TAG, "beginIatRecognize");

        buffer.setLength(0);
        // 设置参数
        setParam(param);

        if (showDialog) {
            // 显示听写对话框
            mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();
        } else {
            mIat.startListening(mRecognizerListener);
        }

    }

    /**
     * 停止识别
     */
    private void stopRecognizer() {
        this.activeEnd = true;
        if (mIat != null) {
            mIat.stopListening();
        }
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {

            Map<String, Object> result = new HashMap<>();
            result.put("results", results.getResultString());
            result.put("isLast", isLast);

            channel.invokeMethod(METHOD_ON_RESULT_DIALOG, result);

        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Map<String, Object> result = new HashMap<>();
            result.put("error", error);
            channel.invokeMethod(METHOD_ON_ERROR_DIALOG, result);
        }
    };

    /**
     * 参数设置
     */
    private void setParam(Map<String, String> param) {

        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);

        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "10000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "10000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    static class MyHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<MicroFlutterIflytekPlugin> plugin;

        MyHandler(MicroFlutterIflytekPlugin plugin) {
            this.plugin = new WeakReference<>(plugin);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MicroFlutterIflytekPlugin outer = plugin.get();
            if (plugin != null) {
                // Do something with outer as your wish.
                if (msg.what == 0x001) {
                    outer.iatStartListening();
                }
            }

        }
    }
}
