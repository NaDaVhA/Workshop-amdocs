

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GoProCamera extends Camera {
	
	private String password;
	private String url;
	
	/**
	 * 
	 * @param password
	 */
	public GoProCamera(String password) {
		this.password = password;
		this.url="http://10.5.5.9";
	}
	
	/**
	 * 
	 * @param mode - 0==video  1==photo
	 * @throws Exception 
	 */
	public void changeMode(int mode) throws Exception{
		if(mode==0){
			sendCommand("http://10.5.5.9/camera/CM?t="+password+"&p=%00");
		}
		if(mode==1){
			sendCommand("http://10.5.5.9/camera/CM?t="+password+"&p=%01");
		}
		
	}
	

	public void turnON() throws Exception{
		sendCommand("http://10.5.5.9/bacpac/PW?t="+password+"&p=%01");
	}
	
	public void turnOFF() throws Exception{
		sendCommand("http://10.5.5.9/bacpac/PW?t="+password+"&p=%00");
	}

	
	public void startCapture() throws Exception{
		sendCommand("http://10.5.5.9/bacpac/SH?t="+password+"&p=%01");
	
	}
	
	public void stopCapture() throws Exception{	
		sendCommand("http://10.5.5.9/bacpac/SH?t="+password+"&p=%00");
	}
	
	public void downloadFiles(){
	     String webUrl = "http://10.5.5.9:8080/videos/DCIM/100GOPRO"; //100GOPRO IS SPECIFIED HERE, JUST CHANGE 100 BY THE NUMBER OF THE FOLDER WHER ALL THE PICS ARE.
	        final String outputFolder = System.getProperty("user.home") + File.separatorChar + "Pictures" + File.separatorChar + "gopro" + File.separatorChar;
	        
	         File outputFolderF = new File(outputFolder);
	        if (!outputFolderF.exists()) {
	          if (!(outputFolderF.mkdirs())) {
	            throw new RuntimeException("Unable to ensure folder exists:" + outputFolder);
	          }
	          System.out.println("Created download folder:" + outputFolder);
	        }

	        try {
	        	
	           Document doc = Jsoup.connect(webUrl).timeout(15 * 1_000).get();
	          
	          for (final Element link : doc.select("a")) {
	            final String linkFileName = link.attr("href");
	            if (linkFileName.endsWith("JPG")){ //|| linkFileName.endsWith("MP4")) {

	              final File destFile = new File(outputFolder + linkFileName);
	              if (destFile.exists() && destFile.length() > 1_204) {
	                System.out.println("Already have\t" + destFile);
	              } else {
	                System.out.println("Starting download! of\t" + destFile);
	           
	                Response resultImageResponse = Jsoup.connect(link.absUrl("href")).maxBodySize(0).execute();
	                OutputStream out = new BufferedOutputStream(new FileOutputStream(destFile), 1024*1024*7);
	                
	                out.write(resultImageResponse.bodyAsBytes());
	                out.flush();
	                
	                System.out.println("Downloaded\t" + destFile);
	            
	              
	            }
	        }
	      
	    }
	        }
	        catch(Exception e){
	        	
	        }
		
	}
	
	
	private boolean sendCommand(String url) throws Exception{
		URL command = new URL(url);
        URLConnection yc = command.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) //{}
           System.out.println(inputLine);
        in.close();
        return true;
	}

	@Override
	public int capturePic(String path) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int captureVid(String path) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
