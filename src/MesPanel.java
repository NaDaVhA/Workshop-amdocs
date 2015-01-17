import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


public class MesPanel extends MyPanel{
	
	private Button[] buttons;
	
	public MesPanel(int x, int y, int w, int h, String back,Button[] buttons) {
		super(x, y, w, h, back);
		this.buttons = buttons;
		
	}
	@Override
	protected void addComponents(){
		for (int i=0; i< buttons.length ; i++){
			add(buttons[i]);
		}
		
	};
	
	public Button[] getButtons(){
		return buttons;
	}

}
