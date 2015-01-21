import java.util.Observable;

public class ViewSensor extends Observable{
	private int camNum=0;
	
	public void greatView(int cam){
		camNum = cam;
		setChanged();
		notifyObservers();
	}
	public int getCamNum(){
		return camNum;
	}
}
