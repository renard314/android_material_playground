package codingpractice.renard314.com.products.model.generated;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by renard on 11/04/15.
 */
public final class Inventory implements Parcelable {
    public final long max_sale_qty;
    public final long stock_status;
    public final long qty_in_carts;
    public final long qty_in_stock;
    public final long __v;

    @JsonCreator
    public Inventory(@JsonProperty("max_sale_qty") long max_sale_qty, @JsonProperty("stock_status") long stock_status, @JsonProperty("qty_in_carts") long qty_in_carts, @JsonProperty("qty_in_stock") long qty_in_stock, @JsonProperty("__v") long __v) {
        this.max_sale_qty = max_sale_qty;
        this.stock_status = stock_status;
        this.qty_in_carts = qty_in_carts;
        this.qty_in_stock = qty_in_stock;
        this.__v = __v;
    }

    protected Inventory(Parcel in) {
        max_sale_qty = in.readLong();
        stock_status = in.readLong();
        qty_in_carts = in.readLong();
        qty_in_stock = in.readLong();
        __v = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(max_sale_qty);
        dest.writeLong(stock_status);
        dest.writeLong(qty_in_carts);
        dest.writeLong(qty_in_stock);
        dest.writeLong(__v);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Inventory> CREATOR = new Parcelable.Creator<Inventory>() {
        @Override
        public Inventory createFromParcel(Parcel in) {
            return new Inventory(in);
        }

        @Override
        public Inventory[] newArray(int size) {
            return new Inventory[size];
        }
    };
}