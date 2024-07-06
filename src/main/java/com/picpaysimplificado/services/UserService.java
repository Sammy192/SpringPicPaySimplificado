package com.picpaysimplificado.services;

import com.picpaysimplificado.entities.User;
import com.picpaysimplificado.enums.UserType;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.repositories.UserRepository;
import com.picpaysimplificado.services.exceptions.DatabaseException;
import com.picpaysimplificado.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado.")
        );
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO createUser(UserDTO data) {
        try {
            User newUser = new User(data);
            userRepository.save(newUser);
            return new UserDTO(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Usuário já registrado no sistema.");
        }

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

    @Transactional(propagation = Propagation.SUPPORTS)
    public UserDTO update(Long id, UserDTO userDTO) {
        try {
            User userEntity = userRepository.getReferenceById(id);
            copyDtoToEntity(userDTO, userEntity);

            userEntity = userRepository.save(userEntity);
            return new UserDTO(userEntity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado.");
        }
        catch (ConstraintViolationException e) {
            throw new DatabaseException("[CONSTRAINT] Falha de integridade referencial:) " + e.getMessage());
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("[INTEGRITY] Falha de integridade referencial");
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            userRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(UserDTO userDTO, User userEntity) {
        userEntity.setFirstName(userDTO.firstName());
        userEntity.setLastName(userDTO.lastName());
        userEntity.setDocument(userDTO.document());
        userEntity.setEmail(userDTO.email());
        userEntity.setPassword(userDTO.password());
        userEntity.setBalance(userDTO.balance());
        userEntity.setUserType(userDTO.userType());

    }
}
