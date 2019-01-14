package com.heynchy.wechatpay.paylibs.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 统一下单---所需的请求参数实体
 */
@XStreamAlias("xml")
public class OrderSendInfo {

    private String appid;             // 应用ID
    private String mch_id;            // 商户号
    private String nonce_str;         // 随机字符串
    private String body;              // 商品描述
    private String out_trade_no;      // 商品订单号（32个字符内）
    private String total_fee;         // 总金额（单位：分）
    private String spbill_create_ip;  // 终端IP
    private String notify_url;        // 通知地址
    private String trade_type;        // 交易类型（手机端：APP）
    private String sign;              // 签名 （根据微信支付的签名算法生成的字符串）

    public OrderSendInfo(String appid, String mch_id, String nonce_str, String body, String out_trade_no, String total_fee, String spbill_create_ip, String notify_url, String trade_type) {
        this.appid = appid;
        this.mch_id = mch_id;
        this.nonce_str = nonce_str;
        this.body = body;
        this.out_trade_no = out_trade_no;
        this.total_fee = total_fee;
        this.spbill_create_ip = spbill_create_ip;
        this.notify_url = notify_url;
        this.trade_type = trade_type;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    //注意排序规则ASCII排序（一般按照A-Z,a-z）就行
    @Override
    public String toString() {
        return "appid=" + appid  + '&' +
                "body=" + body + '&' +
                "mch_id=" + mch_id  + '&' +
                "nonce_str=" + nonce_str  + '&' +
                "notify_url=" + notify_url + '&' +
                "out_trade_no=" + out_trade_no + '&' +
                "spbill_create_ip=" + spbill_create_ip + '&' +
                "total_fee=" + total_fee + '&' +
                "trade_type=" + trade_type + '&';
    }
}
