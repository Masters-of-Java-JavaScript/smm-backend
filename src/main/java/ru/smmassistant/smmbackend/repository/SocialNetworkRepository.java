package ru.smmassistant.smmbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.smmassistant.smmbackend.model.SocialNetwork;

public interface SocialNetworkRepository extends JpaRepository<SocialNetwork, Long> {

    SocialNetwork findByUserId(Integer userId);
}
