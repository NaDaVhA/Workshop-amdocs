
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.json.simple.parser.ParseException;

import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;


public class FacebookUpload {

	private static final String MY_ACCESS_TOKEN = "CAAWoyR2IYXYBAOHO1jAYIyuDiUtbElvnMp4wkAiFP6l1VICIW707jsaEWRcWpkKVwac8nxY60VdhJg8LKSZCxW2QOMNorNRfFhv3SHxjrEkYFMZA92fZCLz1hfQ43FEl0PXJ9ZCpV9tjZASzwAmOZBy7lTzLfXNolPoWroxZAkEhCxtstWKIbiA4cRlGxCEpXbvm0E5RelZBFLdRgKAG36ro";
	private static final String MY_APP_ID="1592956620923254";
	private static final String MY_APP_SECRET="224daff3855ffa9bf91e79b651ec037d";
	
	
	private static FacebookClient initFacebookUser() {
		AccessToken extendedToken =
				  new DefaultFacebookClient().obtainExtendedAccessToken(MY_APP_ID,
				    MY_APP_SECRET, MY_ACCESS_TOKEN);
		String extendedStringToken = extendedToken.getAccessToken();
		FacebookClient facebookClient = new DefaultFacebookClient(extendedStringToken);

		return facebookClient;
		
	}
	
	
	
	@SuppressWarnings("deprecation")
	static int sharePhotoOnFacebook(String photoPath,String facebookMessage){
		FacebookClient facebookClient = initFacebookUser();
		InputStream is;
		try {
			is = new FileInputStream(new File(photoPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		}

		if(facebookMessage==null){
			File photoFile = new File(photoPath);
			try {
				facebookMessage=WriteExifMetadata.readExifMetadata(photoFile);
			} catch (ImageReadException | ImageWriteException | IOException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@SuppressWarnings("unused")
		FacebookType publishPhotoResponse =facebookClient.publish("me/photos",FacebookType.class,
		            BinaryAttachment.with(photoPath, is),
		            Parameter.with("message", facebookMessage));
	
		return 1;


	}
	
	@SuppressWarnings("deprecation")
	static void UploadVideoToFacebook(String videoPath,String facebookMessage) throws IOException{
		FacebookClient facebookClient = initFacebookUser();
		InputStream is = new FileInputStream(new File(videoPath));
		facebookClient.publish("me/videos", FacebookType.class,
				  BinaryAttachment.with(videoPath,is),
				  Parameter.with("message", facebookMessage));
		
	}

	
	
    
}
	
	