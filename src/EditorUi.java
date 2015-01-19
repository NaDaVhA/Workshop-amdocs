import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.imgscalr.Scalr;



public class EditorUi {
	private String path;
	private BufferedImage temp;
	private BufferedImage cur;
	private JLabel displayPane = new JLabel();
	private JLabel displayImg=new JLabel();
	private Button save;
	private Button facebook;
	private AdjustLabel adjustLabel = new AdjustLabel(21,77,170,501,"UI/adjpanel.png");
	private FilterLabel filterLabel =  new FilterLabel(21,77,170,501,"UI/filterspanel.png");
	private MyTab tab;
	private static CropRect cropRect = new CropRect(0,0,0,0,new Dimension(App.WIDTH,App.HEIGHT),new Color(184,222,227),5);
	private int imgX;
	private int imgY;
	
	
	
	public static void ma(String[] args) throws IOException{

		
	//	File img = new File("467.jpg");
		//BufferedImage buf = ImageIO.read(img);
		
		EditorUi edit = new EditorUi("DAC1.jpg");
		
		edit.edit();
		
		
	}
	
	public void edit(){
		JFrame f = new JFrame();
		JPanel p = getPanel();	
		p.add(cropRect);
		f.add(p);
		f.pack();
		setButtons();
		cropRect.setVisible(false);
		adjustLabel.setVisible(true);
		filterLabel.setVisible(false);
		f.setVisible(true);
	}
	
	
	public EditorUi(String path){
		this.path = path;
		File img = new File(path);
		BufferedImage buf;
		try {
			buf = ImageIO.read(img);
			cur = copy(buf);
			 temp= copy(buf);
			 
			 setComponents();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 
	}
	

	private void setComponents(){
		save = new Button(255,519,46,43,"UI/save.png","UI/saveP.png");
		facebook = new Button(690,519,46,43,"UI/facebook.png","UI/facebookP.png");
		displayPane.setBounds(272, 95, 450, 387);
		displayPane.add(displayImg,BorderLayout.CENTER);
		updateDisplayImg();
		String tabs[] = {"UI/adj.png", "UI/filters.png"};
		tab = new MyTab(13,42,191,32,tabs);
		
		
	}
	private void setButtons(){
		save.addMouseListener(new MouseAdapter(){
			@Override
	         public void mouseClicked(MouseEvent e) {		
				save.press();
				(new Thread(new Runnable(){
	 				 public void run() {
	 					String name = App.MEDIADIR + App.MEDNAME + App.currImgNum + "."+App.IMGFORMAT;
	 					File output = new File(name);
	 					try {
	 						ImageIO.write(cur, App.IMGFORMAT, output);
	 						temp = copy(cur);
	 						path = name;
	 						MainScreen.updatePics();
	 						App.currImgNum++;
	 						
	 					} catch (IOException e1) {}
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
	 					PostUI.displayPost(path, "facebook");
	 				 }
	 			})).start();
				

			}
		});
		
	}
	private void updateDisplayImg(){
		BufferedImage temp = Scalr.resize(cur, displayPane.getWidth(), displayPane.getHeight());
		int y = (int) ((displayPane.getHeight() - temp.getHeight())/2)   ;
		int x = (int) ((displayPane.getWidth() - temp.getWidth())/2)   ;
		imgX = x + 272;
		imgY = y + 95;
		displayImg.setBounds(x, y, temp.getWidth(), temp.getHeight());
		displayImg.setIcon(null);
		displayImg.setIcon(new ImageIcon(temp));
		
	}

public EditorPanel getPanel(){
	return new EditorPanel(0,0,App.WIDTH,App.HEIGHT,"UI/editor.png");
}
	
private class EditorPanel extends MyPanel{
	
	
	public EditorPanel(int x, int y, int w, int h, String back) {
		super(x, y, w, h, back);
		
	}

	@Override
	protected void addComponents() {
		
		//setButtons();
		
		add(save);
		add(facebook);
		add(displayPane);
		add(adjustLabel);
		add(tab);
		add(filterLabel);

		
		
	}
	
	
	
	
	
}

private class MyTab extends Tab{

	public MyTab(int x, int y, int w, int h, String[] tabs) {
		super(x, y, w, h, tabs);
	}

	@Override
	protected void buttonOp(int j) {
		if (j==0){
			adjustLabel.setVisible(true);
			filterLabel.setVisible(false);
			temp = copy(cur);
		}
		else{
			adjustLabel.setVisible(false);
			filterLabel.setVisible(true);
			temp = copy(cur);
		}
		
	}
	
}

private class FilterLabel extends JLabel{
	private int w;
	private int h;
	private int filterNum=4;
	private JButton buttons[] = new JButton[filterNum+1];
	private JLabel label;
	
	public FilterLabel(int x,int y,int w,int h,String back){
		this.w=w;
		this.h=h;
		setBounds(x,y,w,h);
		label = App.imgLabel(back, 0, 0);

		
	}
	public Dimension getPreferredSize() {
        return new Dimension(w,h);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);  
        add(label);
        setButtons();
        for (int i=0; i<=filterNum;i++){
    		add(buttons[i]);
        }
        
    }
    private void setButtons(){
    	for (int i=0; i<=filterNum;i++){
    		buttons[i] = new JButton();
    		buttons[i].setBorder(null);
    		buttons[i].setOpaque(false);
    		buttons[i].setContentAreaFilled(false);
    		buttons[i].setBorderPainted(false);
    		if (i==0){
    			buttons[i].setBounds(9,445,156,56);
    			buttons[i].addMouseListener(new MouseAdapter(){
    				@Override
    		         public void mouseClicked(MouseEvent e) {
    					cur = copy(temp);
    					updateDisplayImg();
    				}
    			});
    		}
    		else {
    			final int j=i;
    			buttons[i].setBounds(9 ,9+ (i-1)*109,156,108);
    			buttons[i].addMouseListener(new MouseAdapter(){
    				@Override
    		         public void mouseClicked(MouseEvent e) {
    					if (j==1)cur = Editor.marble(temp);
    					else if (j==2)cur = Editor.invert(temp);
    					else if (j==3)cur = Editor.grayscale(temp);
    					else if (j==4)cur = Editor.solaris(temp);
    					updateDisplayImg();
    				}
    			});
    		}
    	}
    }
}

public static BufferedImage copy(BufferedImage img){
	ColorModel cm = img.getColorModel();
	 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	 WritableRaster raster = img.copyData(null);
	 BufferedImage ret= new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	 return ret;
}

private class AdjustLabel extends JLabel{
	static final int rotateOpNum=4;
	private Button rotateButtons[] = new Button[rotateOpNum];
	private StatusButton crop = new StatusButton(13,133,27,25,"UI/crop.png","UI/cropP.png");
	private Button ok = new Button(49,133,27,25,"UI/ok.png", "UI/okP.png");
	private Button minus = new Button(14,209,15,14,"UI/minus.png", "UI/minusP.png");
	private Button plus = new Button(35,209,15,14,"UI/plus.png", "UI/plusP.png");
	private JLabel circle = App.imgLabel("UI/circle.png",73,230);
	boolean cropOn=false; private boolean cropStart=false;
	int brigthnessPos = 0;
	final  int  MaxB=5;
	final  int MinB=-5;
	
