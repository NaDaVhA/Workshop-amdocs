import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.imgscalr.Scalr;

import com.xuggle.mediatool.IMediaDebugListener.Event;

public class PicDisplay {
	private String path;
	private JLabel label;
	private JLabel piclabel;
	private JFrame frame;

	public PicDisplay(String path){
		 this.path = path;
         setLabels();
	}
	public PicDisplayPanel getPanel(){
		return new PicDisplayPanel(0,0,App.WIDTH ,App.HEIGHT ,"UI/back.png");
	}
	public static void display(String path){
		PicDisplay dis =  new PicDisplay(path);
		PicDisplayPanel panel = dis.getPanel();
		dis.frame = new JFrame();
		dis.frame.add(panel);
		dis.frame.pack();
		dis.frame.setVisible(true);
	}
	private void setLabels(){
    	
    	label = App.imgLabel("UI/videoRecBack.png", 0, 550);
    	final Button edit =new Button(6,4,46,43,"UI/edit.png","UI/editP.png");
    	final Button facebook =new Button(748,4,46,43,"UI/facebook.png","UI/facebookP.png");
    	final JLabel twiter =App.imgLabel("UI/twiter.png",693,4);
    	final JLabel gplus =App.imgLabel("UI/g+.png",638,4);
    	
    	label.add(edit);
    	label.add(gplus);
    	label.add(twiter);
    	label.add(facebook);

    	
    	piclabel = new JLabel();
    	File pic = new File(path);
    	BufferedImage img;
		try {
			img = ImageIO.read(pic);
			BufferedImage imgresized = Scalr.resize(img, App.WIDTH, App.HEIGHT -50);
			piclabel.setIcon(new ImageIcon(imgresized));
			piclabel.setBounds(0,0,App.WIDTH,App.HEIGHT -50);
		} catch (IOException e1) {

			e1.printStackTrace();
		}
    	
    	
    	
    	edit.addMouseListener(new MouseAdapter(){
    		@Override
            public void mouseClicked(MouseEvent e) {
    			edit.press();
    			(new Thread(new Runnable(){
   				 public void run() {
   					 final MesPanel p = App.getSafeEditPanel();
   					final JFrame f = new JFrame();
   					 p.getButtons()[0].addMouseListener(new MouseAdapter(){
   						@Override
   		                public void mouseClicked(MouseEvent e) {
   							p.getButtons()[0].press();
   							f.dispose();
   							frame.dispose();
   							final EditorUi edit = new EditorUi(path);
   		 					(new Thread(new Runnable(){
   				 				 public void run() {
   				 					edit.edit();
   				 				 }
   				 			})).start();
   						}
   					 });
   					
   					 f.add(p);
   					
   					 f.pack();
   					 f.setVisible(true);
   					 
   					 
   				 }
   			})).start();
    		}
    	});
    	
    	
    	facebook.addMouseListener(new MouseAdapter(){
    		@Override
            public void mouseClicked(MouseEvent e) {
    			facebook.press();
    			(new Thread(new Runnable(){
   				 public void run() {
   					 final MesPanel p = App.getSafeSharePanel();
   					final JFrame f = new JFrame();
   					 p.getButtons()[0].addMouseListener(new MouseAdapter(){
   						@Override
   		                public void mouseClicked(MouseEvent e) {
   							p.getButtons()[0].press();
   							f.dispose();
   							//frame.dispose();
   							
   		 					(new Thread(new Runnable(){
   				 				 public void run() {
   				 					PostUI.displayPost(path, "facebook");
   				 				 }
   				 			})).start();
   						}
   					 });
   					p.getButtons()[1].addMouseListener(new MouseAdapter(){
   						@Override
   		                public void mouseClicked(MouseEvent e) {
   							p.getButtons()[0].press();
   							f.dispose();
   							frame.dispose();
   		 					(new Thread(new Runnable(){
   				 				 public void run() {
   				 					PostUI.share(path, "facebook",null);
   				 				 }
   				 			})).start();
   						}
   					 });
   					 f.add(p);
   					 
   					 f.pack();
   					 f.setVisible(true);
   					 
   					 
   				 }
   			})).start();
    		}
    	});
    	

    }
	
class PicDisplayPanel extends MyPanel {

	public PicDisplayPanel(int x, int y, int w, int h, String back) {
		super(x, y, w, h, back);
		
	}
	@Override
	protected void addComponents(){
		add(label);
		add(piclabel);
	};

}
}