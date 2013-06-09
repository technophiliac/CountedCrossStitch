package com.lesliechapman.countedcrossstitch;

import com.lesliechapman.countedcrossstitch.util.ColorUtils;
import com.lesliechapman.countedcrossstitch.zoomsupport.TouchImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private final static int COLOR_OUTLINE_REQUEST = 1;

	Bitmap newBitmap;
	TextView txtView;
	int previousColor = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);		

		drawCanvas();
		
		txtView = ((TextView)findViewById(R.id.textView1));

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

	private void launchPalette() {
		Intent intent = new Intent(this, PaletteActivity.class);
		startActivityForResult(intent, COLOR_OUTLINE_REQUEST);
	}

	private void drawCanvas() {

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bmp = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.gerritt, opt);

		int w = bmp.getWidth();
		int h = bmp.getHeight();
		int squareSize = 10;

		newBitmap = Bitmap.createBitmap((w * squareSize) , (h * squareSize),
				Bitmap.Config.ARGB_8888);
		
		Bitmap grid = Bitmap.createBitmap((w * squareSize) , (h * squareSize),
				Bitmap.Config.ARGB_8888);

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int color = bmp.getPixel(x, y);
				for (int i = 0; i < squareSize; i++) {
					for (int j = 0; j < squareSize; j++) {
						if(i == 0 || j == 0){
							grid.setPixel((x * squareSize) + i,
										(y * squareSize) + j, Color.YELLOW);
							
						} else {
							newBitmap.setPixel((x * squareSize) + i,
									(y * squareSize) + j, color);
						}
					}
				}
			}
		}

		TouchImageView touch = (TouchImageView) findViewById(R.id.imageView1);
		touch.setImageBitmap(newBitmap);
		touch.setMaxZoom(10f);

	}
	
	private void outlineColor(int color){
		txtView.setText(ColorUtils.getDMCColor(color));
		
		int w = newBitmap.getWidth();
		int h = newBitmap.getHeight();
		
		if (previousColor != -1){
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					if (newBitmap.getPixel(x, y) == Color.RED){
						newBitmap.setPixel(x, y, previousColor);
					}
				}
			}
			previousColor = -1;
		}
		
		if (color == -1){
			return;
		}
		
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (newBitmap.getPixel(x, y) == color){
					if(x== 0 || y==0 || x == w-1 || y == h-1){
						newBitmap.setPixel(x, y, Color.RED);
					} else if (newBitmap.getPixel(x+1, y) != color && newBitmap.getPixel(x+1, y) != Color.RED){
						newBitmap.setPixel(x, y, Color.RED);
					} else if (newBitmap.getPixel(x-1, y) != color && newBitmap.getPixel(x-1, y) != Color.RED){
						newBitmap.setPixel(x, y, Color.RED);
					} else if(newBitmap.getPixel(x, y+1) != color && newBitmap.getPixel(x, y+1) != Color.RED){
						newBitmap.setPixel(x, y, Color.RED);
					} else if(newBitmap.getPixel(x, y-1) != color && newBitmap.getPixel(x, y-1) != Color.RED){
						newBitmap.setPixel(x, y, Color.RED);
					}
					
					previousColor = color;
					
					
					//the following will change the selected color to red
					/*if (x > 0 && (!(newBitmap.getPixel(x-1, y) == color))){
						newBitmap.setPixel(x, y, Color.RED);
					}
					else if (x < w-1 && (!(newBitmap.getPixel(x+1, y) == color))){
						newBitmap.setPixel(x, y, Color.RED);
					}
					else if (y > 0 && (!(newBitmap.getPixel(x, y-1) == color))){
						newBitmap.setPixel(x, y, Color.RED);
					}
					else if (y < h-1 && (!(newBitmap.getPixel(x, y+1) == color))){
						newBitmap.setPixel(x, y, Color.RED);
					}*/
				}
			}
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case COLOR_OUTLINE_REQUEST:
			if (resultCode == Activity.RESULT_OK){
				int color = data.getIntExtra(PaletteActivity.COLOR_REQUEST, -1);
				outlineColor(color);
			}
			
			break;

		default:
			break;
		}
	}

}
