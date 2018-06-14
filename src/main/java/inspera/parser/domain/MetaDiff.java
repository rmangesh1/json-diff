package inspera.parser.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Created by rmang on 14-06-2018.
 */
public class MetaDiff {

    private String field;

    @JsonProperty("before")
    private String beforeValue;

    @JsonProperty("after")
    private String afterValue;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }

    public String getAfterValue() {
        return afterValue;
    }

    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaDiff metaDiff = (MetaDiff) o;
        return Objects.equals(field, metaDiff.field) &&
                Objects.equals(beforeValue, metaDiff.beforeValue) &&
                Objects.equals(afterValue, metaDiff.afterValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, beforeValue, afterValue);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MetaDiff{");
        sb.append("field='").append(field).append('\'');
        sb.append(", beforeValue='").append(beforeValue).append('\'');
        sb.append(", afterValue='").append(afterValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
