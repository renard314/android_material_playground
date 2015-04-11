package codingpractice.renard314.com.products.model.generated;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by renard on 11/04/15.
 */
public final class Product implements Parcelable {
    public final Attributes attributes;
    public final int[] categories;
    public final String desc;
    public final Details details;
    public final Filters filters;
    public final long id;
    public final Image images[];
    public final Img img;
    public final Inventory inventory;
    public final Measure measure;
    public final Pricing pricing;
    public final Promotion promotions[];
    public final String sku;
    public final String title;
    public final int[] types;

    @JsonCreator
    public Product(@JsonProperty("attributes") Attributes attributes, @JsonProperty("categories") int[] categories, @JsonProperty("desc") String desc, @JsonProperty("details") Details details, @JsonProperty("filters") Filters filters, @JsonProperty("id") long id, @JsonProperty("images") Image[] images, @JsonProperty("img") Img img, @JsonProperty("inventory") Inventory inventory, @JsonProperty("measure") Measure measure, @JsonProperty("pricing") Pricing pricing, @JsonProperty("promotions") Promotion[] promotions, @JsonProperty("sku") String sku, @JsonProperty("title") String title, @JsonProperty("types") int[] types) {
        this.attributes = attributes;
        this.categories = categories;
        this.desc = desc;
        this.details = details;
        this.filters = filters;
        this.id = id;
        this.images = images;
        this.img = img;
        this.inventory = inventory;
        this.measure = measure;
        this.pricing = pricing;
        this.promotions = promotions;
        this.sku = sku;
        this.title = title;
        this.types = types;
    }


    protected Product(Parcel in) {
        attributes = null;
        categories = null;
        images = (Image[]) in.readArray(Image.class.getClassLoader());
        promotions = null;
        types = in.createIntArray();

        desc = in.readString();
        details = (Details) in.readValue(Details.class.getClassLoader());
        filters = (Filters) in.readValue(Filters.class.getClassLoader());
        id = in.readLong();
        img = (Img) in.readValue(Img.class.getClassLoader());
        inventory = (Inventory) in.readValue(Inventory.class.getClassLoader());
        measure = (Measure) in.readValue(Measure.class.getClassLoader());
        pricing = (Pricing) in.readValue(Pricing.class.getClassLoader());
        sku = in.readString();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(images);
        dest.writeIntArray(types);
        dest.writeString(desc);
        dest.writeValue(details);
        dest.writeValue(filters);
        dest.writeLong(id);
        dest.writeValue(img);
        dest.writeValue(inventory);
        dest.writeValue(measure);
        dest.writeValue(pricing);
        dest.writeString(sku);
        dest.writeString(title);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}