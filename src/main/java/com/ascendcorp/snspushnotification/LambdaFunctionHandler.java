package com.ascendcorp.snspushnotification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
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
    	
    	
    	Connection conn = null;
    	String connectionString = System.getenv("connection_string");
//    	String connectionString = "jdbc:mysql://dbone.cbvpxlkpqfey.ap-southeast-1.rds.amazonaws.com/test?"
//        + "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=root&password=rootroot";

        try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(connectionString);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
    	String sqlString = "SELECT * FROM test.device_profile WHERE id = ("
    			+ "SELECT MAX(id) FROM test.device_profile WHERE phone = '" + input.getPhone() + "' "
    			+ ")";
    	
    	//String device_id = exceuteSql(sqlSelect, conn);
    	Statement stmt = null;
    	ResultSet rs = null;
    	String device_id = null;
    	String os = null;

    	try {
    	    
    	    stmt = conn.createStatement();
    	    if(sqlString.startsWith("SELECT"))
    	    	rs = stmt.executeQuery(sqlString);
    	    
    	    while (rs != null && rs.next()) {
                device_id = rs.getString("device_id");
                os = rs.getString("os");
            }
    	    
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if (rs != null) {
		        try { rs.close(); } 
		        catch (SQLException sqlEx) { } // ignore
		        rs = null;
		    }
		    if (stmt != null) {
		        try { stmt.close(); } 
		        catch (SQLException sqlEx) { } // ignore
		        stmt = null;
		    }
		    if (conn != null) {
		        try { conn.close(); } 
		        catch (SQLException sqlEx) { } // ignore
		        conn = null;
		    }
		}
	    
    	if(device_id == null || device_id.isEmpty()) return "Device ID not found!";
    	
		System.out.println("Device ID: " + device_id);
    	
		AmazonSNS sns = null;
//		BasicAWSCredentials credentials = new BasicAWSCredentials( "AKIAJNOMCLKK3Y3YHE7Q", "eAMsKjgiIyx6IOU8QxeOLv0jltOtKdykmJ6/iOkR" );
		BasicAWSCredentials credentials = new BasicAWSCredentials( System.getenv("AWS_SNS_KEY_ID"), System.getenv("AWS_SNS_ACCESS_KEY") );
		sns = new AmazonSNSClient(credentials);

		sns.setEndpoint("https://sns.ap-southeast-1.amazonaws.com");
		System.out.println("===========================================\n");
		System.out.println("Getting Started with Amazon SNS");
		System.out.println("===========================================\n");

		PublishResult publishResult = null;
		
		try {
			SNSMobilePush sample = new SNSMobilePush(sns);
			
//			String registrationId = "APA91bE1Rw7YkPtvpRasrwveOetEHfABlD3pmSx8yueUMhFAxYm2x9GWrv0ALoXgPdE1XCNwB9ocspODYESMduU1tBK0xB-Tha9iVLGIXPztjIH-hyN2J0juFvnqznmxrrJXY-LLMsYp";
//			String deviceToken = "d6bf9ba0cb43588f5a2beae1ea912a930f30fa8a4e53331eb9ec7f48deb6e9ef"; // This is 64 hex characters.
			if("ios".equals(os))
				publishResult = sample.demoAppleAppNotification(device_id, input.getTextMessage());
			else if("android".equals(os))
				publishResult = sample.demoAndroidAppNotification(device_id, input.getTextMessage());
			
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
        return publishResult.toString();
    }
    
    public static void main(String[] args) {
		LambdaFunctionHandler test = new LambdaFunctionHandler();
		RequestObject input = new RequestObject();
		input.setPhone("0866060132");
		input.setTextMessage("Hellooooooo!!!");
		test.handleRequest(input, null);
	}
    

}
