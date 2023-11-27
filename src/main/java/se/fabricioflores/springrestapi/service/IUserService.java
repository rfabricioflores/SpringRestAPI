package se.fabricioflores.springrestapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import se.fabricioflores.springrestapi.dto.AddUserReq;
import se.fabricioflores.springrestapi.dto.UserRegisterReq;
import se.fabricioflores.springrestapi.model.User;

public interface IUserService extends UserDetailsService {
    User save(AddUserReq addUserReq) throws RuntimeException;
    User registerUser(UserRegisterReq userRegisterReq);
}