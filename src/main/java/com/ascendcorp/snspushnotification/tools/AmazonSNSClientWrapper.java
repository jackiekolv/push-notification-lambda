package com.ascendcorp.snspushnotification.tools;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import com.ascendcorp.snspushnotification.tools.SampleMessageGenerator.Platform;

import java.util.HashMap;
import java.util.Map;

public class AmazonSNSClientWrapper {

	private final AmazonSNS snsClient;

	public AmazonSNSClientWrapper(AmazonSNS client) {
		this.snsClient = client;
	}

	private CreatePlatformApplicationResult createPlatformApplication(
			String applicationName, Platform platform, String principal,
			String credential) {
		CreatePlatformApplicationRequest platformApplicationRequest = new CreatePlatformApplicationRequest();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("PlatformPrincipal", principal);
		attributes.put("PlatformCredential", credential);
		platformApplicationRequest.setAttributes(attributes);
		platformApplicationRequest.setName(applicationName);
		platformApplicationRequest.setPlatform(platform.name());
		return snsClient.createPlatformApplication(platformApplicationRequest);
	}

	private void deletePlatformApplication(String applicationArn) {
		DeletePlatformApplicationRequest request = new DeletePlatformApplicationRequest();
		request.setPlatformApplicationArn(applicationArn);
		snsClient.deletePlatformApplication(request);
	}

	private CreatePlatformEndpointResult createPlatformEndpoint(String customData, String platformToken, String applicationArn) {
		CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();

		platformEndpointRequest.setCustomUserData(customData);
		platformEndpointRequest.setToken(platformToken);
		platformEndpointRequest.setPlatformApplicationArn(applicationArn);

		return snsClient.createPlatformEndpoint(platformEndpointRequest);
	}

	private PublishResult publish(String endpointArn, Platform platform, String textMessage) {
		// If the message attributes are not set in the requisite method, notification is sent with default attributes
		String message = getPlatformSampleMessage(platform, textMessage);

		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put(platform.name(), message);

		message = SampleMessageGenerator.jsonify(messageMap);
		System.out.println("{Message Body: " + message + "}");

		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setMessageStructure("json");
		publishRequest.setTargetArn(endpointArn);
		publishRequest.setMessage(message);

		return snsClient.publish(publishRequest);
	}

	public PublishResult demoNotification(Platform platform, String platformToken, String platformApplicationArn, String textMessage) {

		// Create Platform Application. This corresponds to an app on a platform.
//		CreatePlatformApplicationResult platformApplicationResult = createPlatformApplication(applicationName, platform, principal, credential);
//		System.out.println(platformApplicationResult);

		// The Platform Application Arn can be used to uniquely identify the Platform Application.
//		String platformApplicationArn = platformApplicationResult.getPlatformApplicationArn();
//		String platformApplicationArn = "arn:aws:sns:ap-southeast-1:872767853649:app/APNS_SANDBOX/Carzio";

		// Create an Endpoint. This corresponds to an app on a device.
		CreatePlatformEndpointResult platformEndpointResult = createPlatformEndpoint(
				"CustomData - Useful to store endpoint specific data",
				platformToken,
				platformApplicationArn);
		System.out.println(platformEndpointResult);

		// Publish a push notification to an Endpoint.
		PublishResult publishResult = publish(platformEndpointResult.getEndpointArn(), platform, textMessage);
		System.out.println("Published! \n{MessageId=" + publishResult.getMessageId() + "}");
		return publishResult;
	}

	private String getPlatformSampleMessage(Platform platform, String textMessage) {
		switch (platform) {
		case APNS:
			return SampleMessageGenerator.getSampleAppleMessage(textMessage);
		case APNS_SANDBOX:
			return SampleMessageGenerator.getSampleAppleMessage(textMessage);
		case GCM:
			return SampleMessageGenerator.getSampleAndroidMessage();
		default:
			throw new IllegalArgumentException("Platform not supported : "
					+ platform.name());
		}
	}
}
