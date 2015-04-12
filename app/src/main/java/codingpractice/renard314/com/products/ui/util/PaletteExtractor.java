/*
 * Copyright (c) 2015. Renard Wellnitz. This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 */

package codingpractice.renard314.com.products.ui.util;

import android.graphics.Bitmap;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.squareup.picasso.Transformation;

import java.util.Map;

/**
 * extracts colors from a bitmap while loading it. the result is placed in mColorCache.
 */
public class PaletteExtractor implements Transformation {
    private static final String TAG = PaletteExtractor.class.getSimpleName();
    private final Map<String, Pair<Integer, Integer>> mColorCache;


    private final String mBitmapName;

    public PaletteExtractor(String bitmapName, Map<String, Pair<Integer, Integer>> colorCache) {
        mBitmapName = bitmapName;
        mColorCache = colorCache;
    }


    @Override
    public Bitmap transform(Bitmap source) {
        Pair<Integer, Integer> colors;
        synchronized (mColorCache) {
            Log.i(TAG, "extract color from " + mBitmapName + " cache hit.");
            colors = mColorCache.get(mBitmapName);
        }
        if (colors == null) {
            final Palette palette = Palette.generate(source);
            if (palette.getMutedSwatch() != null) {
                colors = Pair.create(palette.getMutedSwatch().getRgb(), palette.getMutedSwatch().getTitleTextColor());
                synchronized (mColorCache) {
                    mColorCache.put(mBitmapName, colors);
                }
                Log.i(TAG, "extract color from " + mBitmapName + " rgb = " + String.format("%06X", colors.first & 0xFFFFFF));
            } else {
                Log.i(TAG, "extract color from " + mBitmapName + " failed.");
            }

        }
        return source;

    }

    @Override
    public String key() {
        return PaletteExtractor.class.getSimpleName();
    }
}
