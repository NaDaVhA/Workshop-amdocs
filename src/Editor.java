import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;

import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.MarbleFilter;
import com.jhlabs.image.SolarizeFilter;


public class Editor {
	
public static BufferedImage brightness(BufferedImage img, int x){
		
		BufferedImage ret=new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);;
		
		if (x==0){
			Scalr.OP_DARKER.filter(img, ret);
		}
		else{
			Scalr.OP_BRIGHTER.filter(img, ret);
		}
		return ret;
		
	}

public static BufferedImage marble(BufferedImage img){
	
	BufferedImage ret;
	
	try{
		ret = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		MarbleFilter  filter = new MarbleFilter ();
		filter.filter(img, ret);
		return ret;
	}
	catch(Exception e){
		return null;
	}
	
	
}


public static BufferedImage solaris(BufferedImage img){
	
	BufferedImage ret;
	
	try{
		ret = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		SolarizeFilter filter = new SolarizeFilter();
		filter.filter(img, ret);
		return ret;
	}
	catch(Exception e){
		return null;
	}
	
	
}



public static BufferedImage invert(BufferedImage img){
	
	BufferedImage ret;
	
	try{
		ret = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		InvertFilter filter = new InvertFilter();
		filter.filter(img, ret);
		return ret;
	}
	catch(Exception e){
		return null;
	}
	
	
}


public static BufferedImage grayscale(BufferedImage img){
	
	BufferedImage ret;
	
	try{
		ret = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		GrayscaleFilter filter = new GrayscaleFilter();
		filter.filter(img, ret);
		return ret;
	}
	catch(Exception e){
		return null;
	}
	
	
}



/** 
 	rotation = CW_180 , CW_90, CW_270, FLIP_HORZ, FLIP_VERT 
**/
public static BufferedImage rotate(BufferedImage img, Scalr.Rotation rotation){
	
	BufferedImage ret;
	
	try{
		ret = Scalr.rotate(img, rotation);
	}
	catch(Exception e){
		return null;
	}
	
	return ret;
}

/**
	img - The image to crop.
	x - The x-coordinate of the top-left corner of the bounding box used for cropping.
	y - The y-coordinate of the top-left corner of the bounding box used for cropping.
	width - The width of the bounding cropping box.
	height - The height of the bounding cropping box.
**/
public static BufferedImage crop(BufferedImage img, int x, int y, int width, int height){
	
	BufferedImage ret;
	
	try{
		ret = Scalr.crop(img, x,y,width,height);
	}
	catch(Exception e){
		return null;
	}
	
	return ret;
}
}
