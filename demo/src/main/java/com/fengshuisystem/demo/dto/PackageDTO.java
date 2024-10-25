package com.fengshuisystem.demo.dto;
import com.fengshuisystem.demo.entity.Bill;
import com.fengshuisystem.demo.entity.ConsultationRequest;
import com.fengshuisystem.demo.entity.Post;
import com.fengshuisystem.demo.entity.enums.Status;
import lombok.*;
        import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PackageDTO {
    private Integer id;
    private String packageName;
    private BigDecimal price;
    private String description;
    private Status status;
    private Instant createdDate = Instant.now();
    private String createdBy;
    private Instant updatedDate = Instant.now();
    private String updatedBy;


}
