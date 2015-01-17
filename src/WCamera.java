import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;


public class WCamera extends Camera{
	private Webcam cam;
	private Dimension size;
	private String captureFormat;
	private boolean done;
	private boolean pause;
	private static Button pauseB=new Button(0,0,0,0,"UI/pauseUP.png","UI/pauseP.png");
	private static Button stopB=new Button(0,0,0,0,"UI/stop.png",null);
	private long toatlPausedTime=0;
	private long pausedTime=0;
	private double frameRate = 50;
	
	
	
	
	WCamera(Webcam cam, Dimension size,String captureFormat){
		this.cam = cam;
		this.size = size;
		this.captureFormat = captureFormat;
	}
	
	
	/*public static void main(String[] args){
		WCamera cam = new WCamera(Webcam.getDefault(), new Dimension(640,480),"png");
		cam.captureVid("lala.avi",50);
	}*/
	public Webcam getCam(){
		return cam;
	}
	@Override
	public int capturePic(String path) {
		cam.setViewSize(size);
		cam.open();
		try {
			ImageIO.write(cam.getImage(), captureFormat, new File(path));
		} catch (IOException e) {
			
			return 0;
		}
		cam.close();
		return 1;
	}

	@Override
	public int captureVid(String path) {
		

		final WebcamPanel panel = new WebcamPanel(cam, size, false);
		panel.setFPSDisplayed(true);
		panel.setFillArea(true);
		done=false;
		pause=false;
		
		MyPanel capPanel = new MyPanel(0,0,(int)size.getWidth(),50,"UI/videoRecBack.png");
		capPanel.add(pauseB, BorderLayout.CENTER);
		capPanel.add(stopB, BorderLayout.CENTER);
		
		setButonns();

		final JFrame f = App.addFrame();
		
		panel.start();
		f.add(panel);
		f.add(capPanel,BorderLayout.SOUTH);
		f.pack();
		f.setVisible(true);
		
		final IMediaWriter writer = ToolFactory.makeWriter(path);
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, size.width/2, size.height/2);
		long startTime = System.nanoTime();
		
		f.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				done=true;
				f.dispose();
				panel.stop();
			}
			
		});
		
		while (!done){
			if (!pause){
				BufferedImage shot = cam.getImage();
				BufferedImage bgrScreen = convertToType(shot, BufferedImage.TYPE_3BYTE_BGR);
				writer.encodeVideo(0, bgrScreen, System.nanoTime() - startTime-toatlPausedTime, TimeUnit.NANOSECONDS);
				
				try {
					Thread.sleep((long) (1000 / frameRate));
				} 
				catch (InterruptedException e) {
					return 0;
				}
			}
		}
		writer.close();
		f.dispose();
		panel.stop();
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
	 				toatlPausedTime += System.nanoTime() - pausedTime;
	 				
	 			}
	 			else{
	 				pauseB.setIcon(null);
	 				pauseB.setIcon(pauseB.pressed);
	 				pause = true;
	 				pausedTime = System.nanoTime();
	 			}
	 			
	 		}});
		
		stopB.addMouseListener(new MouseAdapter(){
	 		@Override
	         public void mouseClicked(MouseEvent e) {
	 			
	 			done=true;
	 			
	 		}});
	}
	
	public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {

		  BufferedImage image;
		  if (sourceImage.getType() == targetType) {
			  image = sourceImage;

		  }
		  else {

			  image = new BufferedImage(sourceImage.getWidth(), 
		      sourceImage.getHeight(), targetType);
			  image.getGraphics().drawImage(sourceImage, 0, 0, null);

		  }
		  return image;

		}

}
