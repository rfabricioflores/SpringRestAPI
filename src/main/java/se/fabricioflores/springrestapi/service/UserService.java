package se.fabricioflores.springrestapi.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.fabricioflores.springrestapi.dto.AddUserReq;
import se.fabricioflores.springrestapi.dto.UserRegisterReq;
import se.fabricioflores.springrestapi.model.Role;
import se.fabricioflores.springrestapi.model.User;
import se.fabricioflores.springrestapi.repo.RoleRepo;
import se.fabricioflores.springrestapi.repo.UserRepo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ** User details service interface method implementation for retrieving user details
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo
                .findOneByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles())
        );
    }

    // ** Create new user entities with whatever roles you please, this is mainly used by admins
    @Override
    public User createUser(@Valid AddUserReq addUserReq) throws RuntimeException {
        var optUser = userRepo.findOneByUsername(addUserReq.username());
        if(optUser.isPresent()) throw new RuntimeException("User already exists");

        User user = new User();
        user.setUsername(addUserReq.username());
        user.setPassword(passwordEncoder.encode(addUserReq.password()));

        Set<Role> roles = new HashSet<>();

        addUserReq.roles().forEach(r -> {
            var optRole = roleRepo.findRoleByName(r);
            optRole.ifPresent(roles::add);
        });

        user.setRoles(roles);

        return userRepo.save(user);
    }

    // ** Register new users, by default new users get the role "USER"
    @Override
    public User registerUser(@Valid UserRegisterReq userRegisterReq) {
        User user = new User();
        user.setUsername(userRegisterReq.username());
        user.setPassword(passwordEncoder.encode(userRegisterReq.password()));

        Role role = roleRepo.findRoleByName("USER").orElseThrow();

        user.setRoles(Set.of(role));

        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUser(String username) {
        return userRepo.findOneByUsername(username);
    }

    // ** Convert role entities to authorities
    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
