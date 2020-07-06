package com.edozo.java.test.datastores;

import com.edozo.java.test.exceptions.PersistenceException;
import com.edozo.java.test.exceptions.RecordNotFoundException;
import com.edozo.java.test.model.BoundingBox;
import com.edozo.java.test.model.Map;
import com.edozo.java.test.model.UpsertMap;
import com.edozo.java.test.ports.MapRepository;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class H2MapRepository implements MapRepository {

    DataSource datasource;

    @Override
    public int save(UpsertMap map) {
        String query = "INSERT INTO MAPS(USER_ID, ADDRESS, BOTTOM_LEFT_X, BOTTOM_LEFT_Y, TOP_RIGHT_X, TOP_RIGHT_Y, DOWNLOAD_URL, CREATED_AT, UPDATED_AT) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection connection = datasource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, map.getUserId());
            statement.setString(2, map.getAddress());
            statement.setDouble(3, map.getBoundingBox().getBottomLeftX());
            statement.setDouble(4, map.getBoundingBox().getBottomLeftY());
            statement.setDouble(5, map.getBoundingBox().getTopRightX());
            statement.setDouble(6, map.getBoundingBox().getTopRightY());
            statement.setString(7, map.getDownloadUrl().toString());
            statement.setTimestamp(8, Timestamp.from(Instant.now()));
            statement.setTimestamp(9, Timestamp.from(Instant.now()));
            statement.execute();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    log.info("Created new Map with id {}", id);
                    return id;
                }
            }

            log.info(String.format("Failed to persist record: %s", map));
            throw new PersistenceException(String.format("Failed to persist record: %s", map));

        } catch (SQLException e) {
            log.warn("Failed to insert new Map " + map, e);
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(int id, UpsertMap map) {
        String query = "UPDATE MAPS SET USER_ID = ?, ADDRESS = ?, BOTTOM_LEFT_X = ?, BOTTOM_LEFT_Y = ?, TOP_RIGHT_X = ?, TOP_RIGHT_Y = ?, DOWNLOAD_URL = ?, UPDATED_AT = ? WHERE ID = ?";
        try (Connection connection = datasource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, map.getUserId());
            statement.setString(2, map.getAddress());
            statement.setDouble(3, map.getBoundingBox().getBottomLeftX());
            statement.setDouble(4, map.getBoundingBox().getBottomLeftY());
            statement.setDouble(5, map.getBoundingBox().getTopRightX());
            statement.setDouble(6, map.getBoundingBox().getTopRightY());
            statement.setString(7, map.getDownloadUrl().toString());
            statement.setTimestamp(8, Timestamp.from(Instant.now()));
            statement.setInt(9, id);

            if (statement.executeUpdate() == 0) {
                throw new RecordNotFoundException(String.format("Could not find Map %s", id));
            }

        } catch (SQLException e) {
            log.warn("Failed to updated Map " + id, e);
            throw new PersistenceException(e);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = datasource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM MAPS WHERE ID = ?")) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0) {
                log.warn(String.format("Failed to delete Map %s", id));
                throw new RecordNotFoundException(String.format("Could not delete Map %s", id));
            }
            log.info("Deleted Map with id {}", id);
        } catch (SQLException e) {
            log.warn(String.format("Failed to delete Map %s", id));
            throw new PersistenceException(String.format("Failed to delete Map %s", id));
        }
    }

    @Override
    public Collection<Map> loadAll() {
        Set<Map> result = new HashSet<>();
        String query = "SELECT * FROM MAPS";
        try (Connection connection = datasource.getConnection();
             ResultSet rs = connection.prepareStatement(query).executeQuery()) {

            while (rs.next()) {
                result.add(buildMap(rs));
            }

        } catch (SQLException | MalformedURLException e) {
            throw new PersistenceException(e);
        }
        return result;
    }

    @Override
    public Map findById(int id) {
        String query = String.format("SELECT * FROM MAPS WHERE ID = '%s'", id);
        try (Connection connection = datasource.getConnection();
             ResultSet rs = connection.prepareStatement(query).executeQuery()) {
            if (rs.next()) {
                return buildMap(rs);
            }
            throw new RecordNotFoundException(String.format("Could not find Map %s", id));
        } catch (SQLException | MalformedURLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Collection<Map> findByAddress(String address) {
        Set<Map> result = new HashSet<>();
        String query = String.format("SELECT * FROM MAPS WHERE ADDRESS = '%s'", address);
        try (Connection connection = datasource.getConnection();
             ResultSet rs = connection.prepareStatement(query).executeQuery()) {

            while (rs.next()) {
                result.add(buildMap(rs));
            }

        } catch (SQLException | MalformedURLException e) {
            throw new PersistenceException(e);
        }
        return result;
    }

    private Map buildMap(ResultSet rs) throws SQLException, MalformedURLException {
        return Map.builder()
            .id(rs.getInt("ID"))
            .userId(rs.getInt("USER_ID"))
            .address(rs.getString("ADDRESS"))
            .boundingBox(BoundingBox.builder()
                .bottomLeftX(rs.getDouble("BOTTOM_LEFT_X"))
                .bottomLeftY(rs.getDouble("BOTTOM_LEFT_Y"))
                .topRightX(rs.getDouble("TOP_RIGHT_X"))
                .topRightY(rs.getDouble("TOP_RIGHT_Y"))
                .build())
            .downloadUrl(new URL(rs.getString("DOWNLOAD_URL")))
            .createdAt(rs.getTimestamp("CREATED_AT").toInstant())
            .updatedAt(rs.getTimestamp("UPDATED_AT").toInstant())
            .build();
    }
}
