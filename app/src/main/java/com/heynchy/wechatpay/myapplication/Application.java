package com.heynchy.wechatpay.myapplication;


import com.heynchy.wechatpay.paylibs.Utils.WechatPayUtil;

public class Application extends android.app.Application {
    private static volatile Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        WechatPayUtil.initConfig(this,
                "微信注册的APP_ID",
                "商户号",
                "商户号对应的支付秘钥（32位）");
    }

    /**
     * 创建App的单例对象
     */
    public synchronized static Application getInstance() {
        if (mInstance == null) {
            synchronized (Application.class) {
                if (null == mInstance) {
                    mInstance = new Application();
                }
            }
        }
        return mInstance;
    }

}
