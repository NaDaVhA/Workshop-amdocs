

public abstract class Camera {
	
	/**
	 * 
	 * @param path - the path to save the pic
	 * @return 1 if succed
	 */
	abstract public int capturePic(String path);
	
	/**
	 * 
	 * @param path - the path to save the vid
	 * @return 1 if succed
	 */
	abstract public int captureVid(String path);
	
	
}
