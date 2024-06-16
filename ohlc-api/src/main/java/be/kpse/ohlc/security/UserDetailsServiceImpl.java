package be.kpse.ohlc.security;

import be.kpse.ohlc.repository.user.UserEntity;
import be.kpse.ohlc.repository.user.UserRepository;
import be.kpse.security.UserPrincipal;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Component
@Import(UserRepository.class)
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> userEntityOpt = userRepository.findById(UUID.fromString(username));

		if (userEntityOpt.isPresent()) {
			UserEntity userEntity = userEntityOpt.get();
			List<GrantedAuthority> grantedAuthorities = userEntity
					.getRoles()
					.stream()
					.map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getRole()))
					.collect(toList());

			return new UserPrincipal(
					userEntity.getUserId().toString(),
					"",
					grantedAuthorities);

		} else {
			throw new UsernameNotFoundException(
					format("User: %s, not found", username));
		}
	}
}
