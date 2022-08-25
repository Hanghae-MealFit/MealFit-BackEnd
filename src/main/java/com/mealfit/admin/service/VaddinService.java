package com.mealfit.admin.service;

import java.util.List;
import java.util.Optional;

public interface VaddinService<T> {

    void deleteById(Long id);

    T save(T t);

    default Optional<T> findById(Long id) {
        return Optional.empty();
    }

    List<T> findAll();

    List<T> findByKeyAndValue(String key, String value);

    List<T> findByKeyAndId(String key, Long id);
}
