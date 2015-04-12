package codingpractice.renard314.com.products.model.generated;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by renard on 11/04/15.
 */
public final class Measure implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Measure> CREATOR = new Parcelable.Creator<Measure>() {
        @Override
        public Measure createFromParcel(Parcel in) {
            return new Measure(in);
        }

        @Override
        public Measure[] newArray(int size) {
            return new Measure[size];
        }
    };
    public final String wt_or_vol;

    @JsonCreator
    public Measure(@JsonProperty("wt_or_vol") String wt_or_vol) {
        this.wt_or_vol = wt_or_vol;
    }

    protected Measure(Parcel in) {
        wt_or_vol = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wt_or_vol);
    }
}