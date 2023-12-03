package se.fabricioflores.springrestapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import se.fabricioflores.springrestapi.dto.AddUserReq;
import se.fabricioflores.springrestapi.dto.UserRegisterReq;
import se.fabricioflores.springrestapi.model.User;

import java.util.Optional;

public interface IUserService extends UserDetailsService {
    User createUser(AddUserReq addUserReq) throws RuntimeException;
    User registerUser(UserRegisterReq userRegisterReq);
    Optional<User> getUser(String username);
}