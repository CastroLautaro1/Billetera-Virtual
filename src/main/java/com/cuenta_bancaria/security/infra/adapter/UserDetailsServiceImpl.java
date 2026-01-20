package com.cuenta_bancaria.security.infra.adapter;

import com.cuenta_bancaria.security.infra.model.UserPrincipal;
import com.cuenta_bancaria.transaction.domain.port.external.AccountExternalPort;
import com.cuenta_bancaria.user.domain.User;
import com.cuenta_bancaria.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
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

        Long accountId = accountExternal.getAccountIdByUserId(user.getId());

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
