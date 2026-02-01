package com.billetera_virtual.security.infra.adapter;

import com.billetera_virtual.security.infra.model.UserPrincipal;
import com.billetera_virtual.transaction.domain.port.external.AccountExternalPort;
import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryPort userRepository;
    private final AccountExternalPort accountExternal;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No hay coincidencias con el email ingresado"));

        Long accountId = null;
        // Si el rol es Admin entonces NO busco la Account, ya que no va a tener
        if (!user.getRole().equals(User.Role.ADMIN)) {
            System.out.println("Entra al condicional");
            accountId = accountExternal.getAccountIdByUserId(user.getId());
        }

        return UserPrincipal.builder()
                .id(user.getId())
                .accountId(accountId)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .status(user.isStatus())
                .build();
    }

}
