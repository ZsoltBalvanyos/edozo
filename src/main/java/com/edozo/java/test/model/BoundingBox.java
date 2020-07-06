package com.edozo.java.test.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class BoundingBox {
    double bottomLeftX;
    double bottomLeftY;
    double topRightX;
    double topRightY;
}
