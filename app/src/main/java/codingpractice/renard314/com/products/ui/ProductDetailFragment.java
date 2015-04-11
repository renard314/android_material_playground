package codingpractice.renard314.com.products.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import codingpractice.renard314.com.products.R;
import codingpractice.renard314.com.products.model.generated.Product;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by renard on 11/04/15.
 * Shows the details of a single product.
 */
public class ProductDetailFragment extends Fragment {

    final static String TAG = ProductDetailFragment.class.getSimpleName();
    private static final String ARG_PRODUCT = "arg_product";

    @InjectView(R.id.product_detail_image_view)
    ImageView mImageView;
    @InjectView(R.id.my_awesome_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.title_text_view)
    TextView mTitleTextView;
    @InjectView(R.id.measure_text_view)
    TextView mMeasureTextView;

    PhotoViewAttacher mAttacher;

    //?attr/colorPrimary

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        ButterKnife.inject(this, view);
        final Product product = getArguments().getParcelable(ARG_PRODUCT);
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                final int width = mImageView.getWidth();
                final int height = mImageView.getHeight();

                loadImage(width, height, product);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        mTitleTextView.setText(product.title);
        mMeasureTextView.setText(product.measure.wt_or_vol);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }


    private void loadImage(int width, int height, Product product) {
        Callback imageLoadedCallback = new Callback() {

            @Override
            public void onSuccess() {
                if (mAttacher != null) {
                    mAttacher.update();
                } else {
                    mAttacher = new PhotoViewAttacher(mImageView);
                }
            }

            @Override
            public void onError() {
                // TODO show error

            }
        };

        Picasso.with(getActivity().getApplicationContext())
                .load("http://media.redmart.com/newmedia/200p" + product.img.name)
                .resize(width, height)
                .centerInside()
                .into(mImageView, imageLoadedCallback);
    }

    public static ProductDetailFragment newInstance(Product product) {
        final ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, product);
        productDetailFragment.setArguments(args);
        return productDetailFragment;
    }

}
