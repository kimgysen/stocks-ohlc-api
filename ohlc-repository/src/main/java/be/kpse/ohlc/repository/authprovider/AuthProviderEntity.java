package be.kpse.ohlc.repository.authprovider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "AUTHPROVIDER")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthProviderEntity {

    @Id
    @Column(name = "auth_provider_id")
    int authProviderId;

    @Column(name = "auth_provider")
    String authProvider;

}
