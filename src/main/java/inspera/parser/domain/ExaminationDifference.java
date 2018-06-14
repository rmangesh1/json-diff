package inspera.parser.domain;

import java.util.List;
import java.util.Objects;

/**
 * Created by rmang on 14-06-2018.
 */
public class ExaminationDifference {

    private List<MetaDiff> metaDiff;

    private CandidateDifference candidateDifference;

    public List<MetaDiff> getMetaDiff() {
        return metaDiff;
    }

    public void setMetaDiff(List<MetaDiff> metaDiff) {
        this.metaDiff = metaDiff;
    }

    public CandidateDifference getCandidateDifference() {
        return candidateDifference;
    }

    public void setCandidateDifference(CandidateDifference candidateDifference) {
        this.candidateDifference = candidateDifference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExaminationDifference that = (ExaminationDifference) o;
        return Objects.equals(metaDiff, that.metaDiff) &&
                Objects.equals(candidateDifference, that.candidateDifference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metaDiff, candidateDifference);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExaminationDifference{");
        sb.append("metaDiff=").append(metaDiff);
        sb.append(", candidateDifference=").append(candidateDifference);
        sb.append('}');
        return sb.toString();
    }
}
