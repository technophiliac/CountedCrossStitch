package com.lesliechapman.countedcrossstitch;

import java.util.ArrayList;

import com.lesliechapman.countedcrossstitch.util.ColorUtils;
import com.lesliechapman.countedcrossstitch.zoomsupport.TouchImageView;
import com.lesliechapman.countedcrossstitch.zoomsupport.ZoomableRelativeLayout;


import android.R.layout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Bitmap newBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		drawCanvas();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.show_palette:
	        	launchPalette();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void launchPalette(){
		Intent intent = new Intent(this, PaletteActivity.class);
		startActivity(intent);
	}

	
	@SuppressLint("NewApi")
	private void drawCanvas(){
		
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inDither = false;
			opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.gerritt, opt);	
			
			
			
	        int w = bmp.getWidth();
	        int h = bmp.getHeight();
	        int squareSize = 10;
	         
	        newBitmap = Bitmap.createBitmap(w*squareSize, 
				    h*squareSize, Bitmap.Config.ARGB_8888);
	        
	        for (int x = 0; x < w; x++) {
	        	for (int y = 0; y < h; y++) {
	        		int color = bmp.getPixel(x, y);	        		
	        		for (int i = 0; i < squareSize; i++){	        			
	        			for (int j = 0; j <squareSize; j++){	        		
	        				newBitmap.setPixel((x * squareSize)+i, (y * squareSize)+j, color);
	        			}
	        		}					
				}				
			}              
            
            TouchImageView touch = (TouchImageView)findViewById(R.id.imageView1);//new TouchImageView(this);
            touch.setImageBitmap(newBitmap);
            touch.setMaxZoom(5f); //change the max level of zoom, default is 3f
            //setContentView(touch);
	        
	}
	
	public void setCurrentColor(float x, float y){
		
		int color = newBitmap.getPixel(Math.round(x), Math.round(y));
		
		//((TextView)findViewById(R.id.textView1)).setText(ColorUtils.getDMCColor(color));
	}
    
}
