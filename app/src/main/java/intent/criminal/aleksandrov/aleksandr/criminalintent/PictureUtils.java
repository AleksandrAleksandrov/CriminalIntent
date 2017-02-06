package intent.criminal.aleksandrov.aleksandr.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by aleksandr on 2/5/17.
 */

public class PictureUtils {

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();

        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        // Чтение размеров изображения на диске
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float scrWidth = options.outWidth;
        float scrHeight = options.outHeight;

        // Вычисление степени масштабирования
        int inSampleSize = 1;
        if (scrHeight > destHeight || scrWidth > destWidth) {
            if (scrWidth > scrHeight) {
                inSampleSize = Math.round(scrHeight / scrWidth);
            } else {
                inSampleSize = Math.round(scrWidth / scrHeight);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // Чтение данных и создание итогового изображения
        return BitmapFactory.decodeFile(path, options);
    }
}
