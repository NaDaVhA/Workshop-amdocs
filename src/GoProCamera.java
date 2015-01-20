

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFrame;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.github.sarxos.webcam.WebcamPanel;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

public class GoProCamera extends Camera {
	
	private String password;
	private String url;
	
	private boolean done;
	private boolean pause;
	private Dimension size;
	private static Button pauseB=new Button(0,0,0,0,"UI/pauseUP.png","UI/pauseP.png");
	private static Button stopB=new Button(0,0,0,0,"UI/stop.png",null);
	
	/**
	 * 
	 * @param password
	 */
	public GoProCamera(String password) {
		this.password = password;
		this.url="http://10.5.5.9";
		this.size=new Dimension(640,480);
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
	
	public void deleteAllFile() throws Exception{
		//http://10.5.5.9/camera/DL?t=Nadav2471987
		sendCommand("http://10.5.5.9/camera/DA?t="+password);
	}
	
	public void downloadFiles(String path,String fileType){
	     String webUrl = "http://10.5.5.9:8080/videos/DCIM/999GOPRO"; //100GOPRO IS SPECIFIED HERE, JUST CHANGE 100 BY THE NUMBER OF THE FOLDER WHER ALL THE PICS ARE.
	        //final String outputFolder = System.getProperty("user.home") + File.separatorChar + "Pictures" + File.separatorChar + "gopro" + File.separatorChar;
	        
	      /*   File outputFolderF = new File(outputFolder);
	        if (!outputFolderF.exists()) {
	          if (!(outputFolderF.mkdirs())) {
	            throw new RuntimeException("Unable to ensure folder exists:" + outputFolder);
	          }
	          System.out.println("Created download folder:" + outputFolder);
	        }*/

	        try {
	        	
	           Document doc = Jsoup.connect(webUrl).timeout(15 * 1_000).get();
	          
	          for (final Element link : doc.select("a")) {
	            final String linkFileName = link.attr("href");
	            if (linkFileName.endsWith(fileType)) {

	              final File destFile = new File(path); //create new file
	              if (destFile.exists() && destFile.length() > 1_204) {
	                System.out.println("Already have\t" + destFile);
	              } else {
	                System.out.println("Starting download! of\t" + destFile);
	           
	                Response resultImageResponse = Jsoup.connect(link.absUrl("href")).ignoreContentType(true).maxBodySize(0).execute();
	                OutputStream out = new BufferedOutputStream(new FileOutputStream(destFile), 1024*1024*25);
	                out.write(resultImageResponse.bodyAsBytes());
	                out.flush();
	                
	                System.out.println("Downloaded\t" + destFile);
	                
	              
	            }
	        }
	      
	    }
	        }
	        catch(Exception e){
	        	System.out.println("exception "+e.getMessage());
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
		try {
			changeMode(1);
			Thread.sleep(1000);
			startCapture();
			Thread.sleep(1000);
			downloadFiles(path,"JPG");
			Thread.sleep(2000);//wait
			deleteAllFile();
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	
		//no need to stop this is only pic
		return 1;
	}

	@Override
	public int captureVid(String path) {
		// TODO Auto-generated method stub
		try {
			//final WebcamPanel panel = new WebcamPanel(cam, size, false);
			
		/*	 done=false;
			 pause=false;
			
			MyPanel capPanel = new MyPanel(0,0,(int)size.getWidth(),50,"UI/videoRecBack.png");
			capPanel.add(pauseB, BorderLayout.CENTER);
			capPanel.add(stopB, BorderLayout.CENTER);
			
			setButonns();

			final JFrame f = new JFrame();
			
			//panel.start();
			//f.add(panel);
			f.add(capPanel,BorderLayout.SOUTH);
			f.pack();
			f.setVisible(true);
			
			//final IMediaWriter writer = ToolFactory.makeWriter(path);
			//writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, size.width/2, size.height/2);
			f.addWindowListener(new WindowAdapter(){

				@Override
				public void windowClosing(WindowEvent arg0) {
					done=true;
					f.dispose();
					//panel.stop();
					System.out.println("closed! qaqa");
				}
				
			});*/
			changeMode(0);
			Thread.sleep(1000); //wait 1 seconds
			startCapture();
			//need to stop capture....
			Thread.sleep(3000); //wait 3 seconds
			stopCapture();
			Thread.sleep(1500); //wait 1.5 seconds
			downloadFiles(path,"MP4");
			//Thread.sleep(15000);//wait till video is downloaded
			deleteAllFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
		return 1;
	}
	
	private void setButonns(){
		pauseB.addMouseListener(new MouseAdapter(){
	 		@Override
	         public void mouseClicked(MouseEvent e) {
	 			
	 			if (pause){
	 				pauseB.setIcon(null);
	 				pauseB.setIcon(pauseB.unpressed);
	 				pause = false;
	 				//toatlPausedTime += System.nanoTime() - pausedTime;
	 				
	 			}
	 			else{
	 				pauseB.setIcon(null);
	 				pauseB.setIcon(pauseB.pressed);
	 				pause = true;
	 				//pausedTime = System.nanoTime();
	 			}
	 			
	 		}});
		
		stopB.addMouseListener(new MouseAdapter(){
	 		@Override
	         public void mouseClicked(MouseEvent e) {
	 			
	 			done=true;
	 			
	 		}});
	}
	

}
