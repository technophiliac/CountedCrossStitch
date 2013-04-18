package com.lesliechapman.countedcrossstitch;

import java.util.ArrayList;

import zoomsupport.ZoomableRelativeLayout;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	
	private ScaleGestureDetector scaleGestureDetector;
	private ZoomableRelativeLayout _layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_layout = (ZoomableRelativeLayout)findViewById(R.id.layout);
		
		scaleGestureDetector = new ScaleGestureDetector(this,
		        new OnPinchListener(_layout));
		
		
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
	        int squareSize = 50;
	            		
	        
	        for (int x = 0; x < w; x++) {
	        	for (int y = 0; y < h; y++) {
	        		int color = bmp.getPixel(x, y);
	        		
	        		
	        		GradientDrawable gd = new GradientDrawable(Orientation.BOTTOM_TOP, new int[]{color, color});
	        		gd.setStroke(3, Color.BLACK);
	        		
	        		ImageView view1 = new ImageView(this);
	        		view1.setImageDrawable(gd);
	        		view1.setLayoutParams(new LayoutParams(squareSize, squareSize));
	        		view1.setX(x * squareSize);
	        		view1.setY(y * squareSize);
	        		_layout.addView(view1);
					
				}
				
			}        
	        
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	 // TODO Auto-generated method stub
	 scaleGestureDetector.onTouchEvent(event);
	 return true;
	}
	

}

class OnPinchListener extends SimpleOnScaleGestureListener {

    float startingSpan; 
    float endSpan;
    float startFocusX;
    float startFocusY;
    ZoomableRelativeLayout _layout;
    
    public OnPinchListener(ZoomableRelativeLayout layout){
    	_layout = layout;
    }


    public boolean onScaleBegin(ScaleGestureDetector detector) {
        startingSpan = detector.getCurrentSpan();
        startFocusX = detector.getFocusX();
        startFocusY = detector.getFocusY();
        return true;
    }


    public boolean onScale(ScaleGestureDetector detector) {
    	_layout.scale(detector.getCurrentSpan()/startingSpan, startFocusX, startFocusY);
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector detector) {
        //_layout.restore();
    }
}
