package com.edozo.java.test.model;

import java.net.URL;
import java.time.Instant;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class Map {
    int id;
    int userId;
    String address;
    BoundingBox boundingBox;
    Instant createdAt;
    Instant updatedAt;
    URL downloadUrl;
}
