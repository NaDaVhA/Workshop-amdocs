import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.Webcam;


public class MainScreen{
	private static int CAPMODE=0;
	private static StatusButton PicM;
	private static StatusButton VideoM;
	private static Button up;
	private static Button down;
	private static Button[] camsB;
	private static PicsButton[] picsB;
	private static String BACK = "UI/mainscreen.png";
	private static final int NUMPICS=4;
	private static File[] mediaFiles;
	private static int displayIndex=0;
	private static JLabel playLabel;
	private static ImageIcon playIcon;
	
	public MainScreen(){
		initButtons();
	}
	
	private static void setPlayIcon(){
		
		File img = new File("UI/playSymbol.png");
		try {
			
			BufferedImage bufimg = ImageIO.read(img);
			playIcon = new ImageIcon(bufimg);
			
				
		} catch (Exception e) {

		}
		
	}
	public static JLabel getPlayLabel(){
		return playLabel;
	}
	public static ImageIcon getPlayIcon(){
		return playIcon;
	}
	
	private void initButtons(){
		setPlayIcon();
	 	PicM = new StatusButton(442,93,64,59,"UI/picUP.png","UI/picP.png");
	 	VideoM = new StatusButton(530,93,64,59,"UI/vidUP.png","UI/vidP.png");
	 	up = new Button(289,1,31,32,"UI/upUP.png","UI/upP.png");
	 	down = new Button(289,567,31,32,"UI/downUP.png","UI/downP.png");
	 	camsB = new Button[App.CAMNUM];
	 	picsB = new PicsButton[NUMPICS];
	 	for (int i=0; i< App.CAMNUM;i++){
	 		camsB[i] = new Button(479,215 + i*173,165,82,"UI/cam"+(i+1)+"UP.png","UI/cam"+(i+1)+"P.png");
	 	}
	 	for (int i=0; i<NUMPICS;i++){
	 		picsB[i] = new PicsButton(33,6+(i*148),221,141,null,null);
	 	}
	 	updatePics();
	 	setButtons(); 
	 	
	 	App.currImgNum = mediaFiles.length;
	 	
	 }
	
	private static void updateFileList(){
		File directory = new File(App.MEDIADIR);
		mediaFiles = directory.listFiles();
		Arrays.sort(mediaFiles, new Comparator<File>(){
		    public int compare(File f1, File f2)
		    {
		        return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
		    } });
		displayIndex=0;

	}

public MainScreenPanel getPanel(){
		
		return new MainScreenPanel(0,0,App.WIDTH,App.HEIGHT,BACK);
}

	
private static void setButtons(){
	
	for (int i=0; i< App.CAMNUM; i++){
		final int j=i;
		camsB[j].addMouseListener(new MouseAdapter(){
			
			
	 		@Override
	         public void mouseClicked(MouseEvent e) {
	 					camsB[j].press();
	 			if (CAPMODE == 1){
	 				
	 				(new Thread(new Runnable(){
		 				 public void run() {
		 					final String filename = App.MEDIADIR + App.MEDNAME + App.currImgNum + "."+App.IMGFORMAT;
			 				int ret =App.CAMERAS[j].capturePic(filename);
			 				if (ret==1){
			 					updatePics();
			 					App.currImgNum++;
			 				}
		 				 }
		 			})).start();
	 				
	 				}
	 			
	 			else if (CAPMODE==2){
	 				
	 				(new Thread(new Runnable(){

						@Override
						public void run() {
							App.CAMERAS[j].captureVid(App.MEDIADIR + App.MEDNAME + App.currImgNum + ".mp4"); //nadav changed to mp4
					    	MainScreen.updatePics();
							App.currImgNum++;
							
						}
	 					
	 				})).start();
	 			}
	 			
	 			
	         }
	 	});
	}
	
	
	VideoM.addMouseListener(new MouseAdapter(){
 		@Override
         public void mouseClicked(MouseEvent e) {
 			
 			VideoM.press();
 			PicM.unpress();
 			CAPMODE = 2;
 			}});
 	
 	PicM.addMouseListener(new MouseAdapter(){
 		@Override
         public void mouseClicked(MouseEvent e) {
 			VideoM.unpress();
 			PicM.press();
 			CAPMODE = 1;
 			
 			
         }
 	});
 	
 	down.addMouseListener(new MouseAdapter(){
 		@Override
         public void mouseClicked(MouseEvent e) {
 			down.press();
 			
 			(new Thread(new Runnable(){
				 public void run() {
					 if (displayIndex< mediaFiles.length-NUMPICS){
			 				displayIndex++;
			 				updatePanel();
			 			}
				 }
			})).start();		
 					
 			}});
 	up.addMouseListener(new MouseAdapter(){
 		@Override
         public void mouseClicked(MouseEvent e) {
 			up.press();
 			
 			(new Thread(new Runnable(){
				 public void run() {
					 if (displayIndex>0 ){
			 				
			 				displayIndex--;
			 				updatePanel();
			 			}
				 }
			})).start();
 			
 			
 					
 			}});
 	
 	for (int i=0;i<NUMPICS;i++){
 		final int j=i;
 		picsB[i].addMouseListener(new MouseAdapter(){
 			@Override
 	         public void mouseClicked(MouseEvent e) {
 				
 				if (App.isImage(picsB[j].getMed())){
 					
 					(new Thread(new Runnable(){
		 				 public void run() {
		 					 PicDisplay.display(picsB[j].getMed());
		 					/*JFrame f = new JFrame();
		 					PicDisplay.PicDisplayPanel display = new PicDisplay(picsB[j].getMed()).getPanel();
		 					f.add(display);
		 					f.setResizable(false);
		 					f.pack();
		 					f.setVisible(true);*/
		 				 }
		 			})).start();
 					
 					
 					/*final EditorUi edit = new EditorUi(picsB[j].getMed());
 					(new Thread(new Runnable(){
		 				 public void run() {
		 					edit.edit();
		 				 }
		 			})).start();*/

 				}

				//nadav added
	 				if(App.isVideo(picsB[j].getMed())){
	 					(new Thread(new Runnable(){
			 				 public void run() {
			 					VideoPlayer player=new VideoPlayer(picsB[j].getMed());
			 					player.playVideo();
			 				 }
			 			})).start();
	 				}

 			}
 		});
 	}
}

public static void updatePics(){
	updateFileList();
	updatePanel();

}

private static void updatePanel(){
	for (int i = 0; i< NUMPICS;i++){
		if (displayIndex+i< mediaFiles.length ){

				 picsB[i].setPic(mediaFiles[displayIndex+i].getAbsolutePath());

		}
		else{
			picsB[i].setPic(null);
		}
	}
}

class MainScreenPanel extends MyPanel {
	
	public MainScreenPanel(int x, int y, int w, int h, String back) {
		super(x, y, w, h, back);
		
	}

	
	@Override
	protected void addComponents() {
		add(PicM);
	    add(VideoM); 
	    add(up);
	    add(down);
	    for (int i=0; i< App.CAMNUM; i++){
	    	add(camsB[i]);
	    }
	    for (int i=0; i<NUMPICS;i++){
	 		add(picsB[i]);
	 	}	
	    
	    
		
	}	
    	
}
}

