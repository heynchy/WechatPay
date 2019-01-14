package com.heynchy.wechatpay.paylibs;


import com.heynchy.wechatpay.paylibs.bean.PrepayIdInfo;

public interface PrepayOrderListener {
    void Success(PrepayIdInfo prepayIdInfo);

    void Faiulre(String data);
}
