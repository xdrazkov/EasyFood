package cz.muni.fi.pv168.project.storage.memory;

import cz.muni.fi.pv168.project.model.Entity;
import cz.muni.fi.pv168.project.repository.Repository;

import java.util.*;

/**
 * Generic implementation of {@link Repository} which persists entities in memory.
 * @param <T> entity type
 */
public class InMemoryRepository<T extends Entity> implements Repository<T> {

    private Map<String, T> data = new HashMap<>();

    // TODO remove completely useless
    public InMemoryRepository(Collection<T> initEntities) {
        initEntities.forEach(this::create);
    }

    public Optional<T> findByGuid(String guid) {
        if (guid == null) {
            throw new IllegalArgumentException("Guid cannot be null.");
        }
        return Optional.ofNullable(data.get(guid));
    }

    @Override
    public List<T> findAll() {
        return data.values().stream()
                .toList();
    }

    @Override
    public void create(T newEntity) {
        if (newEntity.getGuid() == null) {
            throw new IllegalArgumentException("Cannot persist entity without guid.");
        }
        data.put(newEntity.getGuid(), newEntity);

        System.out.println("[InMemoryStorage] Created entity: " + newEntity);
    }

    @Override
    public void update(T entity) {
        Optional entityOptional = findByGuid(entity.getGuid());
        if (entityOptional.isEmpty()) {
            throw new IllegalArgumentException("No existing entity found with given guid: " + entity.getGuid());
        }
        data.put(entity.getGuid(), entity);

        System.out.println("[InMemoryStorage] Updated entity: " + entity);
    }

    @Override
    public void deleteByGuid(String guid) {
        if (guid == null) {
            throw new IllegalArgumentException("Guid cannot be null.");
        }
        data.remove(guid);
        System.out.println("[InMemoryStorage] Deleted entity with guid: " + guid);
    }

    @Override
    public void deleteAll() {
        data = new HashMap<>();
    }

    @Override
    public boolean existsByGuid(String guid)
    {
        return data.containsKey(guid);
    }
}
