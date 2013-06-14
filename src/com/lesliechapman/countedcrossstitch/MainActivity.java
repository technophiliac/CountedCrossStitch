package com.lesliechapman.countedcrossstitch;

import java.io.File;
import java.util.Map;
import java.util.StringTokenizer;

import com.lesliechapman.countedcrossstitch.util.ColorUtils;
import com.lesliechapman.countedcrossstitch.zoomsupport.TouchImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {

	private final static String PROGRESS = "progressMap";
	private final static int COLOR_OUTLINE_REQUEST = 1;
	public final static int LOAD_IMAGE_REQUEST = 2;
	public static int SQUARE_SIZE = 10;
	public static int MAX_W = 100;
	public static int MAX_H = 100;
	public static int MARK_COLOR = Color.MAGENTA;

	Bitmap pattern;
	Bitmap done;
	TextView txtView;
	TouchImageView touch;
	int previousColor = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		txtView = ((TextView) findViewById(R.id.textView1));
		touch = (TouchImageView) findViewById(R.id.imageView1);		

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
		case R.id.load_image:
			launchLoadImage();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void launchPalette() {
		Intent intent = new Intent(this, PaletteActivity.class);
		startActivityForResult(intent, COLOR_OUTLINE_REQUEST);
	}
	
	private void launchLoadImage() {
		Intent intent = new Intent(this, LoadImageActivity.class);
		startActivityForResult(intent, LOAD_IMAGE_REQUEST);
	}
	
	private void drawCanvas(){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		drawCanvas(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.gerritt, opt));
	}

	private void drawCanvas(Bitmap bmp) {	
		
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		pattern = Bitmap.createBitmap((w * SQUARE_SIZE), (h * SQUARE_SIZE),
				Bitmap.Config.ARGB_8888);
		
		done = Bitmap.createBitmap((w * SQUARE_SIZE), (h * SQUARE_SIZE),
				Bitmap.Config.ARGB_8888);

		Bitmap grid = Bitmap.createBitmap((w * SQUARE_SIZE), (h * SQUARE_SIZE),
				Bitmap.Config.ARGB_8888);

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int color = bmp.getPixel(x, y);
				for (int i = 0; i < SQUARE_SIZE; i++) {
					for (int j = 0; j < SQUARE_SIZE; j++) {
						if (i == 0 || j == 0) {
							grid.setPixel((x * SQUARE_SIZE) + i,
									(y * SQUARE_SIZE) + j, Color.YELLOW);

						} else {
							pattern.setPixel((x * SQUARE_SIZE) + i,
									(y * SQUARE_SIZE) + j, color);
						}
					}
				}
			}
		}
		
		// Restore progress
		SharedPreferences settings = getSharedPreferences(PROGRESS, 0);
		
		Map<String, Boolean> map = (Map<String, Boolean>) settings.getAll();
		for (Map.Entry<String, Boolean> entry : map.entrySet()) {
		    String str = entry.getKey();
			StringTokenizer st2 = new StringTokenizer(str, "-");
			if(st2.hasMoreElements()){
				int x = Integer.parseInt(st2.nextElement().toString());
				if(st2.hasMoreElements()){
					int y = Integer.parseInt(st2.nextElement().toString());
					if(entry.getValue()){
						colorSquareAt(x, y, false);
					}
				}
			}			 
			
		}

		touch.setImageBitmap(pattern);
		touch.setMaxZoom(10f);
		touch.setOnClickListener(handleSquareClick);
	}

	private void colorSquareAt(float origX, float origY, boolean saveProgress)
	{
		String sx = Integer.toString(Math.round(origX));
		String sy = Integer.toString(Math.round(origY));
		SharedPreferences settings = getSharedPreferences(PROGRESS, 0);
		boolean isAlreadySelected = false;
		int color = MARK_COLOR;
		
		if(saveProgress){
			sx = sx.substring(0, sx.length()-1);
			sy = sy.substring(0, sy.length()-1);
			isAlreadySelected = settings.getBoolean(sx + "-" + sy, false);
		}
		
		int startX = Integer.parseInt(sx + "1");
		int startY = Integer.parseInt(sy + "1");
		
		if(isAlreadySelected){
			color = pattern.getPixel(startX + 2, startY);
		}
		
		for(int i=0; i<SQUARE_SIZE-1; i++){
			int x = startX + i;
			int y = startY + i;
			colorPixel(x, y, color);
		}
		
		for(int i=0; i<SQUARE_SIZE-1; i++){
			int x = startX + i;
			int y = startY + SQUARE_SIZE - i - 2;
			colorPixel(x, y, color);
		}
		
		if(saveProgress){			
		    SharedPreferences.Editor editor = settings.edit();
		    editor.putBoolean(sx + "-" + sy, !isAlreadySelected);
		    editor.commit();
		}		
		
		//This will fill in an entire square with a solid color
		/*for(int x = startX; x<startX+SQUARE_SIZE-1; x++ ){
			for(int y = startY; y<startY+SQUARE_SIZE-1; y++ ){
				if(x>=0 && y>=0 && x<pattern.getWidth() && y<pattern.getHeight())
					pattern.setPixel(x, y, MARK_COLOR);				
			}			
		}*/
	}
	
	private void colorPixel(int x, int y, int c){
		if(x>=0 && y>=0 && x<pattern.getWidth() && y<pattern.getHeight()){
			pattern.setPixel(x, y, c);
		}
	}

	View.OnClickListener handleSquareClick = new View.OnClickListener() {
		@SuppressLint("NewApi")
		public void onClick(View v) {

			float[] values = new float[9];
			touch.getMatrix().getValues(values);
			float x_coord = ((TouchImageView.start.x - values[2])*touch.getScale() )/values[0];
			float y_coord= ((TouchImageView.start.y - values[5])*touch.getScale() )/ values[4];

			colorSquareAt(x_coord/touch.getScale(), y_coord/touch.getScale(), true);

		}
	};

	private void outlineColor(int color) {
		txtView.setText(ColorUtils.getDMCColor(color));

		int w = pattern.getWidth();
		int h = pattern.getHeight();

		if (previousColor != -1) {
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					if (pattern.getPixel(x, y) == Color.RED) {
						pattern.setPixel(x, y, previousColor);
					}
				}
			}
			previousColor = -1;
		}

		if (color == -1) {
			return;
		}

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (pattern.getPixel(x, y) == color) {
					if (x == 0 || y == 0 || x == w - 1 || y == h - 1) {
						pattern.setPixel(x, y, Color.RED);
					} else if (pattern.getPixel(x + 1, y) != color
							&& pattern.getPixel(x + 1, y) != Color.RED) {
						pattern.setPixel(x, y, Color.RED);
					} else if (pattern.getPixel(x - 1, y) != color
							&& pattern.getPixel(x - 1, y) != Color.RED) {
						pattern.setPixel(x, y, Color.RED);
					} else if (pattern.getPixel(x, y + 1) != color
							&& pattern.getPixel(x, y + 1) != Color.RED) {
						pattern.setPixel(x, y, Color.RED);
					} else if (pattern.getPixel(x, y - 1) != color
							&& pattern.getPixel(x, y - 1) != Color.RED) {
						pattern.setPixel(x, y, Color.RED);
					}

					previousColor = color;

					// the following will change the selected color to red
					/*
					 * if (x > 0 && (!(newBitmap.getPixel(x-1, y) == color))){
					 * newBitmap.setPixel(x, y, Color.RED); } else if (x < w-1
					 * && (!(newBitmap.getPixel(x+1, y) == color))){
					 * newBitmap.setPixel(x, y, Color.RED); } else if (y > 0 &&
					 * (!(newBitmap.getPixel(x, y-1) == color))){
					 * newBitmap.setPixel(x, y, Color.RED); } else if (y < h-1
					 * && (!(newBitmap.getPixel(x, y+1) == color))){
					 * newBitmap.setPixel(x, y, Color.RED); }
					 */
				}
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case COLOR_OUTLINE_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				int color = data.getIntExtra(PaletteActivity.COLOR_RESULT, -1);
				outlineColor(color);
			}
			break;
		case LOAD_IMAGE_REQUEST:
			if (resultCode == RESULT_OK && null != data) {
				//Bitmap bitmap = (Bitmap) data.getParcelableExtra(LoadImageActivity.LOAD_IMAGE_RESULT);
				Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                        + File.separator + data.getStringExtra(LoadImageActivity.LOAD_IMAGE_RESULT));
				drawCanvas(bmp);
		     }

			break;
		default:
			break;
		}
	}

}
