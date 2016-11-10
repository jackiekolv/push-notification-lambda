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

	public static void main(String[] args) throws IOException {
		AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(SNSMobilePush.class.getResourceAsStream("AwsCredentials.properties")));

		sns.setEndpoint("https://sns.ap-southeast-1.amazonaws.com");
		System.out.println("===========================================\n");
		System.out.println("Getting Started with Amazon SNS");
		System.out.println("===========================================\n");

		try {
			SNSMobilePush sample = new SNSMobilePush(sns);
			PublishResult publishResult = sample.demoAppleAppNotification("textMessage");
//			sample.demoAndroidAppNotification();
//			sample.demoAppleAppNotification();
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon SNS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with SNS, such as not "
							+ "being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}


	public void demoAppleSandboxAppNotification(String textMessage) {
		String deviceToken = "";
		String platformApplicationArn = "";
		
		snsClientWrapper.demoNotification(Platform.APNS_SANDBOX, deviceToken, platformApplicationArn, textMessage);
	}

	public void demoAndroidAppNotification() {
		String registrationId = "";
		String platformApplicationArn = "";

		snsClientWrapper.demoNotification(Platform.GCM, registrationId, platformApplicationArn, "Hello");
	}

	public PublishResult demoAppleAppNotification(String textMessage) {
		String deviceToken = "d6bf9ba0cb43588f5a2beae1ea912a930f30fa8a4e53331eb9ec7f48deb6e9ef"; // This is 64 hex characters.
		//String platformApplicationArn = "arn:aws:sns:ap-southeast-1:304788419564:app/APNS/wallet_ios";
		String platformApplicationArn = "arn:aws:sns:ap-southeast-1:872767853649:app/APNS/nokia3310";

		return snsClientWrapper.demoNotification(Platform.APNS, deviceToken, platformApplicationArn, textMessage);
	}
}
