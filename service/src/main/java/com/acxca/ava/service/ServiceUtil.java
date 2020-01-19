package com.acxca.ava.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ServiceUtil {

    private final Context context;
    private final Map<String,Object> sharedBag;

    @Inject
    public ServiceUtil(Context context,Map<String,Object> sharedBag) {
        this.context = context;
        this.sharedBag = sharedBag;
    }

    public Map<String,String> getTokenHeader(){
        String token = sharedBag.get(ServiceConsts.SB_KEY_TOKEN).toString();
        Map<String,String> header = new HashMap<>();
        header.put(ServiceConsts.API_HEADER_AUTHORIZATION_TYPE_LABEL,"Bearer "+token);
        return header;
    }

    public String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }
}
