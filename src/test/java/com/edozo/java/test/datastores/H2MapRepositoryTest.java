package com.edozo.java.test.datastores;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.edozo.java.test.exceptions.RecordNotFoundException;
import com.edozo.java.test.model.Map;
import com.edozo.java.test.model.UpsertMap;
import com.edozo.java.test.ports.MapRepository;
import java.util.Collection;
import java.util.List;
import javax.sql.DataSource;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;

public class H2MapRepositoryTest {
    EasyRandom random = new EasyRandom();

    DataSource dataSource = DataSourceBuilder
        .create()
        .url("jdbc:h2:mem:maps;Mode=MySQL;DB_CLOSE_DELAY=-1;INIT=runscript from './src/main/resources/scripts/data.sql'")
        .username("sa")
        .password("")
        .build();

    MapRepository mapRepository = new H2MapRepository(dataSource);

    @Test
    public void givenThreeMaps_whenLoadingAll_thenAllThreeFetched() {
        assertThat(mapRepository.loadAll()).hasSizeGreaterThanOrEqualTo(4);
    }

    @Test
    public void givenMap_whenSaved_thenCanBeFetchedUpdatedDeletedById() {
        UpsertMap upsertMap = random.nextObject(UpsertMap.class);

        int id = mapRepository.save(upsertMap);
        Map map = mapRepository.findById(id);

        assertThat(map.getAddress()).isEqualTo(upsertMap.getAddress());
        assertThat(map.getBoundingBox()).isEqualTo(upsertMap.getBoundingBox());
        assertThat(map.getDownloadUrl()).isEqualTo(upsertMap.getDownloadUrl());
        assertThat(map.getUserId()).isEqualTo(upsertMap.getUserId());

        UpsertMap updatedUpsertMap = random.nextObject(UpsertMap.class);
        mapRepository.update(id, updatedUpsertMap);
        Map updatedMap = mapRepository.findById(id);

        assertThat(updatedMap.getAddress()).isEqualTo(updatedUpsertMap.getAddress());
        assertThat(updatedMap.getBoundingBox()).isEqualTo(updatedUpsertMap.getBoundingBox());
        assertThat(updatedMap.getDownloadUrl()).isEqualTo(updatedUpsertMap.getDownloadUrl());
        assertThat(updatedMap.getUserId()).isEqualTo(updatedUpsertMap.getUserId());

        assertThat(map.getCreatedAt()).isEqualTo(updatedMap.getCreatedAt());
        assertThat(map.getUpdatedAt()).isBefore(updatedMap.getUpdatedAt());

        mapRepository.delete(id);

        assertThatThrownBy(() -> mapRepository.findById(id))
            .isInstanceOf(RecordNotFoundException.class)
            .hasMessage(String.format("Could not find Map %s", id));
    }

    @Test
    public void givenMap_whenSaved_thenCanBeFetchedByAddress() {
        UpsertMap upsertMap = random.nextObject(UpsertMap.class);

        mapRepository.save(upsertMap);
        Collection<Map> maps = mapRepository.findByAddress(upsertMap.getAddress());

        assertThat(maps).hasSize(1);

        Map map = List.copyOf(maps).get(0);

        assertThat(map.getAddress()).isEqualTo(upsertMap.getAddress());
        assertThat(map.getBoundingBox()).isEqualTo(upsertMap.getBoundingBox());
        assertThat(map.getDownloadUrl()).isEqualTo(upsertMap.getDownloadUrl());
        assertThat(map.getUserId()).isEqualTo(upsertMap.getUserId());
    }
}
