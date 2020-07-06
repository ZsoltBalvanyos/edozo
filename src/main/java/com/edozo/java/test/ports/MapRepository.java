package com.edozo.java.test.ports;

import com.edozo.java.test.model.Map;
import com.edozo.java.test.model.UpsertMap;
import java.util.Collection;

public interface MapRepository {
    int save(UpsertMap map);
    void update(int id, UpsertMap map);
    void delete(int id);
    Collection<Map> loadAll();
    Map findById(int id);
    Collection<Map> findByAddress(String address);
}
