package com.stardust.autojs.runtime.api;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.stardust.app.GlobalAppContext;
import com.stardust.autojs.annotation.ScriptInterface;
import com.stardust.autojs.core.image.ImageWrapper;
import com.stardust.autojs.ocr.OcrResult;
import com.stardust.autojs.util.OcrHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by linke on 2021/12/08
 */
public class Ocr {

    private static final String TAG = "OcrTAG";

    //初始化超时时间5s
    private static final long INIT_TIMEOUT = 5000;

    @ScriptInterface
    public boolean init() {
        Ref<Boolean> isSuccess = new Ref<>(false);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        OcrHelper.getInstance().initIfNeeded(() -> {
            countDownLatch.countDown();
            isSuccess.value = true;
        });
        try {
            countDownLatch.await(INIT_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // ignore
        }
        return isSuccess.value;
    }

    @ScriptInterface
    public OcrResult ocrImage(ImageWrapper image) {
        if (image == null) {
            return OcrResult.buildFailResult();
        }
        Bitmap bitmap = image.getBitmap();
        if (bitmap == null || bitmap.isRecycled()) {
            return OcrResult.buildFailResult();
        }
        return OcrHelper.getInstance().getOcrInstance().recognize(bitmap);
    }

    @ScriptInterface
    public String ocrImage2Native(Object runtime, Object global, ImageWrapper image) {
        OcrResult result = ocrImage(image);
        if (!result.success || result.words == null || result.words.isEmpty()) {
            return "fail";
        }
        Log.d(TAG, "runtime:" + runtime + ", global: " + global + ", result.text:" + result.text);
        Context context = GlobalAppContext.get();
        Intent intent;
        try {
            intent = new Intent(context, Class.forName("com.shesw.hamibot.sg_ocr.CaptureAndOcrManagerService"));

            JSONArray jsonArray = new JSONArray();
            for (OcrResult.OCrWord oCrWord : result.words) {
                JSONObject word = new JSONObject();
                word.put("text", oCrWord.text);
                word.put("bounds", oCrWord.bounds);
                word.put("confidences", oCrWord.confidences);
                jsonArray.put(word);
            }

            intent.putExtra("data", jsonArray.toString());
            context.startService(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return result.text;
    }

    @ScriptInterface
    public boolean end() {
        OcrHelper.getInstance().end();
        return true;
    }

    static class Ref<T> {
        public T value;

        public Ref(T value) {
            this.value = value;
        }
    }
}

