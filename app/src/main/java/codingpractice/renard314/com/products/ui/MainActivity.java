/*
 * Copyright (c) 2015. Renard Wellnitz. This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 */

package codingpractice.renard314.com.products.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import codingpractice.renard314.com.products.R;
import codingpractice.renard314.com.products.model.generated.Product;
import codingpractice.renard314.com.products.model.generated.ProductsResponse;
import codingpractice.renard314.com.products.network.JacksonRequest;
import codingpractice.renard314.com.products.network.VolleySingleton;
import de.greenrobot.event.EventBus;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = RetainFragment.class.getSimpleName();

    /**
     * Locate an existing instance of this Fragment or if not found, create and
     * add it using FragmentManager.
     *
     * @param fm The FragmentManager manager to use.
     * @return The existing instance of the Fragment or the new instance if just
     * created.
     */
    private static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {

        // Check to see if we have retained the worker fragment.
        RetainFragment mRetainFragment = (RetainFragment) fm.findFragmentByTag(TAG);

        // If not retained (or first time running), we need to create and add it.
        if (mRetainFragment == null) {
            mRetainFragment = new RetainFragment();
            fm.beginTransaction().add(mRetainFragment, TAG).commitAllowingStateLoss();
        }

        return mRetainFragment;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findOrCreateRetainFragment(getFragmentManager());

        if (savedInstanceState == null) {
            final ProductGridFragment productGridFragment = ProductGridFragment.newInstance();

            android.app.FragmentManager fm = getFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, productGridFragment, ProductGridFragment.TAG)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        final DialogFragment fragmentByTag = (DialogFragment) getFragmentManager().findFragmentByTag(ProductDetailFragment.TAG);
        if (fragmentByTag != null) {
            fragmentByTag.dismiss();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public Set<Product> getProducts() {
        return findOrCreateRetainFragment(getFragmentManager()).mProducts;
    }

    /**
     * A simple non-UI Fragment that stores a single Object and is retained over configuration
     * Keeps all products in memory and is responsible for fetching them from the server.
     * Depending on how often the data changes production code should make use of a content provider to persist the loaded products.
     */
    public static class RetainFragment extends Fragment implements Response.Listener<ProductsResponse>, Response.ErrorListener {
        private final static String QUERY_PAGE = "page";
        private final static String QUERY_PAGE_SIZE = "pageSize";
        private final static String QUERY_INSTOCK = "instock";
        private final Uri mBaseUri = new Uri.Builder().scheme("https").authority("api.redmart.com").path("v1.5.1/products/new").build();
        private Set<Product> mProducts = new LinkedHashSet<>();
        private long mTotalNumberOfProducts = 0;

        /**
         * Empty constructor as per the Fragment documentation
         */
        public RetainFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.i(TAG, "DATA Fragment CREATE");
            setRetainInstance(true);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            Log.i(TAG, "DATA Fragment onAttach");
            // https://api.redmart.com/v1.5.1/products/new?page=0&pageSize=3&sort=null&instock=false
            if (mProducts.isEmpty()) {
                Log.i(TAG, "DATA fetching everything");
                fetchProducts(0, 10, getActivity().getApplicationContext());
            }

        }

        private void fetchProducts(int page, int pageSize, Context applicationContext) {
            //final String initialRequestUrl = "https://api.redmart.com/v1.5.1/products/new?page=0&pageSize=10&sort=null&instock=false";
            final Uri uri = mBaseUri.buildUpon().appendQueryParameter(QUERY_PAGE, String.valueOf(page)).appendQueryParameter(QUERY_PAGE_SIZE, String.valueOf(pageSize)).appendQueryParameter(QUERY_INSTOCK, "false").build();
            final JacksonRequest<ProductsResponse> productsResponseJacksonRequest = new JacksonRequest<>(Request.Method.GET, uri.toString(), ProductsResponse.class, this, this);
            productsResponseJacksonRequest.setTag(TAG);
            VolleySingleton.getInstance(applicationContext).addToRequestQueue(productsResponseJacksonRequest);
        }

        private void getRestOfProducts(long total) {
            //spawn  enough volley requests to load the rest of the products
            //we assume that the intial request fetched the first 10 products

            final Context applicationContext = getActivity().getApplicationContext();
            //fetch next 10
            fetchProducts(1, 10, applicationContext);
            total -= 20;
            //fetch in batches of 20 after that
            int numRequests = (int) (total / 20) + 1;
            for (int i = 0; i < numRequests; i++) {
                fetchProducts(i, 20, applicationContext);
            }
        }

        /**
         * merge product list from newProducts into our Set
         *
         * @param newProducts
         */
        private void merge(ProductsResponse newProducts) {
            List<Product> delta = new ArrayList<>();
            for (Product p : newProducts.products) {
                //HashSet does not replace the old instance if it exists.

                if (mProducts.add(p)) {
                    delta.add(p);
                }
            }
            if (delta.size() > 0) {
                EventBus.getDefault().post(new ProductGridFragment.ProductsMessage(delta));
            }
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.i(TAG, "DATA Fragment DESTROY");
            VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue().cancelAll(TAG);
        }


        @Override
        public void onErrorResponse(VolleyError volleyError) {
            //TODO dialog with retry
            Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public synchronized void onResponse(ProductsResponse productsResponse) {
            if (mTotalNumberOfProducts > 0 && productsResponse.total != mTotalNumberOfProducts) {
                //number of products has changed while we are busy fetching them
                //handling this gracefully is better done in another excercise;
                //for now we stop all network requests and start from scratch
                VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue().cancelAll(TAG);
                getRestOfProducts(productsResponse.total);
            } else if (mTotalNumberOfProducts == 0) {
                getRestOfProducts(productsResponse.total);
            }
            mTotalNumberOfProducts = productsResponse.total;
            merge(productsResponse);
        }

    }

}
