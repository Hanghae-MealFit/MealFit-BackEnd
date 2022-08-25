package com.mealfit.admin.service;

import com.mealfit.exception.user.NoUserException;
import com.mealfit.user.domain.User;
import com.mealfit.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VaadinUserService implements VaddinService<User>{

    private final UserRepository userRepository;

    public VaadinUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findByKeyAndValue(String key, String value) {
        switch (key) {
            case "EMAIL":
                return List.of(userRepository.findByEmail(value)
                      .orElseThrow(() -> new NoUserException("찾는 회원이 없습니다.")));
            case "NICKNAME":
                return List.of(userRepository.findByNickname(value)
                      .orElseThrow(() -> new NoUserException("찾는 회원이 없습니다.")));
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public List<User> findByKeyAndId(String key, Long value) {
        if ("USER_ID".equals(key)) {
            return List.of(userRepository.findById(value)
                  .orElseThrow(() -> new NoUserException("찾는 회원이 없습니다.")));
        }
        throw new RuntimeException();
    }
}
