package com.lesliechapman.countedcrossstitch;

import java.util.ArrayList;

import com.lesliechapman.countedcrossstitch.util.ColorUtils;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaletteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_palette);
		
		drawPalette();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_palette, menu);
		return true;
	}
	
	@SuppressLint("NewApi")
	private void drawPalette(){
		LinearLayout leftFrame = (LinearLayout)findViewById(R.id.linearLeft);
		LinearLayout rightFrame = (LinearLayout)findViewById(R.id.linearRight);
		
		ArrayList<Integer> colors = new ColorUtils(this).getImageColors();
		
		TextView loading = (TextView)findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		
		for (int i = 0; i < colors.size(); i++) {
			ShapeDrawable rect = new ShapeDrawable(new RectShape());
	        rect.getPaint().setColor(colors.get(i));

	        ImageView view = new ImageView(this);	        
	        view.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
	        view.setBackground(rect);        
	        leftFrame.addView(view);
	        view.setId(i);
	        
	        view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("YOU CLICKED: " + v.getId());
					((ShapeDrawable)v.getBackground()).getPaint().getColor();
					
				}
			});
	        
	        
	        TextView textView = new TextView(this);
	        textView.setText(ColorUtils.getDMCColor(colors.get(i)));
	        textView.setLayoutParams(new ViewGroup.LayoutParams(500, 50));
	        rightFrame.addView(textView);
		}
		
		ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.getPaint().setColor(Color.DKGRAY);

        ImageView view = new ImageView(this);	        
        view.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
        view.setBackground(rect);        
        leftFrame.addView(view);
        
        view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("YOU CLICKED NONE");
				
			}
		});
        
        
        TextView textView = new TextView(this);
        textView.setText("NONE");
        textView.setLayoutParams(new ViewGroup.LayoutParams(500, 50));
        rightFrame.addView(textView);
        
	}

}
