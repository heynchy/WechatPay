# WechatPay
Android 微信支付功能的简单集成和入坑说明，集成了微信支付的统一下单功能和支付功能
#### 微信支付功能介绍：   
     1. 统一下单功能-----这个实际应用中需放在服务端进行处理
     2. 支付功能-----调起支付界面，接收支付的返回状态
### Usage
#### 1. Add dependency
```groovy
	dependencies {
	        implementation 'com.github.heynchy:WechatPay:0.0.1'
	}
```
#### 2. 配置build.gradle(app 目录下的，非根目录)--配置签名文件，并使用在buildType中，例如：
```groovy
      defaultConfig {
        //  applcation ID 要与微信开发平台的配置保持一致性    
        applicationId "com.test.application"
        .......
    }
  // 配置的签名文件要与微信开发平台保持一致
  signingConfigs {
        config {
            storeFile file("签名文件的路径名称")
            storePassword "密码"
            keyAlias "alias的名称"
            keyPassword "密码"
        }
    }
    
  buildTypes {
      debug {
            // 配置签名文件后的使用
            signingConfig signingConfigs.config
            ......
        }
```
    注意事项： 签名文件要与微信开发平台中配置的签名文件保持一致， APPlication ID 要与微信端的配置保持一致
#### 2. 配置AndroidManifest.xml 文件
```java
    <activity android:name="com.test.application.wxapi.WXPayEntryActivity"
            android:exported="true"/>
```
    注意事项：com.test.application.wxapi.WXPayEntryActivity 此处路径要与applicationID 保持一致，否则会出现
             不能够接收返回通知的问题
#### 3. 	     
	     
	     
License
-------
    Copyright 2019 heynchy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

