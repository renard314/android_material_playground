/*
 * Copyright (c) 2015. Renard Wellnitz. This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 */

package codingpractice.renard314.com.products.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by renard on 11/04/15.
 * This layout is meant to be a child of a GridView.
 * Since the GridView will stretch its children to match the desired number of columns this layout tries to maintain a constant aspect ratio.
 */
public class ProductLayout extends FrameLayout {

    private final static float TARGET_ASPECT_RATIO = 1.31f;

    public ProductLayout(Context context) {
        super(context);
    }

    public ProductLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 0);

        // set height depending on the width
        int minh = (int) ((TARGET_ASPECT_RATIO * w) - getPaddingBottom() - getPaddingTop());
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
    }
}
