
public class StatusButton extends Button{

	public StatusButton(int x, int y, int w, int h, String imageUP,
			String imageP) {
		super(x, y, w, h, imageUP, imageP);
		
	}
	@Override
	public void press(){
		setIcon(pressed);
	}
	
	public void unpress(){
		setIcon(unpressed);
	}

}
