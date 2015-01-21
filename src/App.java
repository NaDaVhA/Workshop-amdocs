import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

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




public class App {
	public static final String IMGFORMAT = "jpeg";
	public static final String MEDIADIR = "media/";
	public static final int WIDTH=800;
	public static final int HEIGHT=600;
	public static final int CAMNUM = 2; //nadav changed
	public static Camera[] CAMERAS;
	public static int currImgNum;
	public static final String MEDNAME="DAC";
	public static User currUser;	
	public static boolean terminated = false;
	
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	
            	initAll();
                startGui();
                new Thread(new Runnable(){
      				 public void run() {
      					EventHandler.setEventsListeners();
    //  					EventHandler.simulateEvents();
      				 }
      			}).start();
            }
        });	
		
	}
	
	private static void initAll(){
		CAMERAS = new Camera[CAMNUM];
		/*List<Webcam> cams = Webcam.getWebcams();
		for (int i=0; i< cams.size(); i++){
			if ((cams.get(i)).getName().equals("Microsoft LifeCam VX-2000 0")){
				CAMERAS[0] = new WCamera(cams.get(i), new Dimension(640,480),IMGFORMAT);
				break;
			}
		}*/
		
		CAMERAS[0] = new WCamera(Webcam.getDefault(), new Dimension(640,480),IMGFORMAT);
		if (CAMNUM>1)CAMERAS[1]= new GoProCamera("Nadav2471987");
		
		
		currUser = new User();
		Properties newProp = new Properties();
		try {
			FileOutputStream configFileStreamOut = new FileOutputStream(WriteData.getVIDEODATAPATH());
			newProp.store(configFileStreamOut, null);
			configFileStreamOut.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void startGui(){
		
		
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new MainScreen().getPanel());       
		frame.pack();
		frame.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				terminated=true;
				frame.dispose();
				
			}
			
		});
		frame.setVisible(true); 

		
	}
	
	public static boolean isImage(String name){
		if (name== null)return false;
		String type = name.substring(name.length() - 3);
		if (type.equals("png")||type.equals("jpg")||type.equals("JPG")|| type.equals("peg"))return true;
		else return false;
	}
	
	//nadav added this function
	public static boolean isVideo(String name){
		if (name== null)return false;
		String type = name.substring(name.length() - 3);
		if (type.equals("mp4")||type.equals("avi"))return true;
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






