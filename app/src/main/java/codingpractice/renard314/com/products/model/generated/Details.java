package codingpractice.renard314.com.products.model.generated;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by renard on 11/04/15.
 */
public final class Details implements Parcelable {
    public final long prod_type;
    public final String uri;
    public final long status;
    public final long is_new;
    public final String storage_class;

    @JsonCreator
    public Details(@JsonProperty("prod_type") long prod_type, @JsonProperty("uri") String uri, @JsonProperty("status") long status, @JsonProperty("is_new") long is_new, @JsonProperty("storage_class") String storage_class) {
        this.prod_type = prod_type;
        this.uri = uri;
        this.status = status;
        this.is_new = is_new;
        this.storage_class = storage_class;
    }

    protected Details(Parcel in) {
        prod_type = in.readLong();
        uri = in.readString();
        status = in.readLong();
        is_new = in.readLong();
        storage_class = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(prod_type);
        dest.writeString(uri);
        dest.writeLong(status);
        dest.writeLong(is_new);
        dest.writeString(storage_class);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Details> CREATOR = new Parcelable.Creator<Details>() {
        @Override
        public Details createFromParcel(Parcel in) {
            return new Details(in);
        }

        @Override
        public Details[] newArray(int size) {
            return new Details[size];
        }
    };
}