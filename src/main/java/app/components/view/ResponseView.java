package app.components.view;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Common response view
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseView {
    public Object data;

    public ResponseView(Object data) {
        this.data = data;
    }

    //для jackson
    public ResponseView() {

    }

    @Override
    public String toString() {
        return "ResponseView{" +
                "data=" + data +
                '}';
    }
}