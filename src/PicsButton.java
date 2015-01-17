import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.bytedeco.javacv.FFmpegFrameGrabber;


public class PicsButton extends Button{
	
	private String mediaPic;
	private JLabel play;
	
	
	public PicsButton(int x, int y, int w, int h, String imageUP, String imageP) {
		super(x, y, w, h, null, null);
		mediaPic = imageUP;
		play = new JLabel();
		play.setBounds(88,53,45,45);
		add(play);
		play.setIcon(null);
		
	}
	
	public void showPlay(boolean b){
		if (b)play.setIcon(MainScreen.getPlayIcon());
		else play.setIcon(null);
	}
	
	public void setPic(String path){
		BufferedImage bufimg;
		if (path!=null){
		mediaPic=path;
		File img = new File(path);
		 
		 
		 if(!App.isImage(img.getName())){
			 
			 
			 try {
				FFmpegFrameGrabber g = new FFmpegFrameGrabber(path);
				g.start();
				bufimg = g.grab().getBufferedImage();
				g.stop();
				Image dimg = bufimg.getScaledInstance(w,h,Image.SCALE_SMOOTH);
				ImageIcon icon = new ImageIcon(dimg);
				setIcon(null);
				setIcon(icon);
				showPlay(true);
					
			} catch (Exception e) {

			}

			 
		 }
		 else{
			 
			 try {
				 bufimg = ImageIO.read(img);
					Image dimg = bufimg.getScaledInstance(w,h,Image.SCALE_SMOOTH);
					ImageIcon icon = new ImageIcon(dimg);
					setIcon(null);
					setIcon(icon);
					showPlay(false);
				} catch (IOException e) {

				}
		 }
		
		
		
	}
	}

}
