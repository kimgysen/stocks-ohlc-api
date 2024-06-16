package be.kpse.ohlc.repository.user;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUserIdAndActive(UUID userId, boolean isActive);
    Optional<UserEntity> findByUsernameAndActive(String nickName, boolean isActive);
    Optional<UserEntity> findByUsername(String nickName);

    Boolean existsByUsername(String nickName);

}
