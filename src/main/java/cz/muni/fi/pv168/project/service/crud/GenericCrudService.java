package cz.muni.fi.pv168.project.service.crud;

import cz.muni.fi.pv168.project.model.Entity;
import cz.muni.fi.pv168.project.model.GuidProvider;
import cz.muni.fi.pv168.project.repository.Repository;
import cz.muni.fi.pv168.project.service.GeneralDependencyChecker;
import cz.muni.fi.pv168.project.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.service.validation.Validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenericCrudService <T extends Entity> implements CrudService<T> {
    private final Repository<T> entityRepository;
    private final Validator<T> entityValidator;
    private final GuidProvider guidProvider;
    private GeneralDependencyChecker<T> generalDependencyChecker;

    public GenericCrudService(Repository<T> entityRepository, Validator<T> entityValidator,
                             GuidProvider guidProvider) {
        this.entityRepository = entityRepository;
        this.entityValidator = entityValidator;
        this.guidProvider = guidProvider;
    }

    @Override
    public List<T> findAll() {
        return entityRepository.findAll();
    }

    @Override
    public ValidationResult create(T newEntity) {
        var validationResult = entityValidator.validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(guidProvider.newGuid());
        } else if (entityRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException(newEntity.getClass() + " with given guid already exists: " + newEntity.getGuid());
        }
        if (findAll().contains(newEntity)) {
            validationResult.add(newEntity + " already exists in memory");
        }
        if (validationResult.isValid()) {
            entityRepository.create(newEntity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(T entity) {
        var validationResult = entityValidator.validate(entity);
        if (validationResult.isValid()) {
            entityRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult deleteByGuid(String guid) {
        T entity = entityRepository.findByGuid(guid).get();
        var validationResult = entityValidator.validate(entity);
        var dependentEntities = generalDependencyChecker.getDependentEntities(entity);
        if (!dependentEntities.isEmpty()) {
            validationResult.add(dependentEntities.stream().map(e -> e.toString() + " is dependent on " + entity).collect(Collectors.toSet()));
        } else {
            entityRepository.deleteByGuid(guid);
        }

        return validationResult;
    }

    @Override
    public void deleteAll() {
        entityRepository.deleteAll();
    }

    @Override
    public void setGeneralDependencyChecker(GeneralDependencyChecker<T> generalDependencyChecker) {
        this.generalDependencyChecker = generalDependencyChecker;
    }

    @Override
    public Optional<T> findByGuid(String guid) {
        return entityRepository.findByGuid(guid);
    }
}
