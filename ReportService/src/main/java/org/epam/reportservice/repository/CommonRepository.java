package org.epam.reportservice.repository;

import java.util.Optional;

public interface CommonRepository<T, ID> {
    <S extends T> S save(S entity);
    Optional<T> findById(ID id);
}
