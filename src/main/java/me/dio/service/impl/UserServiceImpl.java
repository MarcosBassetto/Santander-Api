package me.dio.service.impl;


import me.dio.domain.model.User;
import me.dio.domain.repository.UserRepository;
import me.dio.service.UserService;
import me.dio.service.exception.BusinessException;
import me.dio.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
public class UserServiceImpl implements UserService {

    private static final Long UNCHANGEABLE_USER_ID = 1L;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return this.userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<User> findByName(String name) {
        return this.userRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public User create(User userToCreate) {
        ofNullable(userToCreate).orElseThrow(() -> new BusinessException("User to create must not be null."));
        ofNullable(userToCreate.getAccount()).orElseThrow(() -> new BusinessException("User account must not be null."));
        ofNullable(userToCreate.getCard()).orElseThrow(() -> new BusinessException("User card must not be null."));

        this.validateChangeableId(userToCreate.getId(), "...");
        return this.userRepository.save(userToCreate);
    }

    @Transactional
    public User update(Long id, User userToUpdate) {
        var user = this.findById(id);

        user.setName(userToUpdate.getName());
        user.setAccount(userToUpdate.getAccount());
        user.setCard(userToUpdate.getCard());
        user.setFeatures(userToUpdate.getFeatures());
        user.setNews(userToUpdate.getNews());

        return this.userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        this.userRepository.deleteById(id);
    }

    private void validateChangeableId(Long id, String message) {
        if (UNCHANGEABLE_USER_ID.equals(id)) {
            throw new BusinessException(message);
        }
    }
}