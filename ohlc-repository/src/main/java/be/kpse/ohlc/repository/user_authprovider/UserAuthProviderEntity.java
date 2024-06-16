package be.kpse.ohlc.repository.user_authprovider;

import be.kpse.ohlc.repository.authprovider.AuthProviderEntity;
import be.kpse.ohlc.repository.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;
import jakarta.persistence.*;

@Table(name = "USER_AUTHPROVIDER")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class UserAuthProviderEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_provider_id")
    private UUID userAuthProviderId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auth_provider_id")
    private AuthProviderEntity authProviderEntity;

    @Column(name = "oauth_user_id")
    private String oauthUserId;

}
