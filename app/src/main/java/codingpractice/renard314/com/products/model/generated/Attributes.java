package codingpractice.renard314.com.products.model.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by renard on 11/04/15.
 */
public final class Attributes {
    public final Dag dag[];

    @JsonCreator
    public Attributes(@JsonProperty("dag") Dag[] dag) {
        this.dag = dag;
    }

}
