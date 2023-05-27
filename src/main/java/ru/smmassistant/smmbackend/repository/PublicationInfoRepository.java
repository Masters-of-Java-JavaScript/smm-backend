package ru.smmassistant.smmbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.smmassistant.smmbackend.model.PublicationInfo;

public interface PublicationInfoRepository extends JpaRepository<PublicationInfo, Long> {
}
