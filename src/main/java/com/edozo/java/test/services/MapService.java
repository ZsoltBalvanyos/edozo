package com.edozo.java.test.services;

import com.edozo.java.test.model.Map;
import com.edozo.java.test.model.UpsertMap;
import com.edozo.java.test.ports.MapRepository;
import java.util.Collection;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MapService {

    MapRepository mapRepository;

    public int save(UpsertMap map){
        return mapRepository.save(map);
    }

    public void update(int id, UpsertMap map){
        mapRepository.update(id, map);
    }

    public void delete(int id){
        mapRepository.delete(id);
    }

    public Collection<Map> loadAll(){
        return mapRepository.loadAll();
    }

    public Map findById(int id){
        return mapRepository.findById(id);
    }

    public Collection<Map> findByAddress(String address){
        return mapRepository.findByAddress(address);
    }

}
