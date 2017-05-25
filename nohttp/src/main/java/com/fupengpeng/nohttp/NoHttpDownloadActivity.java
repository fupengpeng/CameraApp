package com.fupengpeng.nohttp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadQueue;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.error.ArgumentError;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.ServerError;
import com.yolanda.nohttp.error.StorageReadWriteError;
import com.yolanda.nohttp.error.StorageSpaceNotEnoughError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;

import java.util.Locale;

public class NoHttpDownloadActivity extends AppCompatActivity {

    //下载队列
    private DownloadQueue downloadQueue;
    //下载请求
    private DownloadRequest downloadRequest;

    //用来标志请求的what
    private static final int NOHTTP_WHAT = 0x001;

    //TextView：下载状态
    private TextView tvResult;
    //ProgressBar：进度条
    private ProgressBar pbProgress;
    private Button btnDownloadSingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_http_download);

        //创建请求队列
        downloadQueue = NoHttp.newDownloadQueue();

        //初始化控件
        initViews();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        tvResult = (TextView) findViewById(R.id.tv_activity_no_http_shangchuanzhuangtai);
        pbProgress = (ProgressBar) findViewById(R.id.prb_activity_no_http_shangchuanjindu);
        btnDownloadSingle = (Button) findViewById(R.id.btn_activity_no_http_shangchuan);
    }

    /**
     * 下载
     *
     * @param view
     */
    public void doClick(View view) {
        // 开始下载了，但是任务没有完成，代表正在下载，那么暂停下载。
        if (downloadRequest != null && downloadRequest.isStarted() && !downloadRequest.isFinished()) {
            // 暂停下载。
            downloadRequest.cancel();
        } else if (downloadRequest == null || downloadRequest.isFinished()) {// 没有开始或者下载完成了，就重新下载。

            /**
             * 这里不传文件名称、不断点续传，则会从响应头中读取文件名自动命名，如果响应头中没有则会从url中截取。
             */
            // url 下载地址。
            // fileFolder 文件保存的文件夹。
            // isDeleteOld 发现文件已经存在是否要删除重新下载。
            //String url = "http://gdown.baidu.com/data/wisegame/2eeee3831c9dbc42/QQ_374.apk";
            //String dirPath = Environment.getExternalStorageDirectory()+"/NoHttpSample";
            //mDownloadRequest = NoHttp.createDownloadRequest(url, dirPath, true);

            /**
             * 如果使用断点续传的话，一定要指定文件名。
             */
            // url 下载地址。
            // fileFolder 保存的文件夹。
            // fileName 文件名。
            // isRange 是否断点续传下载。
            // isDeleteOld 如果发现存在同名文件，是否删除后重新下载，如果不删除，则直接下载成功。
            String url = "http://gdown.baidu.com/data/wisegame/2eeee3831c9dbc42/QQ_374.apk";
            String dirPath = Environment.getExternalStorageDirectory()+"/NoHttpSample";
            downloadRequest = NoHttp.createDownloadRequest(url, dirPath, "QQ_374.apk", true, true);

            // what 区分下载。
            // downloadRequest 下载请求对象。
            // downloadListener 下载监听。
            downloadQueue.add(NOHTTP_WHAT, downloadRequest, downloadListener);

            // 添加到队列，在没响应的时候让按钮不可用。
            btnDownloadSingle.setEnabled(false);
        }
    }

    /**
     * 下载监听
     */
    private DownloadListener downloadListener = new DownloadListener() {

        @Override
        public void onStart(int what, boolean isResume, long beforeLength, Headers headers, long allCount) {
            int progress = 0;
            //计算进度
            if (allCount != 0) {
                progress = (int) (beforeLength * 100 / allCount);
                pbProgress.setProgress(progress);
            }

            //更新进度
            updateProgress(progress);

            //下载按钮设为可点击，文字改为“暂停”
            btnDownloadSingle.setText("暂停");
            btnDownloadSingle.setEnabled(true);
        }

        @Override
        public void onDownloadError(int what, Exception exception) {
            Logger.e(exception);

            //下载按钮设为可点击，文字改为“重新下载”
            btnDownloadSingle.setText("重新下载");
            btnDownloadSingle.setEnabled(true);

            String message = "下载出错了：%1$s";
            String messageContent;
            if (exception instanceof ServerError) {
                messageContent = "服务器数据错误！";
            } else if (exception instanceof NetworkError) {
                messageContent = "网络不可用，请检查网络！";
            } else if (exception instanceof StorageReadWriteError) {
                messageContent = "存储卡错误，请检查存储卡！";
            } else if (exception instanceof StorageSpaceNotEnoughError) {
                messageContent = "存储卡空间不足！";
            } else if (exception instanceof TimeoutError) {
                messageContent = "下载超时！";
            } else if (exception instanceof UnKnownHostError) {
                messageContent = "找不到服务器。";
            } else if (exception instanceof URLError) {
                messageContent = "URL地址错误。";
            } else if (exception instanceof ArgumentError) {
                messageContent = "下载参数错误。";
            } else {
                messageContent = "未知错误。";
            }

            message = String.format(Locale.getDefault(), message, messageContent);
            tvResult.setText(message);
        }

        @Override
        public void onProgress(int what, int progress, long fileCount) {
            updateProgress(progress);
            pbProgress.setProgress(progress);
        }

        @Override
        public void onFinish(int what, String filePath) {
            Logger.d("Download finish, file path: " + filePath);

            tvResult.setText("下载完成");

            btnDownloadSingle.setText("重新下载");
            btnDownloadSingle.setEnabled(true);
        }

        @Override
        public void onCancel(int what) {
            tvResult.setText("暂停");
            btnDownloadSingle.setText("继续下载");
            btnDownloadSingle.setEnabled(true);
        }

        //更新进度
        private void updateProgress(int progress) {
            String sProgress = "已下载：%1$d";
            sProgress = String.format(Locale.getDefault(), sProgress, progress);
            tvResult.setText(sProgress);
        }
    };

    @Override
    protected void onDestroy() {
        // 暂停下载
        if (downloadRequest != null) {
            downloadRequest.cancel();
        }
        super.onDestroy();
    }

}
