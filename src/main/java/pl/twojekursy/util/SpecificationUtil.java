package pl.twojekursy.util;

import jakarta.persistence.criteria.CriteriaQuery;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SpecificationUtil {
    public boolean isCountQuery(CriteriaQuery<?> query) {
        return query.getResultType() == Long.class || query.getResultType() == long.class;
    }
}
