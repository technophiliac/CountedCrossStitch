package com.lesliechapman.countedcrossstitch;

import java.util.ArrayList;

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
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

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
	        ArrayList<Integer> colors = new ArrayList<Integer>();
	        
	        for (int x = 0; x < w; x++) {
	        	for (int y = 0; y < h; y++) {
	        		int color = bmp.getPixel(x, y);
	        		
	        		
	        		GradientDrawable gd = new GradientDrawable(Orientation.BOTTOM_TOP, new int[]{color, color});
	        		gd.setStroke(3, Color.BLACK);
	        		
	        		ImageView view1 = new ImageView(this);
	        		view1.setImageDrawable(gd);
	        		view1.setLayoutParams(new LayoutParams(50, 50));
	        		RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);
	        		view1.setX(x * 50);
	        		view1.setY(y * 50);
	        		layout.addView(view1);
					
				}
				
			}
	}
	
	

}
