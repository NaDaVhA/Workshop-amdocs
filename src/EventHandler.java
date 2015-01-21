import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;


public class EventHandler{
	final private static String SECURITYDIR = "securityFootage/"; 
	final private static String APPDIR = "appFootage/";
	private static AccidentSensor accidentSensor = new AccidentSensor();
	private static ViewSensor viewSensor = new ViewSensor();
	private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh mm ss");
	final private static int secondsToRun = 5;
	private static int sleepSec=20;
	
public static void simulateEvents(){
	try {
		Thread.sleep(1000*sleepSec);
	} catch (InterruptedException e1) {

	}
	while (!App.terminated ){
		try {
			
			Random r = new Random();
			int i = r.nextInt(2);
			injectEvent(i);
			System.out.println("Event "+ i+ "\n");
			Thread.sleep(1000*sleepSec);
		} catch (InterruptedException e) {

		}
	}
	((WCamera)App.CAMERAS[0]).getCam().close();
}

private static void injectEvent(int x){
	if (x==0){
		accidentSensor.carCrached();
	}
	else if (x==1){
		if (App.CAMNUM>1){
		Random r = new Random();
		int i = r.nextInt(2);
		viewSensor.greatView(i);
		}
		else{
			viewSensor.greatView(0);
		}
	}
}

public static void setEventsListeners(){
	EventObserver observer= new EventHandler().new EventObserver();
	accidentSensor.addObserver(observer);
	viewSensor.addObserver(observer);
}

	
private class EventObserver implements Observer{
	public void update(Observable o, Object arg) {
		if (o == accidentSensor){
			Date date = new Date();
			final String name = dateFormat.format(date) + ".mp4";
			(new Thread(new Runnable(){
	 				 public void run() {
	 					((WCamera)App.CAMERAS[0]).captureLimitVid(SECURITYDIR +"cam1-"+ name, secondsToRun);
	 				 }
	 			})).start();
			(new Thread(new Runnable(){
				 public void run() {
					if (App.CAMNUM >1)App.CAMERAS[1].captureVid(SECURITYDIR+"cam2-" + name);
				 }
			})).start();
			
		}
		else if (o == viewSensor){
			Date date = new Date();
			final String name = dateFormat.format(date) + "."+App.IMGFORMAT;
			App.CAMERAS[viewSensor.getCamNum()].capturePic(APPDIR +name);
		}
	}
}
	
}
