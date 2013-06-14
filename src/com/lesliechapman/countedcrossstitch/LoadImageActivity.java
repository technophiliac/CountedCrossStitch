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
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class LoadImageActivity extends Activity {

	public final static int RESULT_LOAD_IMAGE = 0;
	public static final String LOAD_IMAGE_RESULT = "LOAD_IMAGE_RESULT";
	public static PointF start = new PointF();
	public static PointF last = new PointF();
	
	private ImageView rect;
	private ImageView image;
	private String picturePath;
	private int xOffset;
	private int yOffset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_image);

		image = (ImageView) findViewById(R.id.imageView1);
		rect = (ImageView) findViewById(R.id.rectView);

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
			    
			    xOffset = Math.round(event.getX());
			    yOffset = Math.round(event.getY());

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
			            int left = layoutPrarms.leftMargin + (int)(xOffset - startPoint.x) - 100;
			            int top = layoutPrarms.topMargin + (int)(yOffset - startPoint.y) - 100;
			            layoutPrarms.leftMargin = left;
			            layoutPrarms.topMargin = top;
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
				System.out.println(picturePath);
				// String picturePath contains the path of selected Image

				Bitmap bmp = BitmapFactory.decodeFile(picturePath);
				image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			}

			break;

		default:
			break;
		}
	}
	
	private void cropImage(){
		Bitmap bmp = BitmapFactory.decodeFile(picturePath);
		Bitmap resizedbitmap = Bitmap.createBitmap(bmp, 0, 0, 100, 100);
		
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
