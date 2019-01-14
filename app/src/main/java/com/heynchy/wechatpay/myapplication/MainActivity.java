package com.heynchy.wechatpay.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.heynchy.wechatpay.paylibs.PrepayOrderListener;
import com.heynchy.wechatpay.paylibs.Utils.WechatPayUtil;
import com.heynchy.wechatpay.paylibs.bean.OrderSendInfo;
import com.heynchy.wechatpay.paylibs.bean.PrepayIdInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String mPreOrderId;      // 预支付订单ID
    private TextView mCreatePreIdTv; // 生成预支付订单ID
    private TextView mPreIdTv;       // 显示预支付ID
    private TextView mWechatPayTv;   // 打开微信支付的界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

    }

    private void initView() {
        mCreatePreIdTv = findViewById(R.id.tv_preId);
        mPreIdTv = findViewById(R.id.tv_Id);
        mWechatPayTv = findViewById(R.id.tv_open);
    }

    private void initData() {
        mCreatePreIdTv.setOnClickListener(this);
        mWechatPayTv.setOnClickListener(this);
    }

    /**
     * 生成预支付订单ID----建议由后台生成
     */
    private void creatPrepayId() {
        /**
         * orderSendInfo 统一下单接口的入参信息
         * getPreOrderInfo(String describe, String money)
         *    ------  describe: 商品的描述
         *    ------  money:    商品的钱数（单位： 分）
         */
        OrderSendInfo orderSendInfo = WechatPayUtil.getPreOrderInfo("test", "1");
        WechatPayUtil.createPrepayOrder(orderSendInfo, new PrepayOrderListener() {
            @Override
            public void Success(PrepayIdInfo data) {
                String info = new Gson().toJson(data);
                mPreOrderId = data.getPrepay_id();
                mPreIdTv.setText(mPreOrderId == null ? info : mPreOrderId);
            }

            @Override
            public void Faiulre(String data) {
                Log.i("heyn1234", "生成订单ID失败： " + data);
                mPreIdTv.setText(data);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_preId:
                // 生成预支付ID-----统一下单 （建议由后台生成）
                creatPrepayId();
                break;
            case R.id.tv_open:

                if (mPreOrderId == null){
                    Toast.makeText(this,"请先获取预支付ID！",Toast.LENGTH_SHORT).show();
                    return;
                }
                // 打开支付界面----进行支付
                WechatPayUtil.wechatPay(mPreOrderId);
                break;
        }
    }
}
