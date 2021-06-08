package com.epam.esm.repository.query;

import java.util.Collections;
import java.util.Map;

public interface NativeQuery {

    String getQuery();

    default Map<String, Object> getParams() {
        return Collections.emptyMap();
    }

    default String getCountQuery() {
        return String.format("SELECT count(*) FROM %s", getQuery());
    }
}
