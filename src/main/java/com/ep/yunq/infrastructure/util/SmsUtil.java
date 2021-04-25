package com.ep.yunq.infrastructure.util;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

/**
 * @classname: SmsUtil
 * @Author: yan
 * @Date: 2021/4/13 20:23
 * 功能描述：
 **/
@Slf4j
@Component
public class SmsUtil {

    @Autowired
    RedisUtil redisUtil;


    // 短信应用 SDK AppID
    private static int appid = 1400452227; // SDK AppID 以1400开头
    // 短信应用 SDK AppKey
    private static String appkey = "8dd104a2baa67c22cf7d416f58cddd91";
    // 短信模板 ID，需要在短信应用中申请
    private static int templateId = 781551; // NOTE: 这里的模板 ID`7839`只是示例，真实的模板 ID 需要在短信控制台中申请
    // 签名
    private static String smsSign = "生意专家fzu"; // NOTE: 签名参数使用的是`签名内容`，而不是`签名ID`。这里的签名"腾讯云"只是示例，真实的签名需要在短信控制台申请


    /**
     * 随机生成6位随机验证码
     * 方法说明
     * @Discription:扩展说明
     * @return
     * @return String
     */
    public String createRandomVcode(){
        //验证码
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }

    /*
     *发送手机验证码(30分钟)
     */

    public String sendSms(String phoneNumber,String verifyCode){
        try {
            log.info(verifyCode);
            String[] params = {verifyCode};

            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber,
                    templateId, params, smsSign, "", "");
            log.info(String.valueOf(result));
            if (result.result == 0) {
                return verifyCode;
            } else {
                return "fail";
            }
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
        }
        return "";
    }
    public String checkSms(String phone, String code){
        Object redisVerificationCode = redisUtil.get(phone + ConstantUtil.SMS_Verification_Code.code);
        log.info(phone);
        log.info(code);
        if (ObjectUtils.isEmpty(redisVerificationCode)) {
            String message = "验证码超时,请重新获取";
            return message;
        } else if (!redisVerificationCode.equals(code)) {
            String message = "验证码错误";
            return message;
        }else {
            String message = "验证成功";
            return message;
        }
    }


    /*public Map<String, Object> checkSms(String verifyCode, String phoneNumber) {
        Map<String, Object> result = new HashMap<String, Object>();
        String rtnCode = "";
        String rtnMsg = "";
        RedisUtil redisUtil=new RedisUtil();
        try{
            String verify =redisUtil.get(phoneNumber).toString();
            if(!verify.substring(0,6).equals(verifyCode)){
                rtnCode = "-1";
                rtnMsg = "验证码错误";
                result.put("rtnCode", rtnCode);
                result.put("rtnMsg", rtnMsg);
                return result;
            }
            if((Double.parseDouble(verifyCode+System.currentTimeMillis()) - Double.parseDouble(verify)) > 1000*60*10){
                rtnCode = "-2";
                rtnMsg = "验证码过期";
                result.put("rtnCode", rtnCode);
                result.put("rtnMsg", rtnMsg);
                redisUtil.del(phoneNumber);
                return result;
            }
        }catch (NullPointerException e){
            rtnCode = "-1";
            rtnMsg = "手机号错误或验证码已被使用或验证码已过期移除";
            result.put("rtnCode", rtnCode);
            result.put("rtnMsg", rtnMsg);
            return result;
        }
        rtnCode = "0";
        rtnMsg = "验证通过";
        result.put("rtnCode", rtnCode);
        result.put("rtnMsg", rtnMsg);
        redisUtil.del(phoneNumber);
        return result;
    }*/
}
