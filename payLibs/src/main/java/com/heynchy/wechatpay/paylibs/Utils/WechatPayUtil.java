package com.heynchy.wechatpay.paylibs.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.heynchy.wechatpay.paylibs.Configure;
import com.heynchy.wechatpay.paylibs.PrepayOrderListener;
import com.heynchy.wechatpay.paylibs.bean.OrderSendInfo;
import com.heynchy.wechatpay.paylibs.bean.PrepayIdInfo;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thoughtworks.xstream.XStream;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * Author: Heynchy
 * Date:   2019/1/11
 * <p>
 * Introduce: 微信支付的相关操作工具类
 */
public class WechatPayUtil {
    private static String appId;
    private static String mchId;
    private static String apiKey;
    private static Context context;
    private static IWXAPI iwxapi;

    private static volatile WechatPayUtil instance;

    private WechatPayUtil() {
    }

    public static WechatPayUtil getInstance() {
        if (instance == null) {
            synchronized (WechatPayUtil.class) {
                if (instance == null) {
                    instance = new WechatPayUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化配置信息
     *
     * @param context
     * @param appId
     * @param mchId
     * @param apiKey
     */
    public static void initConfig(Context context, String appId, String mchId, String apiKey) {
        getInstance().setConfigure(context, appId, mchId, apiKey);
        if (iwxapi == null) {
            //通过WXAPIFactory创建IWAPI实例
            iwxapi = WXAPIFactory.createWXAPI(WechatPayUtil.getInstance().getContext(), null);
            //将应用的appid注册到微信
            iwxapi.registerApp(WechatPayUtil.getInstance().getAppId());
        }
    }

    /**
     * 支付 （调起支付界面）------------支付功能
     *
     * @param prepayid
     */
    public static void wechatPay(String prepayid) {
        if (isInstalled()) {
            genPayReq(prepayid);
            iwxapi.registerApp(appId);
            iwxapi.sendReq(genPayReq(prepayid));
        }
    }

    /**
     * 统一下单 生成微信预支付Id-----建议后台生成
     *
     * @param orderSendInfo
     * @param listerner
     */
    public static void createPrepayOrder(OrderSendInfo orderSendInfo, final PrepayOrderListener listerner) {

        //生成sign签名
        String sign = genSign(orderSendInfo);

        //生成所需参数，为xml格式
        orderSendInfo.setSign(sign.toUpperCase());
        XStream xstream = new XStream();
        xstream.alias("xml", OrderSendInfo.class);
        final String xml = xstream.toXML(orderSendInfo).replaceAll("__", "_");

        //调起接口，获取预支付ID
        OkHttpUtils.ResultCallback<String> resultCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                String data = response;
                data = data.replaceAll("<!\\[CDATA\\[", "").replaceAll("]]>", "");
                XStream stream = new XStream();
                stream.processAnnotations(PrepayIdInfo.class);
                PrepayIdInfo bean = (PrepayIdInfo) stream.fromXML(data);
                listerner.Success(bean);
            }

            @Override
            public void onFailure(Exception e) {
                listerner.Faiulre(e.toString());
            }
        };

        OkHttpUtils.post(Configure.UNIFIED_ORDER, resultCallback, xml);
    }

    /**
     * 获取生成预支付订单的基本信息------建议后台处理
     *
     * @param describle
     * @param money
     */
    public static OrderSendInfo getPreOrderInfo(String describle, String money) {
        //生成预支付Id（建议由后台生成）
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateNowStr = sdf.format(d);
        OrderSendInfo sendInfo = new OrderSendInfo(appId,
                mchId,
                genNonceStr(),
                describle,
                dateNowStr,
                money,
                "192.168.101.49",
                "http://weixin.qq.com",
                "APP");
        return sendInfo;
    }

    //生成支付参数
    private static PayReq genPayReq(String prepayid) {
        PayReq req = new PayReq();
        req.appId = WechatPayUtil.getInstance().getAppId();
        req.partnerId = WechatPayUtil.getInstance().getMchId();
        req.prepayId = prepayid;
        req.packageValue = "Sign=" + prepayid;
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<OkHttpUtils.Param> signParams = new LinkedList<OkHttpUtils.Param>();
        signParams.add(new OkHttpUtils.Param("appid", req.appId));
        signParams.add(new OkHttpUtils.Param("noncestr", req.nonceStr));
        signParams.add(new OkHttpUtils.Param("package", req.packageValue));
        signParams.add(new OkHttpUtils.Param("partnerid", req.partnerId));
        signParams.add(new OkHttpUtils.Param("prepayid", req.prepayId));
        signParams.add(new OkHttpUtils.Param("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);
        return req;
    }

    //获得时间戳
    private static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    //生成随机字符串
    private static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    //生成预支付随机签名
    private static String genSign(OrderSendInfo info) {
        StringBuffer sb = new StringBuffer(info.toString());
        String apiKey = WechatPayUtil.getInstance().getApiKey();
        if (TextUtils.isEmpty(apiKey)) {
            Toast.makeText(WechatPayUtil.getInstance().getContext(), "APP_ID为空", Toast.LENGTH_LONG).show();
        }
        //拼接密钥
        sb.append("key=");
        sb.append(apiKey);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());

        return appSign;
    }

    //生成支付随机签名
    private static String genAppSign(List<OkHttpUtils.Param> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).key);
            sb.append('=');
            sb.append(params.get(i).value);
            sb.append('&');
        }
        //拼接密钥
        sb.append("key=");
        sb.append(WechatPayUtil.getInstance().getApiKey());

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        return appSign.toUpperCase();
    }

    public static boolean isInstalled() {
        if (iwxapi == null) {
            Toast.makeText(WechatPayUtil.getInstance().getContext(), "请初始化相关配置", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(WechatPayUtil.getInstance().getContext(), "请先安装微信应用", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!iwxapi.isWXAppSupportAPI()) {
            Toast.makeText(WechatPayUtil.getInstance().getContext(), "请先更新微信应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 设置配置信息
     *
     * @param context
     * @param appId
     * @param mchId
     * @param apiKey
     */
    private void setConfigure(Context context, String appId, String mchId, String apiKey) {
        this.appId = appId;
        this.mchId = mchId;
        this.apiKey = apiKey;
        this.context = context;
    }

    public String getMchId() {
        return mchId;
    }

    public String getAppId() {
        return appId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Context getContext() {
        return context;
    }
}
