package com.lesliechapman.countedcrossstitch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.lesliechapman.countedcrossstitch.zoomsupport.TouchImageView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class LoadImageActivity extends Activity {

	public final static int RESULT_LOAD_IMAGE = 0;
	public static final String LOAD_IMAGE_RESULT = "LOAD_IMAGE_RESULT";
	public static PointF start = new PointF();
	public static PointF last = new PointF();
	public static final String TAG = LoadImageActivity.class.getName();
	
	private ImageView rect;
	private ImageView image;
	private String picturePath;
	private int xOffset = 0;
	private int yOffset = 0;
	private int marginLeft;
	private int marginTop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_image);

		image = (ImageView) findViewById(R.id.imageView1);
		rect = (ImageView) findViewById(R.id.rectView);
		
		RelativeLayout.LayoutParams layoutPrarms = new RelativeLayout.LayoutParams(100, 100);
		layoutPrarms.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutPrarms.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rect.setLayoutParams(layoutPrarms);
        

		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, RESULT_LOAD_IMAGE);
		
		rect.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event)  {
			    ImageView imageView = (ImageView)view;
			    imageView.bringToFront();
			    
			    PointF startPoint = new PointF();
			    int EVENT_NONE = 1;
			    int EVENT_DRAG = 2;
			    int currentMode = EVENT_NONE;
			    
			    xOffset = (int)event.getRawX();
			    yOffset = (int)event.getRawY();
			    
			    //  Handle event
			    switch (event.getAction() & MotionEvent.ACTION_MASK)  {
			        case MotionEvent.ACTION_DOWN:  {
			            startPoint.set(event.getX(), event.getY());
			            currentMode = EVENT_DRAG;
			            break;
			        }  

			        case MotionEvent.ACTION_UP:
			        case MotionEvent.ACTION_POINTER_UP:  {
			            currentMode = EVENT_NONE;
			            break;
			        }  

			        case MotionEvent.ACTION_MOVE:  {
			            RelativeLayout.LayoutParams layoutPrarms = (RelativeLayout.LayoutParams)imageView.getLayoutParams();
			            marginLeft = layoutPrarms.leftMargin + (int)(event.getX() - startPoint.x) - 50;
			            marginTop = layoutPrarms.topMargin + (int)(event.getY() - startPoint.y) - 50;
			            layoutPrarms.leftMargin = marginLeft;
			            layoutPrarms.topMargin = marginTop;
			            imageView.setLayoutParams(layoutPrarms);     
			            break;
			        }  
			    }

			    //  Indicate event was handled
			    return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_load_image, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.crop:
			cropImage();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_LOAD_IMAGE:
			if (resultCode == RESULT_OK && null != data) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
				cursor.close();
				Log.d(TAG, picturePath);
				// String picturePath contains the path of selected Image

				Bitmap bmp = BitmapFactory.decodeFile(picturePath);
				image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
				image.setScaleType(ScaleType.FIT_XY);
			}

			break;

		default:
			break;
		}
	}
	
	@SuppressLint("NewApi")
	private void cropImage(){
		Bitmap bmp = BitmapFactory.decodeFile(picturePath);
		Log.d(TAG, "raw x = " + xOffset);
		Log.d(TAG, "raw y = " + yOffset);
		Log.d(TAG, "bmp h = " + bmp.getHeight());
		Log.d(TAG, "bmp w = " + bmp.getWidth());
		Log.d(TAG, "image h = " + image.getHeight());
		Log.d(TAG, "image w = " + image.getWidth());
		Log.d(TAG, "rect x = " + rect.getX());
		Log.d(TAG, "rect y = " + rect.getY());
		Log.d(TAG, "marginLeft = " + marginLeft);
		Log.d(TAG, "marginTop" + marginTop);
		
		int xPnt = (marginLeft * bmp.getWidth())/image.getWidth();
		int yPnt = (marginTop * bmp.getHeight())/image.getHeight();// - (yOffset - (int)rect.getY()) + 50
		
		Bitmap resizedbitmap = Bitmap.createBitmap(bmp, xPnt, yPnt, 100, 100);
		
		/*Parcel parcel = Parcel.obtain();
		
		resizedbitmap.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);*/
		
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		   resizedbitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		   File f = new File(Environment.getExternalStorageDirectory()
		                        + File.separator + "test1.png");
		try {
			f.createNewFile();
			//write the bytes in file
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());

			// remember close the FileOutput
			fo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent resultIntent = new Intent();
		resultIntent.putExtra(LOAD_IMAGE_RESULT, "test1.png");
		setResult(Activity.RESULT_OK, resultIntent);
		finish();		
	}

}