	public AdjustLabel(int x, int y, int w, int h, String back) {
		File in = new File(back);
		try {
			BufferedImage img = ImageIO.read(in);
			setBounds(x,y,w,h);
			setIcon(new ImageIcon(img));
			addComponents();
			ok.setVisible(false);
	
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	
	protected void addComponents() {
		
		for (int i=0; i<rotateOpNum;i++){
			rotateButtons[i] = new Button(13 + i*36,56,27,25, "UI/rotate" + i + ".png","UI/rotate" + i + "P.png");
		}
		
		setButtons();
		addButtons();
	}
	
	private void updateCircle(int i){
		int x = circle.getX() + i;
		int y = circle.getY();
		int w = circle.getWidth();
		int h = circle.getHeight();
		circle.setBounds(x,y,w,h);
	}
	
	private void setButtons(){
		for (int i=0; i< rotateOpNum; i++){
			final int j = i;
			rotateButtons[i].addMouseListener(new MouseAdapter(){				
		 		@Override
		         public void mouseClicked(MouseEvent e) {
		 					
		 			rotateButtons[j].press();
		 			if (j==0)cur = Editor.rotate(cur, Scalr.Rotation.CW_270);
		 			else if (j==1)cur =Editor.rotate(cur, Scalr.Rotation.CW_90);
		 			else if (j==2)cur =Editor.rotate(cur, Scalr.Rotation.FLIP_HORZ);
		 			else if (j==3)cur =Editor.rotate(cur, Scalr.Rotation.FLIP_VERT);
		 			
		 			updateDisplayImg();
		         }
		 	});
		}
		
		plus.addMouseListener(new MouseAdapter(){				
	 		@Override
	         public void mouseClicked(MouseEvent e) {
	 					
	 			if (brigthnessPos<MaxB){
	 				plus.press();
	 				cur = Editor.brightness(cur, 1);
	 				
	 				brigthnessPos++;
	 				updateDisplayImg();
	 				updateCircle(10);
	 			}
	         }
	 	});
		
		minus.addMouseListener(new MouseAdapter(){				
	 		@Override
	         public void mouseClicked(MouseEvent e) {
	 					
	 			if (brigthnessPos>MinB){
	 				minus.press();
	 				cur = Editor.brightness(cur, 0);
	 				
	 				brigthnessPos--;
	 				updateDisplayImg();
	 				updateCircle(-10);
	 			}
	         }
	 	});
		
		crop.addMouseListener(new MouseAdapter(){				
	 		@Override
	         public void mouseClicked(MouseEvent e) {
	 					
	 			crop.press();
	 			cropOn = true;
	 			ok.setVisible(true);
	 			//cropRect.setVisible(true);
	 			//cropRect.setDim(new Dimension(displayImg.getWidth(),displayImg.getHeight()));
	 			
	         }
	 	});
		
		ok.addMouseListener(new MouseAdapter(){				
	 		@Override
	         public void mouseClicked(MouseEvent e) {
	 			ok.press();
	 			int x = cropRect.getMyX();	
	 			int y = cropRect.getMyY();	
	 			int w = cropRect.getMyW();	
	 			int h = cropRect.getMyH();	
	 			
	 			
	 			if (w!=0 && h!=0){
	 				int newx = ((x-imgX)*cur.getWidth()/displayImg.getWidth());
		 			int newy = ((y-imgY)*cur.getHeight()/displayImg.getHeight());
		 			int neww = (w*cur.getWidth()/displayImg.getWidth());
		 			int newh = (h*cur.getHeight()/displayImg.getHeight());
	 				cur = Editor.crop(cur, newx, newy, neww, newh);
	 				
	 			}
	 			
	 			cropOn = false;
	 			ok.setVisible(false);
	 			cropRect.setVisible(false);
	 			cropRect.setMyX(0);
	 			cropRect.setMyY(0);
	 			cropRect.setMyW(0);
	 			cropRect.setMyH(0);
	 			crop.unpress();
	 			updateDisplayImg();
	 			
	         }
	 	});
		
		displayImg.addMouseListener(new MouseAdapter(){
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (cropOn){
					
					if (legalStart(e.getX()+imgX,e.getY()+imgY)){
				cropStart=true;
				cropRect.setMyX(e.getX()+imgX);
				cropRect.setMyY(e.getY()+imgY);
				cropRect.setMyW(0);
				cropRect.setMyH(0);
				cropRect.setVisible(true);
				cropRect.repaint();
				}
				}
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (cropOn){
					if (legalEnd(e.getX()+imgX,e.getY()+imgY)){
				cropStart=false;
				cropRect.setMyW(Math.abs(e.getX()+imgX-cropRect.getMyX()));
				cropRect.setMyH(Math.abs(e.getY()+imgY-cropRect.getMyY()));
				cropRect.repaint();
				}
				}
				
			}
	 	});
		
displayImg.addMouseMotionListener(new MouseMotionAdapter(){

			@Override
			public void mouseDragged(MouseEvent e) {
				if (cropStart){
					if (legalEnd(e.getX()+imgX,e.getY()+imgY)){
					cropRect.setMyW(Math.abs(e.getX()+imgX-cropRect.getMyX()));
					cropRect.setMyH(Math.abs(e.getY()+imgY+-cropRect.getMyY()));
				cropRect.repaint();
					}
				}
				
			}
	 	});
		
		
	}
	private void addButtons(){
		for (int i=0; i< rotateOpNum; i++){
			add(rotateButtons[i]);
		}
		add(crop);
		add(ok);
		add(plus);
		add(minus);
		add(circle);
	}
}

private boolean legalStart(int x, int y){
	if (x>= imgX && x<= imgX + displayImg.getWidth() && y>= imgY && y<= imgY + displayImg.getHeight() ){
		return true;
	}
	return false;
}
private boolean legalEnd(int x, int y){
	
	if (x>= cropRect.getMyX() && y>=cropRect.getMyY()){
	if (x>= imgX && x<= imgX + displayImg.getWidth() && y>= imgY && y<= imgY + displayImg.getHeight() ){
		return true;
	}
	}
	return false;
}
	

}
