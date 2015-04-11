package codingpractice.renard314.com.products.model.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ProductsResponse {
    public final long total;
    public final long page_size;
    public final String session;
    public final long page;
    public final Status status;
    public final Product products[];
    public final Filters filters;

    @JsonCreator
    public ProductsResponse(@JsonProperty("total") long total, @JsonProperty("page_size") long page_size, @JsonProperty("session") String session, @JsonProperty("page") long page, @JsonProperty("status") Status status, @JsonProperty("products") Product[] products, @JsonProperty("filters") Filters filters){
        this.total = total;
        this.page_size = page_size;
        this.session = session;
        this.page = page;
        this.status = status;
        this.products = products;
        this.filters = filters;
    }

    public static final class Status {
        public final String msg;
        public final long code;

        @JsonCreator
        public Status(@JsonProperty("msg") String msg, @JsonProperty("code") long code){
            this.msg = msg;
            this.code = code;
        }
    }

    public static final class Filters {
        public final Option options[];
        public final Toggle toggles[];

        @JsonCreator
        public Filters(@JsonProperty("options") Option[] options, @JsonProperty("toggles") Toggle[] toggles){
            this.options = options;
            this.toggles = toggles;
        }

        public static final class Option {
            public final String name;
            public final String uri;
            public final Type types[];

            @JsonCreator
            public Option(@JsonProperty("name") String name, @JsonProperty("uri") String uri, @JsonProperty("types") Type[] types){
                this.name = name;
                this.uri = uri;
                this.types = types;
            }

            public static final class Type {
                public final String uri;
                public final String name;
                public final long count;

                @JsonCreator
                public Type(@JsonProperty("uri") String uri, @JsonProperty("name") String name, @JsonProperty("count") long count){
                    this.uri = uri;
                    this.name = name;
                    this.count = count;
                }
            }
        }

        public static final class Toggle {
            public final String name;
            public final String uri;

            @JsonCreator
            public Toggle(@JsonProperty("name") String name, @JsonProperty(value="uri", required=false) String uri){
                this.name = name;
                this.uri = uri;
            }
        }
    }
}