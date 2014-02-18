package com.example.greyarea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.RectF;

/**
 * TODO: document your custom view class.
 */
public class GreyDrawable extends View {
	private final static int TO_GREY_SCALE = 0;
	private final static int TO_COLOR = 1;
	private Bitmap mainBitmap;
	private Bitmap colorBitmap;
	private Bitmap greyBitmap;
	private Bitmap shadowBitmap;
	private Canvas shadowCanvas = new Canvas();
	private Path path = null;
	private Paint blackPaint = new Paint();
	private int brushRadius = 5;
	private Bitmap changeToBitmap = greyBitmap;
	private int currentBitmap = TO_GREY_SCALE;
	

	public GreyDrawable(Context context) {
		super(context);
		blackPaint.setColor(Color.BLACK);
		blackPaint.setStyle(Paint.Style.STROKE);
		blackPaint.setStrokeWidth(brushRadius);
		
	}

	public GreyDrawable(Context context, AttributeSet attrs) {
		super(context, attrs);
		blackPaint.setColor(Color.BLACK);
		blackPaint.setStyle(Paint.Style.STROKE);
		blackPaint.setStrokeWidth(brushRadius);

		
	}

	public GreyDrawable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		blackPaint.setColor(Color.BLACK);
		blackPaint.setStyle(Paint.Style.STROKE);
		blackPaint.setStrokeWidth(brushRadius);

	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (path != null) {
			RectF bounds = new RectF();
			path.computeBounds(bounds, true);
			for(int i = (int) bounds.left; i < (int) bounds.right; i++){
				for(int j = (int) bounds.top; j < (int) bounds.bottom; j++){
					int toColor = changeToBitmap.getPixel(i, j);
					int shadowColor = shadowBitmap.getPixel(i, j);
					if( shadowColor  == Color.BLACK){
						try {
							mainBitmap.setPixel(i, j, toColor);
						} catch (IllegalStateException e) {
							Log.i("Error", "Tried to set pixel at" + String.valueOf(i));
						}
					}
				}
			}
		}
		canvas.drawBitmap(mainBitmap, 0, 0, blackPaint);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(event.getX() < mainBitmap.getWidth() && event.getY() < mainBitmap.getHeight()){
			switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					touchDown(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_MOVE:
					touchMove(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_CANCEL:
					break;
				default:
					break;
			}
		}
		return true;
	}
	
	public void setImageBitmap(Bitmap bitmap){
		colorBitmap = bitmap;
		mainBitmap = colorBitmap.copy(Bitmap.Config.RGB_565, true);
		shadowBitmap = Bitmap.createBitmap(colorBitmap.getWidth(), colorBitmap.getHeight(), Bitmap.Config.RGB_565);
		shadowBitmap.eraseColor(Color.WHITE);
		greyBitmap = toGrayscale(colorBitmap);
		shadowCanvas.setBitmap(shadowBitmap);
		changeToBitmap = greyBitmap;
	}
	
	/*Method taken from example that can be found
	 * at http://stackoverflow.com/questions/3373860/convert-a-bitmap-to-grayscale-in-android/3391061#3391061
	 */
	protected Bitmap toGrayscale(Bitmap bmpOriginal){        
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();    

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
	
	private void touchDown(float x, float y){
		if (path != null){
			path.reset();
		} else {
			path = new Path();
		}
		mainBitmap.setPixel((int) x, (int) y, changeToBitmap.getPixel((int) x, (int) y));
		path.moveTo(x, y);
	}
	
	private void touchMove(float x, float y){
		path.lineTo(x, y);
		shadowCanvas.drawPath(path, blackPaint);
		this.invalidate();
		path.moveTo(x, y);
		
	}
	
	protected void setBrushWidth(int x){
		blackPaint.setStrokeWidth(x);
	}
	
	protected void setConversion(int x){
		switch(x){
			case TO_COLOR:
				changeToBitmap = colorBitmap;
				break;
			case TO_GREY_SCALE:
				changeToBitmap = greyBitmap;
				break;
			default:
				changeToBitmap = greyBitmap;
				break;
		}
		shadowBitmap.eraseColor(Color.WHITE);
		shadowCanvas.setBitmap(shadowBitmap);
	}
	
	protected void resetImage(){
		shadowBitmap.eraseColor(Color.WHITE);
		mainBitmap = colorBitmap.copy(Bitmap.Config.RGB_565, true);
		path.reset();
		Log.i("ChangeToBitmap", changeToBitmap.toString());
		this.invalidate();
	}
}
