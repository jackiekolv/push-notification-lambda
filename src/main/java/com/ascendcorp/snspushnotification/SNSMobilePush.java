package com.ascendcorp.snspushnotification;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishResult;
import com.ascendcorp.snspushnotification.tools.AmazonSNSClientWrapper;
import com.ascendcorp.snspushnotification.tools.SampleMessageGenerator.Platform;

import java.io.IOException;

public class SNSMobilePush {

	private AmazonSNSClientWrapper snsClientWrapper;

	public SNSMobilePush(AmazonSNS snsClient) {
		this.snsClientWrapper = new AmazonSNSClientWrapper(snsClient);
	}

	public void demoAppleSandboxAppNotification(String textMessage) {
		String deviceToken = "";
		String platformApplicationArn = "";
		
		snsClientWrapper.demoNotification(Platform.APNS_SANDBOX, deviceToken, platformApplicationArn, textMessage);
	}

	public PublishResult demoAndroidAppNotification(String registrationId, String textMessage) {
		
		String platformApplicationArn = "arn:aws:sns:ap-southeast-1:304788419564:app/GCM/wallet_android";

		return snsClientWrapper.demoNotification(Platform.GCM, registrationId, platformApplicationArn, textMessage);
	}

	public PublishResult demoAppleAppNotification(String deviceToken, String textMessage) {
		
		String platformApplicationArn = "arn:aws:sns:ap-southeast-1:304788419564:app/APNS/wallet_ios";

		return snsClientWrapper.demoNotification(Platform.APNS, deviceToken, platformApplicationArn, textMessage);
	}
}
