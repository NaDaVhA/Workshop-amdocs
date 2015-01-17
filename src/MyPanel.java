import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
    	int x;
    	int y;
    	int w;
    	int h;
    	String back;
    	
    	
        public MyPanel(int x,int y,int w,int h,String back) {
            
            this.x=x;
            this.y=y;
            this.w=w;
            this.h=h;
            this.back = back;
        }
     
        public Dimension getPreferredSize() {
            return new Dimension(w,h);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);       
       
            File imgF = new File(back);
            
        	try {
    			BufferedImage img = ImageIO.read(imgF);
    			g.drawImage(img,x,y,null);
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        	
        	
        	setLayout(null);
        	addComponents();                  
        }
        
        protected void addComponents(){};
}