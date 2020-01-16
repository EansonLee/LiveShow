package com.maywide.liveshow.utils;

import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;

import java.text.NumberFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by liyizhen on 2020/1/15.
 */

public class UpLoadUtils {

    private static final String TAG = UpLoadUtils.class.getSimpleName();

    //七牛后台的key
    private static String AccessKey = "KAzcjabVsHI6yeVYOK_qnh6stWtiSiD6DNLtooVC";
    //七牛后台的secret
    private static String SecretKey = "BshL7694EF-utvheJf39MO6NVkFmpdlImomG6G9n";

    private static String BucketName = "images";

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    private static Configuration configuration;

    private static long delayTimes = 3029414400l; //有效时间

    /**
     * 上传
     * <p>
     * //* @param keys 上传到空间后的文件名的名字
     *
     * @param path     上传文件的路径地址
     */
    public static void uploadPic(final String path, final String key) {
        //定义数据上传结束后的处理动作
        final UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()){
                    Log.e("---",info.toString());
                }else {
                    Log.e("---",info.toString());
                }

            }
        };
        final UploadOptions uploadOptions = new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, final double percent) {
                //百分数格式化
                NumberFormat fmt = NumberFormat.getPercentInstance();
                fmt.setMaximumFractionDigits(2);//最多两位百分小数，如25.23%
//                tv.setText("图片已经上传:" + fmt.format(percent));
            }
        }, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return false;
            }
        });
        try {
            String token = Auth.create(AccessKey, SecretKey).uploadToken(BucketName);
            QiNiuInitialize.getSingleton().put(path, key, token, upCompletionHandler,uploadOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
