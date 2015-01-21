import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


public class PostUI {
	
	private final static String FACEBOOK = "facebook";
	private final static String YOUTUBE = "youtube";
	private final static int FONTSIZE = 18;
	private final static String FONTSTYLE = "Ariel";
	private final static int PANEW = 450;
	private final static int PANEH = 170;
	
	public static void displayPost(final String path, final String network){
		final JFrame f = new JFrame();
		final JTextArea text = new JTextArea();
    	text.setFont(new Font(FONTSTYLE,Font.PLAIN,FONTSIZE));
    	final JScrollPane pane = new JScrollPane(text);
    	
    	text.setLineWrap(true);
    	
    	pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    	pane.setPreferredSize(new Dimension(PANEW,PANEH));
    	MesPanel temp=null; 
    	
    	if (network.equals(FACEBOOK))temp=getFacebookPanel();
    	else if (network.equals(YOUTUBE))temp=getYoutubePanel();
    	final MesPanel p=temp;
    	
    	p.getButtons()[0].addMouseListener(new MouseAdapter(){
    		@Override
	         public void mouseClicked(MouseEvent e) {
    			p.getButtons()[0].press();
    			(new Thread(new Runnable(){
		 				 public void run() {
		 					share(path,network,text.getText());
		 				 }
		 			})).start();
    			
    			new java.util.Timer().schedule( 
				        new java.util.TimerTask() {
				            @Override
				            public void run() {  
				            	f.dispose();
				            }
				        }, 
				        700);			
    	               
    		}
    	});
    	
    	
    	p.add(pane);
    	f.add(p);
    	f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	f.pack();f.setVisible(true);
	}
	
	private static MesPanel getFacebookPanel(){
		Button[] buttons = new Button[1];
		buttons[0] = new Button(375,238,93,30,"UI/shareblue.png","UI/shareblueP.png");
		MesPanel ret = new MesPanel(0,0,500,300,"UI/sharef.png",buttons);
		return ret;
	}
	
	private static MesPanel getYoutubePanel(){
		Button[] buttons = new Button[1];
		buttons[0] = new Button(375,238,93,30,"UI/sharered.png","UI/shareredP.png");
		MesPanel ret = new MesPanel(0,0,500,300,"UI/shareu.png",buttons);
		return ret;
	}
	
	public static void share(String path, String network, String post){
		if (App.isImage(path)){
			//Ariela
			if (network.equals(FACEBOOK))FacebookUpload.sharePhotoOnFacebook(path, post);
	    	
		}
		else{
			if (network.equals(FACEBOOK))
				try {
					
					//Ariela
					FacebookUpload.UploadVideoToFacebook(path, post);

				} catch (IOException e) {
					e.printStackTrace();
				}
			else if (network.equals(YOUTUBE)){
				String title = null;
				String content=null;
				String[] str;
				if (post!=null){
					str = post.split("\n",2);
					title = str[0];
					if (str.length >=2)content=str[1];
				}
				
				//Ariela
				UploadVideo.shareVideoOnYoutube(path, title, content);
			}
		}
	}
	
	
	
}






