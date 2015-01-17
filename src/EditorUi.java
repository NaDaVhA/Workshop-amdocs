import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.imgscalr.Scalr;

import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.MarbleFilter;
import com.jhlabs.image.SolarizeFilter;


public class EditorUi {

	private BufferedImage original;
	private BufferedImage cur;
	private JLabel displayPane = new JLabel();
	private JLabel displayImg=new JLabel();
	private Button save;
	private Button facebook;
	private AdjustLabel adjustLabel = new AdjustLabel(21,77,170,501,"UI/adjpanel.png");
	
	
	
	public static void a(String[] args) throws IOException{
		
		File img = new File("467.jpg");
		BufferedImage buf = ImageIO.read(img);
		
		EditorUi edit = new EditorUi(buf);
		
		JFrame f = new JFrame();
		f.add(edit.getPanel());
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
		
		
	}
	
	
	public EditorUi(BufferedImage img){
		original = img;
		
		ColorModel cm = img.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = img.copyData(null);
		 cur= new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		 
		 setComponents();
		 
	}
	

	private void setComponents(){
		save = new Button(255,519,46,43,"UI/save.png","UI/saveP.png");
		facebook = new Button(690,519,46,43,"UI/facebook.png","UI/facebookP.png");
		displayPane.setBounds(272, 95, 450, 387);
		displayPane.add(displayImg,BorderLayout.CENTER);
		updateDisplayImg();
		
		
	}
	
	private void updateDisplayImg(){
		BufferedImage temp = Scalr.resize(cur, displayPane.getWidth(), displayPane.getHeight());
		int y = (int) ((displayPane.getHeight() - temp.getHeight())/2)   ;
		int x = (int) ((displayPane.getWidth() - temp.getWidth())/2)   ;
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
		
		setButtons();
		
		add(save);
		add(facebook);
		add(displayPane);
		add(adjustLabel);
		
		
	}
	
	private void setButtons(){
		
	}
	
	
	
}



private class AdjustLabel extends JLabel{
	static final int rotateOpNum=4;
	private Button rotateButtons[] = new Button[rotateOpNum];
	private StatusButton crop = new StatusButton(13,133,27,25,"UI/crop.png","UI/cropP.png");
	private Button ok = new Button(49,133,27,25,"UI/ok.png", "UI/okP.png");
	private Button minus = new Button(14,209,15,14,"UI/minus.png", "UI/minusP.png");
	private Button plus = new Button(35,209,15,14,"UI/plus.png", "UI/plusP.png");
	private JLabel circle = App.imgLabel("UI/circle.png",73,230);
	int cropOn=0;
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

	

}
