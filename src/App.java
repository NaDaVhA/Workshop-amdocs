import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;



//jhkjh

public class App {
	public static final String MEDIADIR = "media/";
	public static final int WIDTH=800;
	public static final int HEIGHT=600;
	public static final int CAMNUM = 1;
	public static Camera[] CAMERAS;
	public static int currImgNum;
	public static final String MEDNAME="DAC";	
	
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	initAll();
                startGui(); 
            }
        });	
		
	}
	
	private static void initAll(){
		CAMERAS = new Camera[CAMNUM];
		CAMERAS[0] = new WCamera(Webcam.getDefault(), new Dimension(640,480),"png");
		
	}
	public static JFrame addFrame(){
		return new JFrame();
	}
	public static void startGui(){
		
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new MainScreen().getPanel());
		frame.setResizable(false);	        
		frame.pack();
		frame.setVisible(true); 

		
	}
	
	public static boolean isImage(String name){
		String type = name.substring(name.length() - 3);
		if (type.equals("png")||type.equals("jpg")||type.equals("JPG"))return true;
		else return false;
	}
	
	public static JLabel imgLabel(String path,int x, int y){
		File in = new File(path);
		try {
			BufferedImage img = ImageIO.read(in);
			JLabel ret = new JLabel();
			ret.setBounds(x,y,img.getWidth(), img.getHeight());
			ret.setIcon(new ImageIcon(img));
			return ret;
	
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
	}
}
