import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;


abstract public class Tab extends JLabel{
	private int w;
	private int h;
	private JButton tabs[];
	private ImageIcon images[];
	private int buttonNum;
	
	
	public Tab(int x, int y, int w, int h, String tabs[]){
		this.w=w;
		this.h=h;
		setBounds(x,y,w,h);
		buttonNum = tabs.length;
		this.tabs = new JButton[buttonNum];
		images = new ImageIcon[buttonNum];
		
		for (int i=0; i< buttonNum; i++){
			File imgF = new File(tabs[i]);
            
        	try {
    			BufferedImage img = ImageIO.read(imgF);
    			images[i] = new ImageIcon(img);
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
		}
	}
	
	public Dimension getPreferredSize() {
        return new Dimension(w,h);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);  
        setButtons();
        for (int i=0; i< buttonNum;i++){
        	add(tabs[i]);
        }
    }
    private void setButtons(){
    	int buttonW = w/buttonNum;
    	for (int i=0; i< buttonNum;i++){
    		final int j=i;
        	tabs[i] = new JButton();
        	tabs[i].setBounds(i*buttonW,0,buttonW,h);
        	tabs[i].setBorder(null);
        	tabs[i].setFocusable(false);
        	tabs[i].setOpaque(false);
        	tabs[i].setContentAreaFilled(false);
        	tabs[i].setBorderPainted(false);
        	tabs[i].addMouseListener(new MouseAdapter(){
        		@Override
   	         	public void mouseClicked(MouseEvent e) {
        			setIcon(null);
        			setIcon(images[j]);
        			buttonOp(j);
        		}
        	});
        }
    }
    
    abstract protected void buttonOp(int j);
}
