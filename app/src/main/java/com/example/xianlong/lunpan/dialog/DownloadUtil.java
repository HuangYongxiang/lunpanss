package com.example.xianlong.lunpan.dialog;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import com.example.xianlong.lunpan.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017-09-10.
 */

public class DownloadUtil {

    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param url      下载连接
     * @param saveDir  储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String apk, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();

            InputStream is = null;
            byte[] buf = new byte[2048];
            int len = 0;
            FileOutputStream fos = null;
            // 储存下载文件的目录
//            String savePath = isExistDir(saveDir);
            try {
                is = response.body().byteStream();
                long total = response.body().contentLength();
//                File file = new File(savePath, getNameFromUrl(url));
//                File file = new File(Environment.getExternalStorageDirectory()+"/test.apk");
//                if(!file.exists()){
//                    file.mkdir();
//                }

                File extDir = Environment.getExternalStorageDirectory();
                String filename = apk;
                File fullFilename = new File(extDir, filename);

                fullFilename.createNewFile();
                fullFilename.setWritable(Boolean.TRUE);


                fos = new FileOutputStream(fullFilename);
                long sum = 0;
                int lastFlag = 0;
                int progress = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    progress = (int) (sum * 1.0f / total * 100);
                    // 下载中
                    Log.i("SHIYANG", progress + "");

                    if (progress - lastFlag > 4) {
                        lastFlag = progress;
                        listener.onDownloading(progress);
                    }
                }
                fos.flush();
                // 下载完成
                listener.onDownloadSuccess();
            } catch (Exception e) {
                listener.onDownloadFailed();
            } finally {
                try {
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                }
                try {
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                }
            }

        } catch (IOException e) {
            // 下载失败
            listener.onDownloadFailed();
            e.printStackTrace();
        }


    }


    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }


}
