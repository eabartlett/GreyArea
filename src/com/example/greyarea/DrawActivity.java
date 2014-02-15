package com.example.greyarea;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

public class DrawActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//get intent for information
		Intent intent = getIntent();
		String imagePath = intent.getStringExtra("imagefile");
		GreyDrawable image = (GreyDrawable) findViewById(R.id.drawing_image);
		Bitmap imageBitmap;
		if(intent.getBooleanExtra("preloaded", false)){
			if(imagePath.equals("drink")){
				imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.drink);
			}else if(imagePath.equals("sunflower")){
				imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sunflower);
			}else{
				imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.abstract_art);
			}
		} else{
			imageBitmap = BitmapFactory.decodeFile(imagePath);			
		}
		image.setImageBitmap(imageBitmap);
		
		Spinner brushSize = (Spinner) findViewById(R.id.brush_size);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
				R.array.brush_size, android.R.layout.simple_spinner_dropdown_item);
		//Define layout for when displaying choices
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Apply adapter to spinners
		brushSize.setAdapter(adapter);
		brushSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
				GreyDrawable canvasView = (GreyDrawable) findViewById(R.id.drawing_image);
				canvasView.setBrushWidth((pos +1) * 10);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent){
				return;
			}
		});
		Spinner toColor = (Spinner) findViewById(R.id.x_to_x);
		ArrayAdapter<CharSequence> toColorAdapter = ArrayAdapter.createFromResource(this, 
				R.array.to_color, android.R.layout.simple_spinner_dropdown_item);
		//Define layout for when displaying choices
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Apply adapter to spinners
		toColor.setAdapter(toColorAdapter);
		toColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
				GreyDrawable greyDrawable = (GreyDrawable) findViewById(R.id.drawing_image);
				greyDrawable.setConversion(pos);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent){
				return;
			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.draw, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void reset(View view){
		GreyDrawable greyDrawable = (GreyDrawable) findViewById(R.id.drawing_image);
		greyDrawable.resetImage();
	}

}
