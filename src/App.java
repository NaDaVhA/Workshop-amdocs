import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;



public class App {
	public static final String IMGFORMAT = "jpg";
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
		CAMERAS[0] = new WCamera(Webcam.getDefault(), new Dimension(640,480),IMGFORMAT);
		
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
		if (name== null)return false;
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
	
	
	
	public static MesPanel getSafeSharePanel(){
		Button[] buttons = new Button[2];
		buttons[0] = new Button(304,189,177,30,"UI/notdriver.png","UI/notdriverP.png");
		buttons[1] = new Button(115,189,177,30,"UI/share.png","UI/shareP.png");
		MesPanel ret = new MesPanel(0,0,500,240,"UI/safeshare.png",buttons);
		return ret;
	}
	
	public static MesPanel getSafeEditPanel(){
		Button[] buttons = new Button[1];
		buttons[0] = new Button(304,189,177,30,"UI/notdriver.png","UI/notdriverP.png");
		MesPanel ret = new MesPanel(0,0,500,240,"UI/safeedit.png",buttons);
		return ret;
	}
	
	
}






