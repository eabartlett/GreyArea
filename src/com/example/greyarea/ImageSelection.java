package com.example.greyarea;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class ImageSelection extends Activity {
	
	static final int REQUEST_IMAGE_CAPTURE = 1;
	String myCurrentPhotoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_selection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_selection, menu);
		return true;
	}
	
	/* Sends intent to select from pre-loaded images */
	public void selectPreloadedIntent(View view){
		Intent selectPreloaded = new Intent(this, SelectStaticImage.class);
		startActivity(selectPreloaded);
	}
	
	/* Method called when Take A Photo is selected */
	public void takePicture(View view){
		boolean pictureTaken = dispatchTakePictureIntent();
		Log.i("pictureTaken", String.valueOf(pictureTaken));
		if(!pictureTaken){
			photoLoadError();
		}
	}

	private void photoLoadError() {
		Intent photoError = this.getIntent();
		photoError.putExtra("error", true);
		startActivity(photoError);
	}
	
	/* Send intent to take a picture to camera application
	 * save file to gallery if photo created, else go back to 
	 * starting activity
	 */
	private boolean dispatchTakePictureIntent(){
		Log.d("MSG", "Dispatching Take Photo Intent");
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null){
			File photoFile = null;
			try{
				photoFile = createImageFile();
			}catch (IOException e){
				// Error creating file name
			}
			if (photoFile != null){
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				takePictureIntent.putExtra("preloaded", false);
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_IMAGE_CAPTURE){
			if(resultCode == RESULT_OK){
				Log.d("Activity Change", "Going into Drawing Activity");
				Intent startDrawing = new Intent(this, DrawActivity.class);
				startDrawing.putExtra("imagefile", myCurrentPhotoPath);
				startActivity(startDrawing);
			}
		}
	}
	
	/* Create file name/path for photo taken to be saved to
	 * and throw an IOException if there is a collision
	 */
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException{
		//create image file
		String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
		String fileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(fileName, ".jpg", storageDir);
		
		myCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}
	

}
