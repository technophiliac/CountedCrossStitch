package com.lesliechapman.countedcrossstitch;

import com.lesliechapman.countedcrossstitch.util.ColorUtils;
import com.lesliechapman.countedcrossstitch.zoomsupport.TouchImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {

	private final static int COLOR_OUTLINE_REQUEST = 1;
	public static int SQUARE_SIZE = 10;

	Bitmap pattern;
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
		pattern = Bitmap.createBitmap((w * SQUARE_SIZE), (h * SQUARE_SIZE),
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
							if (x == 59 && y == 57) {
								Log.e("MainActivity",
										ColorUtils.getDMCColor(color));
								Log.e("MainActivity", ((x * SQUARE_SIZE) + i)
										+ ", Y:" + ((y * SQUARE_SIZE) + i));
							}
						}
					}
				}
			}
		}

		touch.setImageBitmap(pattern);
		touch.setMaxZoom(10f);

		touch.setOnClickListener(myhandler1);
	}

	private void colorSquareAt(float origX, float origY){
		String sx = Integer.toString(Math.round(origX));
		sx = sx.substring(0, sx.length()-1);
		String sy = Integer.toString(Math.round(origY));
		sy = sy.substring(0, sy.length()-1);
		
		int startX = Integer.parseInt(sx + "1");
		int startY = Integer.parseInt(sy + "1");
		
		for(int x = startX; x<startX+SQUARE_SIZE-1; x++ ){
			for(int y = startY; y<startY+SQUARE_SIZE-1; y++ ){
				if(x>=0 && y>=0 && x<pattern.getWidth() && y<pattern.getHeight())
					pattern.setPixel(x, y, Color.MAGENTA);				
			}			
		}
	}

	View.OnClickListener myhandler1 = new View.OnClickListener() {
		@SuppressLint("NewApi")
		public void onClick(View v) {

			// this is where on the screen I clicked
			System.out.println("start.x = " + TouchImageView.start.x);
			System.out.println("start.y = " + TouchImageView.start.y);

			RectF r = new RectF();
			TouchImageView.matrix.mapRect(r);
			Log.e("MainActivity", "Rect " + r.left + " " + r.top + " "
					+ r.right + " " + r.bottom + " " + TouchImageView.saveScale
					+ " ");
			
			float[] values = new float[9];
			TouchImageView.matrix.getValues(values);
			float x_coord = ((TouchImageView.start.x - values[2])*TouchImageView.saveScale )/values[0];
			float y_coord= ((TouchImageView.start.y - values[5])*TouchImageView.saveScale )/ values[4];
			
			System.out.println("coords: " + x_coord + ", " + y_coord);

			colorSquareAt(x_coord/TouchImageView.saveScale, y_coord/TouchImageView.saveScale);

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
				int color = data.getIntExtra(PaletteActivity.COLOR_REQUEST, -1);
				outlineColor(color);
			}

			break;

		default:
			break;
		}
	}

}
