package com.lesliechapman.countedcrossstitch.util;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.lesliechapman.countedcrossstitch.R;

public class ColorUtils {
	
	Activity _activity;
	
	public ColorUtils(Activity a){
		_activity = a;
	}
	
	public ArrayList<Integer> getImageColors() {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bmp = BitmapFactory.decodeResource(_activity.getResources(), R.drawable.gerritt, opt);		
		
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        
        for (int x = 0; x < w; x++) {
        	for (int y = 0; y < h; y++) {
        		int color = bmp.getPixel(x, y);
        		
        		int R = Color.red(color);
        		int G = Color.green(color);
        		int B = Color.blue(color);
        		int A = Color.alpha(color);
        		
				System.out.println("The color at [" + x + "," + y + " is " + ":" + R + ":" + G + ":" + B + "(" + A + ")");
				if (!colors.contains((Integer)color) && A!=0){
					colors.add((Integer)color);
				}
				
			}
			
		}
        System.out.println("Total number of colors in the image = " + colors.size());
        return colors;
	}
	
	public static String getHexColor(int color){
		StringBuilder s = new StringBuilder();
		
		String red = Integer.toHexString(Color.red(color));
		if (red.length() == 1){
			red = "0" + red;
		}		
        s.append(red);
        
        String green = Integer.toHexString(Color.green(color));
        if(green.length() == 1){
        	green = "0" + green;
        }
        s.append(green);
        
        String blue = Integer.toHexString(Color.blue(color));
        if (blue.length() == 1){
        	blue = "0" + blue;
        }
        s.append(blue);
        
        return s.toString();
	}
	
	public static String getDMCColor(int color){
		String hexColor = getHexColor(color);
		
		if (hexColor.equalsIgnoreCase("FFFFFF")){
			return "blanc";
		}
		if (hexColor.equalsIgnoreCase("FFE6E4")){
			return "ecru";
		}
		if (hexColor.equalsIgnoreCase("D9a899")){
			return "543";
		}
		if (hexColor.equalsIgnoreCase("aa7c6f")){
			return "3864";
		}
		if (hexColor.equalsIgnoreCase("825d56")){
			return "3790";
		}
		if (hexColor.equalsIgnoreCase("5d3c38")){
			return "839";
		}
		if (hexColor.equalsIgnoreCase("382322")){
			return "838";
		}
		if (hexColor.equalsIgnoreCase("1a0d10")){
			return "3371";
		}
		if (hexColor.equalsIgnoreCase("000000")){
			return "310(black)";
		}
		if (hexColor.equalsIgnoreCase("0079c1")){
			return "blue";
		}
		
		return "No DMC color for " + hexColor;
	}

}
