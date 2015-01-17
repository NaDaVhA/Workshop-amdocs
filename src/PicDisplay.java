import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PicDisplay extends JPanel {
    	int x;
    	int y;
    	String path;
    	
        public PicDisplay(int x,int y,String path) {
            
            this.x=x;
            this.y=y;
            this.path = path;
        }
     
        public Dimension getPreferredSize() {
            return new Dimension(App.WIDTH,App.HEIGHT);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);       

            
            File imgF = new File(path);
            
        	try {
    			BufferedImage img = ImageIO.read(imgF);
    			Image dimg = img.getScaledInstance(App.WIDTH,App.HEIGHT,
    			        Image.SCALE_SMOOTH);
    			g.drawImage(dimg,x,y,null);
    			
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}          
    	    
            
        }  
    }