package be.kpse.ohlc.repository.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "ROLE")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoleEntity {

	@Id
	@Column(name = "role_id")
	int roleId;

	@Column(name = "role")
	String role;

}
