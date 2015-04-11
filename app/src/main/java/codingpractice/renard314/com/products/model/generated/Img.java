package codingpractice.renard314.com.products.model.generated;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by renard on 11/04/15.
 */
public final class Img implements Parcelable {
    public final long h;
    public final String name;
    public final long w;
    public final long position;

    @JsonCreator
    public Img(@JsonProperty("h") long h, @JsonProperty("name") String name, @JsonProperty("w") long w, @JsonProperty("position") long position) {
        this.h = h;
        this.name = name;
        this.w = w;
        this.position = position;
    }

    protected Img(Parcel in) {
        h = in.readLong();
        name = in.readString();
        w = in.readLong();
        position = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(h);
        dest.writeString(name);
        dest.writeLong(w);
        dest.writeLong(position);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Img> CREATOR = new Parcelable.Creator<Img>() {
        @Override
        public Img createFromParcel(Parcel in) {
            return new Img(in);
        }

        @Override
        public Img[] newArray(int size) {
            return new Img[size];
        }
    };
}