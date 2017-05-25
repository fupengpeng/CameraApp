package com.fupengpeng.nohttp.base;

import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

/**
 * Created by fp on 2017/5/25.
 */

public class Application extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化NoHttp
        NoHttp.initialize(this);

        // 开始NoHttp的调试模式, 这样就能看到请求过程和日志（可选）
        Logger.setTag("NoHttpSample");
        Logger.setDebug(true);
    }

}
