package be.kpse.ohlc.repository.user;

import be.kpse.ohlc.repository.BaseEntity;
import be.kpse.ohlc.repository.role.RoleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@Table(name = "USER")
@Entity
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
public class UserEntity extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "username")
	private String username;

	@Column(name = "email")
	private String email;

	@Column(name = "is_active")
	private boolean active;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "USER_ROLE",
			joinColumns = {@JoinColumn(name = "user_id")},
			inverseJoinColumns = {@JoinColumn(name = "role_id")}
	)
	private Set<RoleEntity> roles;

}
