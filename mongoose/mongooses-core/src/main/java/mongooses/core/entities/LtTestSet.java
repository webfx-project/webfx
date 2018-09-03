package mongooses.core.entities;

import java.time.Instant;

/**
 * @author Jean-Pierre Alonso.
 */
public interface LtTestSet {

    Instant getDate();

    void setDate(Instant date);

    String getName();

    void setName(String name);

    String getComment();

    void setComment(String comment);
}
