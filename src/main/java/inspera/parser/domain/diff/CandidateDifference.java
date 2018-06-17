package inspera.parser.domain.diff;

import java.util.List;
import java.util.Objects;

/**
 * Created by rmang on 14-06-2018.
 */
public class CandidateDifference {

    private List<CandidateIdentifier> editedCandidates;

    private List<CandidateIdentifier> addedCandidates;

    private List<CandidateIdentifier> removedCandidates;

    public List<CandidateIdentifier> getEditedCandidates() {
        return editedCandidates;
    }

    public void setEditedCandidates(List<CandidateIdentifier> editedCandidates) {
        this.editedCandidates = editedCandidates;
    }

    public List<CandidateIdentifier> getAddedCandidates() {
        return addedCandidates;
    }

    public void setAddedCandidates(List<CandidateIdentifier> addedCandidates) {
        this.addedCandidates = addedCandidates;
    }

    public List<CandidateIdentifier> getRemovedCandidates() {
        return removedCandidates;
    }

    public void setRemovedCandidates(List<CandidateIdentifier> removedCandidates) {
        this.removedCandidates = removedCandidates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CandidateDifference that = (CandidateDifference) o;
        return Objects.equals(editedCandidates, that.editedCandidates) &&
                Objects.equals(addedCandidates, that.addedCandidates) &&
                Objects.equals(removedCandidates, that.removedCandidates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(editedCandidates, addedCandidates, removedCandidates);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CandidateDifference{");
        sb.append("editedCandidates=").append(editedCandidates);
        sb.append(", addedCandidates=").append(addedCandidates);
        sb.append(", removedCandidates=").append(removedCandidates);
        sb.append('}');
        return sb.toString();
    }
}
