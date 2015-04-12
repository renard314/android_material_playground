package codingpractice.renard314.com.products.model.generated;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by renard on 11/04/15.
 */
public final class Pricing implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Pricing> CREATOR = new Parcelable.Creator<Pricing>() {
        @Override
        public Pricing createFromParcel(Parcel in) {
            return new Pricing(in);
        }

        @Override
        public Pricing[] newArray(int size) {
            return new Pricing[size];
        }
    };
    public final long price;
    public final long promo_price;
    public final String savings_text;
    public final long on_sale;

    @JsonCreator
    public Pricing(@JsonProperty("price") long price, @JsonProperty("promo_price") long promo_price, @JsonProperty("savings_text") String savings_text, @JsonProperty("on_sale") long on_sale) {
        this.price = price;
        this.promo_price = promo_price;
        this.savings_text = savings_text;
        this.on_sale = on_sale;
    }

    protected Pricing(Parcel in) {
        price = in.readLong();
        promo_price = in.readLong();
        savings_text = in.readString();
        on_sale = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(price);
        dest.writeLong(promo_price);
        dest.writeString(savings_text);
        dest.writeLong(on_sale);
    }
}