package com.nchu.lecturerplatform.config;

import org.springframework.context.annotation.Configuration;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

@Configuration
public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2021000117660463";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCb46/PAOeoouZmvXvtLCBjn+hv2rEdIAcWLBko2xA8fWsi8eX5p25zRBgv5RF+x1Tc18A7cK7Ri+rmF6iLasn8odaD1rJjf6c3DpsFz+8hXY6g5NrRpm3B9hseEDt2o6S6skbVqTbATlZluIM4uIJ87korqnUEsTL8Vots8lIH0xN2xAYeT0ZqAyE9kPvxkrkmSeg5kuo14SbDocveBdjtjspARy6LJ2FaEhLxuTICioS4ACx2fm3SkZmPfwMA5uRNOen/QspQ8QYtlSyVeunh6GnCSxSe2I+tTxyLEFky7UDb8fO+qruYGm/gPZT2KoRRFpbBkraglI1akSkcxISNAgMBAAECggEAQlaNLUkUTCYuaBBAmXhKvDqDXn4xGtTTTzMq7cDzUtqIahGIrUn77Z9Pr6oKXOoUifpiOqlEK7YgGxR8TAAAvrpcIDSQK1laXkqf28RV3dwDYyKgn9D0KjNB7PQ5ac5Pvoc5L5lXzFRJ3fW8GbojBWkeNSDPtVR2/v0RTiZ8lAkqinWhw7wiFiVnt/Sx6AOChrPw3TPdgS905JRa6MELvXBYYO7nkpfpXRFqYKzXOshOVov1/7M/Wk79fGfApGesQ9eCrK+QJDJ9lmmCfNcQ9RymlqRqXyeiATPvFB1PjdIYDZ96o2MMyYqlnLSDE43Oc521WorY6VPd3lRTjfXUXQKBgQDiPu2HqiSMYR8vBaZ6Dd6MDpii2vv2G8B1ioCzTIbrg9vpMe8TSXx/XS9rvuLNX2ro6EUq4W+HQ9QkXWXttwOs53L9Y/G0TYa8laP5WUSfQRrukN5M7fjkGs4mbZC0nAR/4VjXN63/mqa0rUYVU5ezq1Sb8GYkUdJXhHsSPEgmvwKBgQCwZA1+c04T3pQpUXCSUFYch/8KoVNEuRET4aFjypBH42V7PL4etmebxEweWoIXf47jNX+AVRhPET9YlJIccBRbHlPuW68cUABCjc6yPsb1UuI18TBC2lFmTErR0e51NHu+BD/xHFYhIXeq8H1TJt2pnQuPlTRJfwsjOmeMDB7TswKBgESZLe4ngYNq40ifnvV9BRBUUKV4XlMl4PoCr7ktp+77AQ+4yzN+4ZfAimNR8nSwzLqnymkRB37s6wLHquMjzaJ8XtpDloLDyumdOaWeEndzBxZ0qGKM+R04n275Y5nPgHHBwu0mfcR1gLxhmVFYEjQw66uIfX/fqzZL/gDudDYDAoGBAJR4Mu97qH2K/NTmasWM/15WjjLpZYhT6e79HPY9G0KXQeUAVNrPF9iB2rw13Ihg4umWLzLa7XIoB1TNAUsUf93OYkEgRP2kzQM4D4Hy207FLAjRcZjj4F4l21hOxtM6kOl49EVDX6/VVr+4WxSxgyRwPrnlkbRHTh2h3GSCEzSHAoGAJtcEcMbLwgQAYHL5+eebrq8rE4YfAumGFgKVDDq8Rbq27OAd5YL6zSyytcPu8uQ3WtWJvWWzHlP/35OB8MeNzlN8C9UDLmlyRwuwIbzq9Z1H1TvTx67MrAMxs7En+6R4/0p8b5bWEZsFwrAjfZFTJ72n641wAzHo2wO7DsApzyU=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxIRJyXd4H9E5ep5xHo5fq53uK1cnFJx3cjmIB0xkJ92ZLYBRFR8xuibqRXPXPIsA70nKGDTlLDKgl2EIHdb1W9isJj+QApymP91f7TkmYSVkWfz2sKfBt3XIYfRgGjOpdqdKaPGCPrDIS42xGOJMyJCW/F8zweLCbiOcYuJz42bEAgAjfwI2PKMUV9oqjaJDkg6ldYDSbVHjGjx4aC5TUfecyObDNgHkOF8GERkK7i6lNCJ/QlAdB9L0/kNmcwmePOuOIBCleodmGLfSNMupoNzSm8x2h2IntAUZAnDFjoDa2sK5CKa6pGaje9YGcVdMjIhpz5vGX7wZP2FJvw0eGQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://localhost:8080/student/course/alipay/notify";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://localhost:8080/student/course/alipay/return";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

