package codingpractice.renard314.com.products;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import codingpractice.renard314.com.products.model.generated.Product;

/**
 * Created by renard on 10/04/15.
 * Provides the views for ProductGridFragment.
 */
public class ProductGridAdapter extends BaseAdapter {


    private static final String TAG = ProductGridAdapter.class.getSimpleName();
    final private List<Product> mProducts = new ArrayList<>();
    final private LayoutInflater mLayoutInflater;
    private int mColumnWidth = 0;
    private int mImageHeight = 0;


    final static class ProductViewHolder {
        @InjectView(R.id.text_container) View mTextContainer;
        @InjectView(R.id.measure_text_view) TextView mMeasureView;
        @InjectView(R.id.title_text_view) TextView titleView;
        @InjectView(R.id.price_text_view) TextView priceView;
        @InjectView(R.id.product_image_view) ImageView imageView;


        public ProductViewHolder(View convertView) {
            ButterKnife.inject(this, convertView);
        }
    }

    public ProductGridAdapter(LayoutInflater layoutInflater, Collection<Product> products) {
        mProducts.addAll(products);
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
        if(mColumnWidth==0) {
            final View finalConvertView = convertView;
            holder.imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @SuppressLint("NewApi")
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    mColumnWidth = holder.imageView.getWidth();
                    mImageHeight = holder.imageView.getHeight();


                    loadImage(finalConvertView,mColumnWidth,mImageHeight, holder, product);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        holder.imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        holder.imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        } else {
            loadImage(convertView, mColumnWidth,mImageHeight, holder, product);
        }
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        holder.priceView.setText(format.format(product.pricing.price));
        holder.titleView.setText(product.title);
        final int accentColor = holder.mMeasureView.getResources().getColor(R.color.colorAccent);
        holder.mTextContainer.setBackgroundColor(accentColor);
        holder.mMeasureView.setText(product.measure.wt_or_vol);


        return convertView;
    }

    private void loadImage(View convertView, int itemWidth, int itemHeight, ProductViewHolder holder, Product product) {

        Picasso.with(convertView.getContext().getApplicationContext())
                .load("http://media.redmart.com/newmedia/200p" + product.img.name)
                .resize(itemWidth, itemHeight)
                .centerInside()
                .transform(new PaletteExtractor(holder))
                .into(holder.imageView);
    }

    private static class PaletteExtractor implements Transformation {

        private final ProductViewHolder mHolder;

        PaletteExtractor(ProductViewHolder holder){

            mHolder = holder;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Palette palette = Palette.generate(source);
            mHolder.mTextContainer.post(new Runnable() {
                @Override
                public void run() {
                    if(palette.getMutedSwatch()==null){
                        return;
                    }
                    final int color = palette.getMutedSwatch().getRgb();
                    ValueAnimator anim = new ValueAnimator();
                    final int color1 = ((ColorDrawable) mHolder.mTextContainer.getBackground()).getColor();
                    anim.setIntValues(color1, color);
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            mHolder.mTextContainer.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
                        }
                    });

                    anim.setDuration(200);
                    anim.start();
                    final int textColor = palette.getMutedSwatch().getTitleTextColor();
                    ValueAnimator anim2 = new ValueAnimator();
                    final int textColor1 = mHolder.titleView.getCurrentTextColor();
                    anim2.setIntValues(textColor1, textColor);
                    anim2.setEvaluator(new ArgbEvaluator());
                    anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            mHolder.titleView.setTextColor((Integer) valueAnimator.getAnimatedValue());
                        }
                    });

                    anim2.setDuration(200);
                    anim2.start();

                }
            });

            //mHolder.mTextContainer.setBackgroundColor(color);
            //mHolder.titleView.setTextColor(textColor);

            return source;
        }

        @Override
        public String key() {
            return "PaletteExtractor";
        }
    }
}
