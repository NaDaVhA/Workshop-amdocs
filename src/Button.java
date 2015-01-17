import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;


public class Button extends JLabel{
	protected int x;
	protected int y;
	protected int w;
	protected int h;
	public ImageIcon pressed;
	public ImageIcon unpressed;
	
	public Button(int x,int y,int w,int h, String imageUP,String imageP){
		super();
		try {
			if (imageP!=null){File imgP = new File(imageP);
				BufferedImage bufimgP = ImageIO.read(imgP);
				ImageIcon iconP = new ImageIcon(bufimgP);
				pressed = iconP;
			}
			else{pressed=null;}
			if (imageUP!=null){
			File imgUP = new File(imageUP);
			BufferedImage bufimgUP = ImageIO.read(imgUP);
			ImageIcon iconUP = new ImageIcon(bufimgUP);
			unpressed = iconUP;
			}
			else{unpressed=null;}
			if (w!=0 && h!=0){
				this.setBounds(x, y,w, h );
			}
			this.setIcon(unpressed);
			this.x=x;
			this.y=y;
			this.w=w;
			this.h=h;
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void press(){
		
		(new Thread(new Runnable(){
			 public void run() {
				 setIcon(null);	
				 setIcon(pressed);
				 new java.util.Timer().schedule( 
					        new java.util.TimerTask() {
					            @Override
					            public void run() {  
					            	setIcon(null);	
					            	setIcon(unpressed);	
					            }
					        }, 
					        250);
			    }
		})).start();
		
		
		
		
	}
}
