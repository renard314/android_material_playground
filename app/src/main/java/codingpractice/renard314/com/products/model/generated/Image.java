package codingpractice.renard314.com.products.model.generated;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by renard on 11/04/15.
 */
public final class Image implements Parcelable {
    public final long h;
    public final long w;
    public final String name;
    public final long position;

    @JsonCreator
    public Image(@JsonProperty("h") long h, @JsonProperty("w") long w, @JsonProperty("name") String name, @JsonProperty("position") long position) {
        this.h = h;
        this.w = w;
        this.name = name;
        this.position = position;
    }

    protected Image(Parcel in) {
        h = in.readLong();
        w = in.readLong();
        name = in.readString();
        position = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(h);
        dest.writeLong(w);
        dest.writeString(name);
        dest.writeLong(position);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}