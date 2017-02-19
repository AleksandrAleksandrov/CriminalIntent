package intent.criminal.aleksandrov.aleksandr.criminalintent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by aleksandr on 2/12/17.
 */

public class FullPictureDialogFragment extends DialogFragment {

    private static final String PICTURE_PATH = "picture_path";
    private File picturePath;

    private ImageView mImageView;

    static FullPictureDialogFragment newInstance(File filePath) {
        FullPictureDialogFragment fullPictureDialogFragment = new FullPictureDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PICTURE_PATH, filePath);
        fullPictureDialogFragment.setArguments(bundle);
        return fullPictureDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        picturePath = (File) getArguments().getSerializable(PICTURE_PATH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_picture, container, false);
        mImageView = (ImageView) view.findViewById(R.id.image_view_full_crime_picture);

        Bitmap bitmap = BitmapFactory.decodeFile(picturePath.getPath());

        mImageView.setImageBitmap(bitmap);


        return view;
    }
}
