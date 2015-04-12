/*
 * Copyright (c) 2015. Renard Wellnitz. This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 */

package codingpractice.renard314.com.products.ui;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.Collection;

import butterknife.ButterKnife;
import butterknife.InjectView;
import codingpractice.renard314.com.products.ProductGridAdapter;
import codingpractice.renard314.com.products.R;
import codingpractice.renard314.com.products.model.generated.Product;
import de.greenrobot.event.EventBus;

/**
 * Created by renard on 10/04/15.
 * Shows a grid of products.
 */
public class ProductGridFragment extends Fragment implements AdapterView.OnItemClickListener {

    final static String TAG = ProductGridFragment.class.getSimpleName();
    @InjectView(R.id.products_grid_view)
    GridView mGridView;
    @InjectView(R.id.empty_view)
    View mEmptyView;
    @InjectView(R.id.logo_image_view)
    ImageView mLogoView;
    private ProductGridAdapter mAdapter;

    public static ProductGridFragment newInstance() {
        return new ProductGridFragment();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FragmentManager fm = getFragmentManager();
        Product product = (Product) mAdapter.getItem(i);
        ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance(product);
        final boolean showAsDialog = getActivity().getResources().getBoolean(R.bool.show_details_as_dialog);
        if(showAsDialog){
            productDetailFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            productDetailFragment.show(fm, ProductDetailFragment.TAG);
        } else {
            fm.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, productDetailFragment, ProductDetailFragment.TAG)
                    .addToBackStack("detail")
                    .commit();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.inject(this, layout);
        mGridView = (GridView) layout.findViewById(R.id.products_grid_view);
        mEmptyView = layout.findViewById(R.id.empty_view);
        MainActivity activity = (MainActivity) getActivity();
        Collection<Product> products = activity.getProducts();
        if (products.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
        mAdapter = new ProductGridAdapter(inflater, products);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        EventBus.getDefault().register(this);
        return layout;
    }

    /**
     * EventBus callback from ProductLoading Fragment
     *
     * @param message
     */
    public void onEventMainThread(ProductsMessage message) {
        if (mAdapter.isEmpty()) {
            startRemoveLogoAnimation();
        }
        mAdapter.addAll(message.mProducts);
        mAdapter.notifyDataSetChanged();

    }

    private void startRemoveLogoAnimation() {
        final ObjectAnimator backgroundColor = ObjectAnimator.ofFloat(mEmptyView, "alpha", 0);
        ObjectAnimator slideOut = ObjectAnimator.ofFloat(mLogoView, "y", -mLogoView.getHeight());
        slideOut.setDuration(300);
        backgroundColor.setDuration(200);

        slideOut.setInterpolator(new AccelerateInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(slideOut, backgroundColor);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mGridView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public static class ProductsMessage {
        Collection<Product> mProducts;

        public ProductsMessage(Collection<Product> delta) {
            mProducts = delta;
        }
    }
}
