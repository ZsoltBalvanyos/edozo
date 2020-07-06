package com.edozo.java.test.model;

import java.net.URL;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.hibernate.validator.constraints.Range;

@Value
@Builder
@With
public class UpsertMap {
    @Range(min = 1, message = "User Id must be present") int userId;
    @NotBlank(message = "Address must be present") String address;
    @NotNull(message = "Bounding Box must be present") BoundingBox boundingBox;
    @NotNull(message = "Download Url must be present") URL downloadUrl;
}
