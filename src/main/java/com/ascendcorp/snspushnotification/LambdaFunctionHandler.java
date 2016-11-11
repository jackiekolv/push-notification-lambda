package com.ascendcorp.snspushnotification;

import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishResult;

public class LambdaFunctionHandler implements RequestHandler<RequestObject, String> {

//    @Override
    public String handleRequest(RequestObject input, Context context) {
    	if(context != null)
    		context.getLogger().log("Input: " + input);

		AmazonSNS sns = null;
		BasicAWSCredentials credentials = new BasicAWSCredentials( "AKIAIWDLLRCIOZRGSGPA", "B9I+6BCseyjw2Zt6ydimsNF3MmyKIfbZOZbKVhKp" );
//			sns = new AmazonSNSClient(
//					new PropertiesCredentials(SNSMobilePush.class.getResourceAsStream("./AwsCredentials.properties")));
		sns = new AmazonSNSClient(credentials);

		sns.setEndpoint("https://sns.ap-southeast-1.amazonaws.com");
		System.out.println("===========================================\n");
		System.out.println("Getting Started with Amazon SNS");
		System.out.println("===========================================\n");

		PublishResult publishResult = null;
		
		try {
			SNSMobilePush sample = new SNSMobilePush(sns);
			publishResult = sample.demoAppleAppNotification(input.getTextMessage());
//			sample.demoAppleSandboxAppNotification();
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
        
        // TODO: implement your handler
        return publishResult.getMessageId();
    }
    
    public static void main(String[] args) {
		LambdaFunctionHandler test = new LambdaFunctionHandler();
		test.handleRequest(new RequestObject(), null);
	}

}
