package com.example.android.sutechteam;

import android.graphics.*;
import com.squareup.picasso.Transformation;

/**
 * Transforms an image into a circle representation. Such as a avatar.
 */
public class CircularTransformation implements Transformation
{
    int radius = 10;

    public CircularTransformation(final int radius)
    {
        this.radius = radius;
    }

    public CircularTransformation()
    {
    }

    @Override
    public Bitmap transform(final Bitmap source)
    {
        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(Color.parseColor("#BAB399"));

        canvas.drawCircle(source.getWidth() / 2 + 0.7f, source.getHeight() / 2 + 0.7f, source.getWidth() / 2 - 1.1f, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(source, rect, rect, paint);

        if(source != output) {
            source.recycle();
        }
        return output;
    }

    @Override
    public String key()
    {
        return "circular" + String.valueOf(radius);
    }
}
