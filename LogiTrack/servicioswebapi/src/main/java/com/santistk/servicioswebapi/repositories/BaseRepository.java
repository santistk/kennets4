package com.santistk.servicioswebapi.repositories;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T, ID> {

    @Inject
    protected EntityManager entityManager;

    protected abstract Class<T> entity();

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(entityManager.find(entity(), id));
    }

    public List<T> getAll() {
        String entityName = entityManager.getMetamodel().entity(entity()).getName();

        return entityManager.createQuery(MessageFormat.format("SELECT e FROM {0} e", entityName), entity()).getResultList();
    }

    public Optional<T> save(T entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Object id = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
            if (id == null) {
                entityManager.persist(entity);
            } else {
                entityManager.merge(entity);
            }
            transaction.commit();

            return Optional.ofNullable(entity);
        } catch (Exception ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    public void delete(T entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }
}
