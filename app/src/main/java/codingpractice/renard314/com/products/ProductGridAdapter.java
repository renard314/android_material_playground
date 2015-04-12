/*
 * Copyright (c) 2015. Renard Wellnitz. This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 */

package codingpractice.renard314.com.products;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import codingpractice.renard314.com.products.model.generated.Product;
import codingpractice.renard314.com.products.ui.util.PaletteExtractor;

/**
 * Created by renard on 10/04/15.
 * Provides the views for ProductGridFragment.
 */
public class ProductGridAdapter extends BaseAdapter {


    public static final Map<String, Pair<Integer, Integer>> mColorCache = new HashMap<>();
    private static final String TAG = ProductGridAdapter.class.getSimpleName();
    final private List<Product> mProducts = new ArrayList<>();
    final private LayoutInflater mLayoutInflater;
    private final int mDefaultBackgroundColor;
    private final int mDefaultTextColor;
    private int mColumnWidth = 0;
    private int mImageHeight = 0;


    public ProductGridAdapter(LayoutInflater layoutInflater, Collection<Product> products) {
        mProducts.addAll(products);
        final Resources resources = layoutInflater.getContext().getResources();
        mDefaultBackgroundColor = resources.getColor(R.color.colorAccent);
        mDefaultTextColor = resources.getColor(R.color.primary_text_default_material_dark);
        this.mLayoutInflater = layoutInflater;
    }

    public void addAll(Collection<Product> products) {
        mProducts.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Object getItem(int i) {
        return mProducts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ProductViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.product_grid_item, viewGroup, false);
            holder = new ProductViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ProductViewHolder) convertView.getTag();
        }
        final Product product = mProducts.get(position);
        startLoadImage(convertView, holder, product);
        //US $ or Singapore $ looks the same to me but a proper shopping app handles currencies differently.
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        holder.priceView.setText(format.format(product.pricing.price));
        holder.titleView.setText(product.title);
        holder.mMeasureView.setText(product.measure.wt_or_vol);

        return convertView;
    }

    private void startLoadImage(View convertView, final ProductViewHolder holder, final Product product) {
        holder.stopColorAnimation();
        if (mColumnWidth == 0) {
            //wait for layout to be ready because we need to know the image view dimensions
            final View finalConvertView = convertView;
            holder.imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @SuppressLint("NewApi")
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    mColumnWidth = holder.imageView.getWidth();
                    mImageHeight = holder.imageView.getHeight();


                    loadImage(finalConvertView, mColumnWidth, mImageHeight, holder, product);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        holder.imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        holder.imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        } else {
            loadImage(convertView, mColumnWidth, mImageHeight, holder, product);
        }
    }

    private void loadImage(View convertView, int itemWidth, int itemHeight, final ProductViewHolder holder, final Product product) {

        Pair<Integer, Integer> colors;
        Callback callBack;
        synchronized (mColorCache) {
            colors = mColorCache.get(product.img.name);
        }
        if (colors != null) {
            //color is in cache, set it immediatly
            holder.mTextContainer.setBackgroundColor(colors.first);
            holder.titleView.setTextColor(colors.second);
            callBack = null;
        } else {
            //set default color
            holder.mTextContainer.setBackgroundColor(mDefaultBackgroundColor);
            holder.titleView.setTextColor(mDefaultTextColor);
            //once image is loaded animate the color to match the image
            callBack = new Callback() {
                @Override
                public void onSuccess() {
                    Pair<Integer, Integer> colors = null;
                    //colors will be in cache most likely
                    synchronized (mColorCache) {
                        colors = mColorCache.get(product.img.name);
                    }
                    if (colors == null) {
                        colors = Pair.create(mDefaultBackgroundColor, mDefaultTextColor);
                    }
                    holder.startColorAnimation(colors);

                }

                @Override
                public void onError() {
                }
            };
        }


        final PaletteExtractor paletteExtractor = new PaletteExtractor(product.img.name, mColorCache);
        Picasso.with(convertView.getContext().getApplicationContext())
                .load("http://media.redmart.com/newmedia/200p" + product.img.name)
                .resize(itemWidth, itemHeight)
                .centerInside()
                .transform(paletteExtractor)
                .into(holder.imageView, callBack);
    }

    final static class ProductViewHolder {
        @InjectView(R.id.text_container)
        View mTextContainer;
        @InjectView(R.id.measure_text_view)
        TextView mMeasureView;
        @InjectView(R.id.title_text_view)
        TextView titleView;
        @InjectView(R.id.price_text_view)
        TextView priceView;
        @InjectView(R.id.product_image_view)
        ImageView imageView;
        private AnimatorSet mAnimatorSet;


        ProductViewHolder(View convertView) {
            ButterKnife.inject(this, convertView);
        }

        private synchronized void stopColorAnimation() {
            if (mAnimatorSet != null && (mAnimatorSet.isRunning() || mAnimatorSet.isStarted())) {
                mAnimatorSet.cancel();
            }
        }

        private void startColorAnimation(final Pair<Integer, Integer> colors) {
            mTextContainer.post(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        stopColorAnimation();
                        mAnimatorSet = new AnimatorSet();

                        int toColor = colors.first;
                        ValueAnimator backGroundColorAnimator = new ValueAnimator();
                        int fromColor = ((ColorDrawable) mTextContainer.getBackground()).getColor();
                        backGroundColorAnimator.setIntValues(fromColor, toColor);
                        backGroundColorAnimator.setEvaluator(new ArgbEvaluator());
                        backGroundColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                mTextContainer.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
                            }
                        });

                        toColor = colors.second;
                        ValueAnimator textColorAnimator = new ValueAnimator();
                        fromColor = titleView.getCurrentTextColor();
                        textColorAnimator.setIntValues(fromColor, toColor);
                        textColorAnimator.setEvaluator(new ArgbEvaluator());
                        textColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                titleView.setTextColor((Integer) valueAnimator.getAnimatedValue());
                            }
                        });

                        mAnimatorSet.setDuration(300).playTogether(backGroundColorAnimator, textColorAnimator);
                        mAnimatorSet.start();
                    }

                }
            });

        }
    }

}
