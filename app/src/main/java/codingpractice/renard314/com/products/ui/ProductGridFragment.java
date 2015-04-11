package codingpractice.renard314.com.products.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.Collection;

import butterknife.ButterKnife;
import butterknife.OnItemClick;
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
    private ProductGridAdapter mAdapter;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FragmentManager fm = getFragmentManager();
        Product product = (Product) mAdapter.getItem(i);
        ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance(product);
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.fragment_container, productDetailFragment, ProductDetailFragment.TAG)
                .addToBackStack(null)
                .commit();

    }



    public static class ProductsMessage {
        Collection<Product> mProducts;

        public ProductsMessage(Collection<Product> delta) {
            mProducts = delta;
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final GridView gridView = (GridView) inflater.inflate(R.layout.fragment_products, container, false);
        MainActivity activity = (MainActivity) getActivity();
        Collection<Product> products = activity.getProducts();
        mAdapter = new ProductGridAdapter(inflater, products);
        gridView.setAdapter(mAdapter);
        final View emptyView = inflater.inflate(R.layout.fragment_empty_products_gird, null);
        gridView.setEmptyView(emptyView);
        gridView.setOnItemClickListener(this);
        EventBus.getDefault().register(this);
        return gridView;
    }

    /**
     * EventBus callback from ProductLoading Fragment
     *
     * @param message
     */
    public void onEventMainThread(ProductsMessage message) {
        mAdapter.addAll(message.mProducts);
        mAdapter.notifyDataSetChanged();
    }

    public static ProductGridFragment newInstance() {
        return new ProductGridFragment();
    }
}
