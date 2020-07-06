package com.edozo.java.test.datastores;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.edozo.java.test.model.Map;
import com.edozo.java.test.model.UpsertMap;
import com.edozo.java.test.ports.MapRepository;
import com.edozo.java.test.services.MapService;
import java.util.Set;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

public class MapServiceTest {

    EasyRandom random = new EasyRandom();
    Map map1 = random.nextObject(Map.class);
    Map map2 = random.nextObject(Map.class);
    MapRepository mapRepository = mock(MapRepository.class);
    MapService mapService = new MapService(mapRepository);

    @Test
    public void save() {
        UpsertMap upsertMap = random.nextObject(UpsertMap.class);
        when(mapRepository.save(any())).thenReturn(1);
        mapService.save(upsertMap);
        assertThat(mapRepository.save(upsertMap)).isEqualTo(1);
    }

    @Test
    public void update() {
        UpsertMap upsertMap = random.nextObject(UpsertMap.class);
        doNothing().when(mapRepository).update(anyInt(), any());
        mapService.update(1, upsertMap);
        verify(mapRepository, times(1)).update(eq(1), eq(upsertMap));
    }

    @Test
    public void delete() {
        doNothing().when(mapRepository).delete(anyInt());
        mapService.delete(1);
        verify(mapRepository, times(1)).delete(1);
    }

    @Test
    public void loadAll() {
        when(mapRepository.loadAll()).thenReturn(Set.of(map1, map2));
        assertThat(mapService.loadAll()).containsExactlyInAnyOrderElementsOf(Set.of(map1, map2));
    }

    @Test
    public void findById() {
        when(mapRepository.findById(eq(map1.getId()))).thenReturn(map1);
        assertThat(mapService.findById(map1.getId())).isEqualTo(map1);
    }

    @Test
    public void findByAddress() {
        when(mapRepository.findByAddress(eq(map1.getAddress()))).thenReturn(Set.of(map1));
        assertThat(mapService.findByAddress(map1.getAddress())).isEqualTo(Set.of(map1));
    }
}
