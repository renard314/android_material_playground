/*
 * Copyright (c) 2015. Renard Wellnitz. This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 */

package codingpractice.renard314.com.products.ui;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import codingpractice.renard314.com.products.ProductGridAdapter;
import codingpractice.renard314.com.products.R;
import codingpractice.renard314.com.products.model.generated.Image;
import codingpractice.renard314.com.products.model.generated.Product;
import codingpractice.renard314.com.products.ui.util.PaletteExtractor;


/**
 * Created by renard on 11/04/15.
 * Shows the details of a single product.
 */
public class ProductDetailFragment extends DialogFragment implements ObservableScrollViewCallbacks {

    final static String TAG = ProductDetailFragment.class.getSimpleName();
    private static final String ARG_PRODUCT = "arg_product";

    private static final float MAX_TEXT_SCALE_DELTA = 0.15f;

    @InjectView(R.id.my_awesome_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.product_detail_image_view)
    ImageView mImageView;
    @InjectView(R.id.product_secondary_detail_image_view)
    ImageView mSecondImageView;
    @InjectView(R.id.overlay)
    View mOverlayView;
    @InjectView(R.id.scroll)
    ObservableScrollView mScrollView;
    @InjectView(R.id.title_text_view)
    TextView mTitleView;
    @InjectView(R.id.measure_text_view)
    TextView mMeasureView;
    @InjectView(R.id.price_text_view)
    TextView mPriceView;
    @InjectView(R.id.description_text_view)
    TextView mDescriptionTextView;
    @InjectView(R.id.fab)
    View mFab;


    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private boolean mFabIsShown;
    private Integer mBackGroundColor;

    public static ProductDetailFragment newInstance(Product product) {
        final ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, product);
        productDetailFragment.setArguments(args);
        return productDetailFragment;
    }

    private int getActionBarSize() {
        return (int) getResources().getDimension(R.dimen.actionBarSize);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        ButterKnife.inject(this, view);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewCompat.setElevation(mToolbar, 4f);

        final Product product = getArguments().getParcelable(ARG_PRODUCT);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = getActionBarSize();
        mToolbar.setTitle(null);
        mScrollView.setScrollViewCallbacks(this);
        mTitleView.setText(product.title);
        mMeasureView.setText(product.measure.wt_or_vol);
        mDescriptionTextView.setText(product.desc);
        //product information should really contain currency information and amount should not be in float either
        //this is a demo so displaying as $ is fine.
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        mPriceView.setText(format.format(product.pricing.price));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Item added to cart.", Toast.LENGTH_SHORT).show();
            }
        });
        mBackGroundColor = getResources().getColor(R.color.colorAccent);
        //workaround for library bug
        mFab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);


        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                //mScrollView.scrollTo(0, mFlexibleSpaceImageHeight);
                //onScrollChanged(0, false, false);
                mScrollView.scrollTo(0, 1);
                mScrollView.scrollTo(0, 0);
            }
        });

        startLoadingImage(mImageView, product.img.name, true);
        String secondaryImage = findSecondaryImageThatIsDifferentToMainImage(product);
        if (secondaryImage != null) {
            startLoadingImage(mSecondImageView, secondaryImage, false);
        } else {
            mSecondImageView.setVisibility(View.GONE);
        }
        return view;
    }

    @Nullable
    private String findSecondaryImageThatIsDifferentToMainImage(Product product) {
        for (Image second : product.images) {
            if (second.position != product.img.position) {
                return second.name;
            }
        }

        return null;
    }

    //TODO remove code duplication (see ProductGridAdapter)
    private void startLoadingImage(final ImageView imageView, final String name, final boolean extractColor) {

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                final int width = imageView.getWidth();
                final int height = imageView.getHeight();

                loadImage(width, height, imageView, name, extractColor);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

    }

    private void loadImage(int width, int height, final ImageView imageView, final String name, boolean extractColor) {
        Callback callbacks = null;
        final RequestCreator creator = Picasso.with(getActivity().getApplicationContext())
                .load("http://media.redmart.com/newmedia/200p" + name);

        if (extractColor) {
            final Pair<Integer, Integer> colors = ProductGridAdapter.mColorCache.get(name);
            if (colors != null) {
                applyColors(colors);

            } else {
                creator.transform(new PaletteExtractor(name, ProductGridAdapter.mColorCache));
                callbacks = new Callback() {
                    @Override
                    public void onSuccess() {
                        final Pair<Integer, Integer> colors = ProductGridAdapter.mColorCache.get(name);
                        applyColors(colors);
                    }

                    @Override
                    public void onError() {
                    }
                };
            }
        }
        creator.resize(width, height)
                .centerInside()
                .into(imageView, callbacks);
    }

    private void applyColors(Pair<Integer, Integer> colors) {
        if (colors != null) {
            mBackGroundColor = colors.first;
            mToolbar.setBackgroundColor(colors.first);
            mTitleView.setTextColor(colors.second);
            mMeasureView.setTextColor(colors.second);
            mOverlayView.setBackgroundColor(colors.first);
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        final float fraction = ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1);
        ViewHelper.setAlpha(mOverlayView, fraction);
        ArgbEvaluator a = new ArgbEvaluator();
        final int toColor = getResources().getColor(R.color.colorPrimary);

        final int animatedColor = (int) a.evaluate(fraction, mBackGroundColor, toColor);
        mOverlayView.setBackgroundColor(animatedColor);
        mToolbar.setBackgroundColor(animatedColor);

        // Scale title text
        /*
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);
        */

        // Translate title text
        int titleTranslationY = mFlexibleSpaceImageHeight - scrollY;
        titleTranslationY = Math.max(0, titleTranslationY);
        ViewHelper.setTranslationY(mToolbar, titleTranslationY);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight + mActionBarSize - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(-scrollY + mFlexibleSpaceImageHeight + mActionBarSize - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        //ViewHelper.setTranslationX(mFab, mOverlayView.getWidth()  -mFabMargin - mFab.getWidth());
        ViewHelper.setTranslationY(mFab, fabTranslationY);

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }


    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

}
