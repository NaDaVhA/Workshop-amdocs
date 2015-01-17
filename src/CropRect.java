
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CropRect extends JLabel {
	
	private int x;
	private int y;
	private int w;
	private int h;
	private Dimension dim;
	private Color color;
	private int thikness;
	
	public CropRect(int x,int y, int w, int h , Dimension dim,Color color,int thikness ){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.dim = dim;		
		this.color = color;
		this.thikness = thikness;
	}
	
	public void setMyX(int x){
		this.x = x;
	}
	public void setMyY(int y){
		this.y = y;
	}
	public void setMyW(int w){
		this.w = w;
	}
	public void setMyH(int h){
		this.h = h;
	}
	public int getMyX(){
		return x;
	}
	public int getMyY(){
		return y;
	}
	public int getMyW(){
		return w;
	}
	public int getMyH(){
		return h;
	}
	public void setDim(Dimension dim){
		this.dim = dim;
	}
	public void setColor(Color color){
		this.color = color;
	}
	public void setThik(int t){
		this.thikness = t;
	}
	
	
	 public Dimension getPreferredSize() {
         return dim;
     }
	@Override
  public void paintComponent(Graphics g) {
	  super.paintComponent(g);
	  Graphics2D g2 = (Graphics2D) g;
	  g2.setStroke(new BasicStroke(thikness));
	  g2.setColor(color);
	  g2.drawRect (x, y, w,h);  
    
  }
}
