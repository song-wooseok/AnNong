package ksnu.sw.uilab.annong.activity;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import ksnu.sw.uilab.annong.R;
import ksnu.sw.uilab.annong.domain.CropMeta;
import ksnu.sw.uilab.annong.domain.CropRowMeta;
import ksnu.sw.uilab.annong.utils.JsonUtils;
import ksnu.sw.uilab.annong.utils.enums.Extras;

public class InputDataActivityTest extends AppCompatActivity {

    TextView time_progress;
    ProgressBar progressBar;
    TableLayout tableLayout;

    CropMeta cropMeta;
    List<CropRowMeta> cropRowMetas;

    TextToSpeech tts;
    SpeechRecognizer mRecognizer;
    Intent sttIntent;

    private final Activity activity = this;
    Bundle params = new Bundle();

    long baseTime, pauseTime, setTime;

    @RequiresApi(api = VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        time_progress = (TextView) findViewById(R.id.time_progress);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        cropMeta = JsonUtils.getInstanceFromJson(this, getIntent().getExtras().getString(Extras.CROP_NAME_KEY.getKey()), CropMeta.class);
        cropRowMetas = cropMeta.getRows();

        if ( Build.VERSION.SDK_INT >= 23 ){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, 1);
        }
        //currentColumn = cropRowMetas.get(0).getColumnName();
        initTextToSpeech();
        initSpeechToText();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, null);
        /* ???????????? -> ????????? */
        time_progress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Button btn = findViewById(R.id.btn_go);

       // btn.setOnClickListener(view -> {
         //   startGetData();
       // });


        /* ????????? ?????? ??? add_table_data_row.xml ?????? */
//        tableLayout = (TableLayout) findViewById(R.id.table_layout);
//        @SuppressLint("ResourceType") TableRow tableRow = (TableRow) findViewById(R.layout.add_table_data_row);
//        tableLayout.addView(tableRow);
    }

    @RequiresApi(api = VERSION_CODES.N)
    private void startGetData(){
//        if(!currentColumn.equals("???")){
//            activity.runOnUiThread(()->{
//                try{
//                    Toast.makeText(activity, "?????? ???", Toast.LENGTH_SHORT).show();
//                    Thread.sleep(10000);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            });
//        }
       // voiceOut(currentColumn+"??? ??????????????????");
    }

    private void setCurrentRowMeta(CropRowMeta currentRowMeta){
        //currentColumn = currentRowMeta.getColumnName();
        //currentDataType = currentRowMeta.getDataType();
        //currentIsRequired = currentRowMeta.isRequired();
    }

    private void initSpeechToText(){
        TextToSpeech.OnInitListener getCropDataTTSListener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int state) {
                if (state == TextToSpeech.SUCCESS) {
                    Log.e("TTs", "tts success");
                    tts.setLanguage(Locale.KOREAN);
                    tts.setPitch(1);
                    tts.setOnUtteranceProgressListener(ttsUtteranceListener);
                    return;
                }
                Log.e("TTS", "fail to init TTS");
            }
        };

        TextToSpeech.OnInitListener checkCropData = new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int state) {
                if (state == TextToSpeech.SUCCESS) {
                    Log.e("TTs", "tts success");
                    tts.setLanguage(Locale.KOREAN);
                    tts.setPitch(1);
                    tts.setOnUtteranceProgressListener(ttsUtteranceListener);
                    return;
                }
                Log.e("TTS", "fail to init TTS");
            }
        };

        //this.tts = new TextToSpeech(this, getCropDataSTTListener);
        //this.tts = new TextToSpeech(this, getCropDataSTTListener);
    }

    private void initTextToSpeech(){
        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

    }

    private void speechStart(){
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(CropDataRecognitionListener);
        mRecognizer.startListening(sttIntent);
    }

    private void voiceOut(String msg){
        tts.speak(msg, TextToSpeech.QUEUE_ADD, params, msg);
    }

    private UtteranceProgressListener ttsUtteranceListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String s) {

        }

        @Override
        public void onDone(String s) {
            activity.runOnUiThread(() -> speechStart());
        }

        @Override
        public void onError(String s) {

        }
    };

    private RecognitionListener CropDataRecognitionListener = new RecognitionListener(){
        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "??????????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "???????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "????????? ????????????";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "?????? ??? ??????";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER ??? ??????";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "????????? ?????????";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "????????? ????????????";
                    break;
                default:
                    message = "??? ??? ?????? ?????????";
                    break;
            }
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }

        @RequiresApi(api = VERSION_CODES.N)
        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            //voiceOut(matches.get(0));
            tts.speak(matches.get(0), TextToSpeech.QUEUE_FLUSH, null);
//            if(currentColumn.equals("?????????")){
//                return;
//            }
//            currentColumn = "?????????";
            startGetData();
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

    private RecognitionListener DataCheckRecognitionListener = new RecognitionListener(){

        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {

        }

        @Override
        public void onResults(Bundle bundle) {

        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };
}