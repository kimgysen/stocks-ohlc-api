package be.kpse.ohlc.repository.user_authprovider;

import be.kpse.ohlc.repository.authprovider.AuthProviderEntity;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserAuthProviderRepository extends JpaRepository<UserAuthProviderEntity, UUID> {

    Optional<UserAuthProviderEntity> findByOauthUserIdAndAuthProviderEntity(String oauthUserId, AuthProviderEntity authProviderEntity);

}
