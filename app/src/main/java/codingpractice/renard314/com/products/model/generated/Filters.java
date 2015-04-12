package codingpractice.renard314.com.products.model.generated;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by renard on 11/04/15.
 */
public final class Filters implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Filters> CREATOR = new Parcelable.Creator<Filters>() {
        @Override
        public Filters createFromParcel(Parcel in) {
            return new Filters(in);
        }

        @Override
        public Filters[] newArray(int size) {
            return new Filters[size];
        }
    };
    public final String brand_name;
    public final String brand_uri;
    public final long frequency;
    public final long is_organic;
    public final String mfr_name;
    public final String vendor_name;

    @JsonCreator
    public Filters(@JsonProperty("brand_name") String brand_name, @JsonProperty("brand_uri") String brand_uri, @JsonProperty("frequency") long frequency, @JsonProperty("is_organic") long is_organic, @JsonProperty("mfr_name") String mfr_name, @JsonProperty("vendor_name") String vendor_name) {
        this.brand_name = brand_name;
        this.brand_uri = brand_uri;
        this.frequency = frequency;
        this.is_organic = is_organic;
        this.mfr_name = mfr_name;
        this.vendor_name = vendor_name;
    }

    protected Filters(Parcel in) {
        brand_name = in.readString();
        brand_uri = in.readString();
        frequency = in.readLong();
        is_organic = in.readLong();
        mfr_name = in.readString();
        vendor_name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brand_name);
        dest.writeString(brand_uri);
        dest.writeLong(frequency);
        dest.writeLong(is_organic);
        dest.writeString(mfr_name);
        dest.writeString(vendor_name);
    }
}
