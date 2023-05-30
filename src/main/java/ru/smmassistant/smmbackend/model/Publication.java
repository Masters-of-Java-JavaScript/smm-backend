package ru.smmassistant.smmbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "smmassistant", name = "publication")
@Builder
@Entity
@ToString(exclude = "publicationInfoList")
@EqualsAndHashCode(exclude = "publicationInfoList")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer userId;

    private OffsetDateTime publishDate;

    private String message;

    private String attachments;

    @OneToMany(mappedBy = "publication")
    @Builder.Default
    private List<PublicationInfo> publicationInfoList = new ArrayList<>();
}
