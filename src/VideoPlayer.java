import javax.swing.JFrame;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;


public class VideoPlayer {
private JFrame player=new JFrame();
	
	private EmbeddedMediaPlayerComponent ourMediaPlayer;
	
	private String mediaPath="";
	
	public VideoPlayer(String mediaURL){
		this.mediaPath=mediaURL;
		//need to configure this at ariella's computer and download vlcLAN with 32/64 bit 
		NativeLibrary.addSearchPath(uk.co.caprica.vlcj.runtime.RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
		ourMediaPlayer=new EmbeddedMediaPlayerComponent();
		
		
		
		//shirly you can change here..
		player.setContentPane(ourMediaPlayer);
		player.setSize(800, 600);
		player.setVisible(true);
		//player.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void playVideo(){
		ourMediaPlayer.getMediaPlayer().playMedia(mediaPath);
		//shirly i this you can call play/pause with the following functions
		//ourMediaPlayer.getMediaPlayer().play();
		//ourMediaPlayer.getMediaPlayer().pause();
	}
}
