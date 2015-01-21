import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.imgscalr.Scalr;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;


public class VideoPlayer {
private JFrame player=new JFrame();
private JLabel label;
private EmbeddedMediaPlayerComponent ourMediaPlayer;
private String mediaPath="";
private boolean paused=false;
	
	public VideoPlayer(String mediaURL){
		this.mediaPath=mediaURL;
		//need to configure this at ariella's computer and download vlcLAN with 32/64 bit 
		NativeLibrary.addSearchPath(uk.co.caprica.vlcj.runtime.RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
		ourMediaPlayer=new EmbeddedMediaPlayerComponent();
		setLabel();

		player.add(ourMediaPlayer);
		player.setSize(App.WIDTH, App.HEIGHT);
		player.add(label,BorderLayout.SOUTH);

		player.setVisible(true);
		player.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	public void playVideo(){
		ourMediaPlayer.getMediaPlayer().playMedia(mediaPath);
		
	}

	
private void networkPressed(final String network){
	(new Thread(new Runnable(){
			 public void run() {
				 final MesPanel p = App.getSafeSharePanel();
				final JFrame f = new JFrame();
				 p.getButtons()[0].addMouseListener(new MouseAdapter(){
					@Override
	                public void mouseClicked(MouseEvent e) {
						p.getButtons()[0].press();
						f.dispose();
						
	 					(new Thread(new Runnable(){
			 				 public void run() {
			 					PostUI.displayPost(mediaPath, network);
			 				 }
			 			})).start();
					}
				 });
				p.getButtons()[1].addMouseListener(new MouseAdapter(){
					@Override
	                public void mouseClicked(MouseEvent e) {
						p.getButtons()[0].press();
						f.dispose();
	 					(new Thread(new Runnable(){
			 				 public void run() {
			 					PostUI.share(mediaPath, network,null);
			 				 }
			 			})).start();
					}
				 });
				 f.add(p);
				 f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				 f.pack();
				 f.setVisible(true);
				 
				 
				 
			 }
		})).start();
}

private void setLabel(){
    	
    	label = App.imgLabel("UI/videoRecBack.png", 0, 550);
    	final Button facebook =new Button(7,4,46,43,"UI/facebook.png","UI/facebookP.png");
    	final Button youtube =new Button(66,4,46,43,"UI/youtube.png","UI/youtubeP.png");
    	final StatusButton pause =new StatusButton(379,7,42,38,"UI/pauseUP.png","UI/play.png");
    	

    	label.add(facebook);
    	label.add(youtube);
    	label.add(pause);
    	
    	facebook.addMouseListener(new MouseAdapter(){
    		@Override
            public void mouseClicked(MouseEvent e) {
    			facebook.press();
    			networkPressed("facebook");
    		}
    	});
    	youtube.addMouseListener(new MouseAdapter(){
    		@Override
            public void mouseClicked(MouseEvent e) {
    			youtube.press();
    			networkPressed("youtube");
    		}
    	});
    	
    	pause.addMouseListener(new MouseAdapter(){
    		@Override
            public void mouseClicked(MouseEvent e) {
    			if (paused){
    				pause.unpress();
    				ourMediaPlayer.getMediaPlayer().play();
    				paused=false;
    			}
    			else{
    				pause.press();
    				ourMediaPlayer.getMediaPlayer().pause();
    				paused=true;
    			}
    		}
    	});
    
    	

    }
}
