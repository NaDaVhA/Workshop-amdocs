import java.util.Observable;

public class AccidentSensor extends Observable{
	
	public void carCrached(){
		setChanged();
		notifyObservers();
	}
}
