/**
 * 
 */
package com.fcsbbc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
/**
 * @author luo.changshu
 *
 */
public class IdentityCodeMessage {
	private static Logger logger = LoggerFactory.getLogger(IdentityCodeMessage.class.getName());
	
	private static final String product = "Dysmsapi";				//短信API产品名称
	private static final String domain = "dysmsapi.aliyuncs.com";	//短信API产品域名
	
	private static final String accessKeyId = "XXXXXX";				//阿里云的key
	private static final String accessKeySecret = "XXXXXXX";		//阿里云的密钥
	
	public static SendSmsResponse sendSms(String telephone, String modelName, int randomIdentityCode) {
		SendSmsResponse sendSmsResponse = null;
		//设置超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        try {
        	DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
			SendSmsRequest request = new SendSmsRequest();	//组装请求对象-具体描述见控制台-文档部分内容
			request.setMethod(MethodType.POST);	//使用post提交
	        request.setPhoneNumbers(telephone);	//必填:待发送手机号。支持以逗号分隔的形式进行批量调用，上限为1000
	        request.setSignName("逸海区块链");	//必填:短信签名-可在短信控制台中找到
	        request.setTemplateCode(modelName);	//必填:短信模板-可在短信控制台中找到
	        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//	        request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
	        String message = "{\"code\":" + randomIdentityCode + "}";
	        request.setTemplateParam(message);
	        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
	        //request.setSmsUpExtendCode("90997");
	        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
	        request.setOutId("fcsbbc");
	        //请求失败这里会抛ClientException异常
	        sendSmsResponse = acsClient.getAcsResponse(request);
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			logger.debug("Sending Message Failed in function<IdentityCodeMessage:sendSms>", e.getMessage());
		}
        return sendSmsResponse;
	}
	
	public static QuerySendDetailsResponse querySendDetails(String bizId, String telephone) {
		QuerySendDetailsResponse querySendDetailsResponse = null;
		//可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
			 IAcsClient acsClient = new DefaultAcsClient(profile);
	        //组装请求对象
	        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
	        //必填-号码
	        request.setPhoneNumber(telephone);
	        //可选-流水号
	        request.setBizId(bizId);
	        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
	        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
	        request.setSendDate(ft.format(new Date()));
	        //必填-页大小
	        request.setPageSize(10L);
	        //必填-当前页码从1开始计数
	        request.setCurrentPage(1L);
	        //hint 此处可能会抛出异常，注意catch
	        querySendDetailsResponse = acsClient.getAcsResponse(request);
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			logger.debug("Sending Message Failed in function<IdentityCodeMessage:querySendDetails>", e.getMessage());
		}
        return querySendDetailsResponse;
	}
	
	public static void main(String[] args) {
		int randomIdentityCode = (int) ((Math.random() * 9 + 1) * 100000);	//验证码随机生成
		String telephone = "15021241266";
		String modelName = "SMS_125025516";
		//发短信
        SendSmsResponse response = sendSms(telephone, modelName, randomIdentityCode);
        System.out.println("NULL Object : " + response);
        if (response != null && !response.equals("")) {
        	System.out.println("ok");
        	System.out.println("----------------短信接口返回的数据----------------");
            System.out.println("Code      = " + response.getCode());
            System.out.println("Message   = " + response.getMessage());
            System.out.println("RequestId = " + response.getRequestId());
            System.out.println("BizId     = " + response.getBizId());
		}
        

        try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //查明细
        if(response != null && !response.equals("") && response.getCode() != null && response.getCode().equals("OK")) {
            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId(), telephone);
            System.out.println("----------------短信明细查询接口返回数据----------------");
            System.out.println("Code    = " + querySendDetailsResponse.getCode());
            System.out.println("Message = " + querySendDetailsResponse.getMessage());
            int i = 0;
            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs()) {
                System.out.println("SmsSendDetailDTO[" + i + "]:");
                System.out.println("Content     = " + smsSendDetailDTO.getContent());
                System.out.println("ErrCode     = " + smsSendDetailDTO.getErrCode());
                System.out.println("OutId       = " + smsSendDetailDTO.getOutId());
                System.out.println("PhoneNum    = " + smsSendDetailDTO.getPhoneNum());
                System.out.println("ReceiveDate = " + smsSendDetailDTO.getReceiveDate());
                System.out.println("SendDate    = " + smsSendDetailDTO.getSendDate());
                System.out.println("SendStatus  = " + smsSendDetailDTO.getSendStatus());
                System.out.println("Template    = " + smsSendDetailDTO.getTemplateCode());
            }
            System.out.println("TotalCount  = " + querySendDetailsResponse.getTotalCount());
            System.out.println("RequestId   = " + querySendDetailsResponse.getRequestId());
        }
    }
}
