package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validateTransactional(User sender, BigDecimal amount) throws Exception {
        if(sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuário do tipo Lojista não está autorizado a realizar transação.");
        }

        if(sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id) throws Exception {
        Optional<User> obj = userRepository.findById(id);
        User user = obj.orElseThrow(() -> new Exception("Usuário não encontrado"));

        return new UserDTO(user);

    }

    @Transactional
    public UserDTO createUser(UserDTO data) {
        User newUser = new User(data);
        userRepository.save(newUser);
        return new UserDTO(newUser);
    }
/*    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }*/

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> usersList = userRepository.findAll();
        return usersList.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
    }
}
