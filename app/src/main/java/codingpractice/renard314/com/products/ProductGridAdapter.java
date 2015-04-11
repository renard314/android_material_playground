package codingpractice.renard314.com.products;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        holder.priceView.setText("" + product.pricing.price);
        holder.titleView.setText(product.title);


        return convertView;
    }

    private void loadImage(View convertView, int itemWidth, int itemHeight, ProductViewHolder holder, Product product) {
        Picasso.with(convertView.getContext().getApplicationContext())
                .load("http://media.redmart.com/newmedia/200p" + product.img.name)
                .resize(itemWidth, itemHeight)
                .centerInside()
                .into(holder.imageView);
    }
}
