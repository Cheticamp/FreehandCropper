package com.example.android.freehandcropper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Bitmap mBitmap;
    private SomeView mSomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
        mSomeView = new SomeView(this, mBitmap);
        LinearLayout layout = findViewById(R.id.layout);
        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                          LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(mSomeView, lp);
    }

    public void cropImage() {

        setContentView(R.layout.activity_picture_preview);

        ImageView imageView = findViewById(R.id.image);

        int widthOfscreen = 0;
        int heightOfScreen = 0;

        DisplayMetrics dm = new DisplayMetrics();
        try {
            getWindowManager().getDefaultDisplay().getMetrics(dm);
        } catch (Exception ex) {
        }
        widthOfscreen = dm.widthPixels;
        heightOfScreen = dm.heightPixels;

        Bitmap bitmap2 = mBitmap;

        Bitmap resultingImage = Bitmap.createBitmap(widthOfscreen,
                                                    heightOfScreen, bitmap2.getConfig());

        Canvas canvas = new Canvas(resultingImage);

        Paint paint = new Paint();

        Path path = new Path();

        List<Point> points = mSomeView.getPoints();
        for (int i = 0; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }

        // Cut out the selected portion of the image...
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, 0, 0, paint);

        // Frame the cut out portion...
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20f);
        canvas.drawPath(path, paint);

        imageView.setImageBitmap(resultingImage);

    }
}