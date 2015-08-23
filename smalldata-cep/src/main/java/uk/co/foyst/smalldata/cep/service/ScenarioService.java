package uk.co.foyst.smalldata.cep.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.foyst.smalldata.cep.Scenario;
import uk.co.foyst.smalldata.cep.ScenarioId;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;
import uk.co.foyst.smalldata.cep.dao.ScenarioView;
import uk.co.foyst.smalldata.cep.dao.ScenarioViewDao;
import uk.co.foyst.smalldata.cep.dao.ScenarioViewFactory;

import java.util.List;

@Service
public class ScenarioService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String NOT_EXISTS_MESSAGE = "Scenario with Id '%s' does not exist.";

    private final ScenarioViewDao scenarioViewDao;
    private final ScenarioViewFactory scenarioViewFactory;
    private final ScenarioValidator scenarioValidator;
    private final CEPAdapter cepAdapter;

    @Autowired
    public ScenarioService(final ScenarioViewDao scenarioViewDao, final ScenarioViewFactory scenarioViewFactory, final ScenarioValidator scenarioValidator, final CEPAdapter cepAdapter) {

        this.scenarioViewDao = scenarioViewDao;
        this.scenarioViewFactory = scenarioViewFactory;
        this.scenarioValidator = scenarioValidator;
        this.cepAdapter = cepAdapter;
    }

    public List<Scenario> readAll() throws Exception {

        return scenarioViewFactory.convertToDomainObject(scenarioViewDao.findAll());
    }

    public Scenario create(final Scenario scenario) throws Exception {

        log.info("add: scenario={}", scenario);

        scenarioValidator.verifyScenarioNameIsUnique(scenario);

        cepAdapter.define(scenario);

        final ScenarioView scenarioView = scenarioViewFactory.build(scenario);
        log.info("add: scenarioView={}", scenarioView);
        scenarioViewDao.save(scenarioView);

        log.info("add: Returning: {}", scenario);
        return scenario;
    }

    public Scenario read(final ScenarioId id) throws Exception {

        final String scenarioId = id.toString();
        final ScenarioView scenarioView = scenarioViewDao.findOne(scenarioId);
        return scenarioViewFactory.convertToDomainObject(scenarioView);
    }

    public Scenario update(final Scenario updatedScenario) throws Exception {

        scenarioValidator.verifyScenarioNameIsUnique(updatedScenario);

        final ScenarioView currentScenarioView = scenarioViewDao.findOne(updatedScenario.getScenarioId().toString());
        if (currentScenarioView == null) {
            throw new Exception(String.format(NOT_EXISTS_MESSAGE, updatedScenario.getScenarioId().toString()));
        }
        cepAdapter.update(updatedScenario);

        final ScenarioView scenarioView = scenarioViewFactory.build(updatedScenario);
        scenarioViewDao.save(scenarioView);

        return updatedScenario;
    }

    public void delete(final Scenario scenario) throws Exception {

        cepAdapter.remove(scenario);
        final ScenarioView scenarioView = scenarioViewFactory.build(scenario);
        scenarioViewDao.delete(scenarioView);
    }
}
