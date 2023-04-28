package ru.smmassistant.smmbackend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.smmassistant.smmbackend.model.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

    List<Publication> findAllByUserId(Integer userId);
}
