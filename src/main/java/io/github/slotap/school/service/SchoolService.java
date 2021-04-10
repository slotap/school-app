package io.github.slotap.school.service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SchoolService<T,S> {

    List<T> getAll(Pageable pageable);

    T save(S var1);

    void delete(long id);

    Optional<T> getDtoData(long id);

    Optional<S> getData(long id);

    boolean existById(long id);

    List<T> findByName(String var1, String var2);


}
