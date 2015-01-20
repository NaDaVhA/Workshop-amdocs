import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.RationalNumber;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.constants.TiffFieldTypeConstants;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldType;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputField;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;
import org.apache.sanselan.util.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WriteExifMetadata{
	
	private static String USERID =new String("ID12345");
	private static String CARID =new String("ID12345");
	private static String APIGOOGLEKEY="AIzaSyCYAR421GeEuVwbvkkfrX3HBY8iMjny-eQ";
    /**
     * add/update EXIF metadata in a JPEG file.
     * 
     * @param jpegImageFile
     *            A source image file.
     * @param dst
     *            The output file.
     * @throws IOException
     * @throws ImageReadException
     * @throws ImageWriteException
     */
    public static void updateExifMetadata(String path)
            throws IOException, ImageReadException, ImageWriteException{
    	File jpegImageFile = new File(path);
    	int index = path.indexOf(".jpg");
    	String outputpath = path.substring(0,index-1);
    	outputpath = outputpath + "wmd.jpeg";
    	File dst = new File(outputpath);
    	OutputStream os = null;
        Calendar cal = Calendar.getInstance();
        try{
            TiffOutputSet outputSet = null;
            IImageMetadata metadata = Sanselan.getMetadata(jpegImageFile);
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (jpegMetadata!=null){
                TiffImageMetadata exif = jpegMetadata.getExif();
                if (exif!=null){
                    /**use getOutputSet() to start with a "copy" of the fields read from the image.**/
                    outputSet = exif.getOutputSet();
                }
            }
 
           
            if (outputSet==null)
                outputSet = new TiffOutputSet();
            	TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
            	
            	/*update USERID*/
            	TiffOutputField userID = createEXIF(USERID, TiffConstants.EXIF_TAG_ARTIST, outputSet.byteOrder);
            	exifDirectory.removeField(TiffConstants.EXIF_TAG_ARTIST);
                exifDirectory.add(userID);

                 /*udapte CARID*/
                TiffOutputField carID = createEXIF(CARID, TiffConstants.EXIF_TAG_USER_COMMENT, outputSet.byteOrder);
            	exifDirectory.removeField(TiffConstants.EXIF_TAG_USER_COMMENT);
                exifDirectory.add(carID);
                
                /*udapte time*/
                TiffOutputField date = createEXIF(cal.getTime().toString(), TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL, outputSet.byteOrder);
            	exifDirectory.removeField(TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                exifDirectory.add(date);
                      
            	/*update location*/
                double longitude = -74.0; // 74 degrees W (in Degrees East)
                double latitude = 40 + 43 / 60.0; // 40 degrees N (in Degrees// North)
                outputSet.setGPSInDegrees(longitude, latitude);
                
     
                
            /*UPDAING FILE*/
            os = new FileOutputStream(dst);
            os = new BufferedOutputStream(os);
 
            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,
                    outputSet);
 
            os.close();
            jpegImageFile.delete();
            os = null;
        } finally{
            if (os != null)
                try
                {
                    os.close();
                } catch (IOException e){
 
                }
        }
    }
 

    public static TiffOutputField createEXIF(String data, TagInfo tag, int byteorder) throws ImageWriteException{
    	   FieldType fieldType = TiffFieldTypeConstants.FIELD_TYPE_ASCII;
           byte bytes[] = fieldType.writeData(data, byteorder);
           return new TiffOutputField(tag.tag, tag, fieldType, (data).length(), bytes);
    }

   
    
    
    
    
    
    
    
    
    
    
    public static String readExifMetadata(File jpegImageFile)throws IOException, ImageReadException, ImageWriteException, ParseException{
            IImageMetadata metadata = Sanselan.getMetadata(jpegImageFile);
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
       		String result;
       		
       		String lng = Double.toString(jpegMetadata.getExif().getGPS().getLongitudeAsDegreesEast());
       		String lat = Double.toString(jpegMetadata.getExif().getGPS().getLatitudeAsDegreesNorth());

       		String location = getAddressByGpsCoordinates(lng,lat);
       		result = " userID:"+ getTagValue(jpegMetadata,TiffConstants.EXIF_TAG_ARTIST) + 
       				 " carID:"+  getTagValue(jpegMetadata,TiffConstants.EXIF_TAG_USER_COMMENT) + 
       				 " Time:"+ getTagValue(jpegMetadata,TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL) + 
       				 " Location:"+location;
       		

       		
       		return result;
           
     
        }
    


	private static String getTagValue(final JpegImageMetadata jpegMetadata,final TagInfo tagInfo) {
	    final TiffField field = jpegMetadata.findEXIFValue(tagInfo);
	    if (field == null) {
	        return "Not Found";
	    } 
	    else {  return field.getValueDescription();
	    }
	}
	
	
	
	
	
	
	

	 
	/**
	 *
	 * @param lng
	 * @param lat
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
		private static String getAddressByGpsCoordinates( String lng,  String lat) throws IOException, ParseException
	           {
		  
			String adressUrl="https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&key="+APIGOOGLEKEY;
	        URL url = new URL(adressUrl);
	        
	        
	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	        String formattedAddress = "";
	 
	      
	            InputStream in = url.openStream();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	            String result, line = reader.readLine();
	            result = line;
	            while ((line = reader.readLine()) != null) {
	                result += line;
	            }
	 
	            JSONParser parser = new JSONParser();
	            JSONObject rsp = (JSONObject) parser.parse(result);
	 
	            if (rsp.containsKey("results")) {
	                JSONArray matches = (JSONArray) rsp.get("results");
	                JSONObject data = (JSONObject) matches.get(0); 
	                formattedAddress = (String) data.get("formatted_address");
	            }
	            urlConnection.disconnect();
	            return formattedAddress;
	        
	    }
  
}
