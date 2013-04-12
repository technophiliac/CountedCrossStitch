package com.lesliechapman.countedcrossstitch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		zoom();
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
	private void zoom(){
		ImageView view = (ImageView)findViewById(R.id.imageView1);
		view.setScaleX(20);
		view.setScaleY(20);
		
		//Matrix matrix = new Matrix();
		
		//view.setImageMatrix(matrix);
	}
	
	

}
