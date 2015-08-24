package uk.co.foyst.smalldata.cep.api;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import uk.co.foyst.smalldata.cep.Scenario;
import uk.co.foyst.smalldata.cep.ScenarioId;
import uk.co.foyst.smalldata.cep.service.ScenarioService;

import java.util.List;

@Controller
@RequestMapping(value = "/v1/scenarios")
public class ScenarioController {

    public static final String MISSING_SCEHARNIO_MESSAGE = "Scenario '%s' does not exist.";
    public static final String SCENARIO_CREATION_ERROR_MESSAGE = "Scenario '%s' could not be created.";
    private final Logger log = LoggerFactory.getLogger(ScenarioController.class);

    private final ScenarioService scenarioService;
    private final ScenarioDtoFactory scenarioDtoFactory;

    @Autowired
    public ScenarioController(ScenarioService scenarioService, ScenarioDtoFactory scenarioDtoFactory) {
        this.scenarioService = scenarioService;
        this.scenarioDtoFactory = scenarioDtoFactory;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public final ResponseEntity<List<ScenarioDto>> get() throws Exception {

        final String messageFormat = "No Scenarios found.";
        final List<Scenario> existingScenarios = scenarioService.readAll();

        Assert.notNull(existingScenarios, String.format(messageFormat));

        final List<ScenarioDto> scenaroDtos = scenarioDtoFactory.build(existingScenarios);

        return new ResponseEntity<>(scenaroDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    @ResponseBody
    public final ResponseEntity<ScenarioDto> getById(@PathVariable("key") final String scenarioId) throws Exception {

        final ScenarioId id = ScenarioId.fromString(scenarioId);
        final Scenario resource = scenarioService.read(id);
        Assert.notNull(resource, String.format(MISSING_SCEHARNIO_MESSAGE, scenarioId));
        final ScenarioDto scenarioDto = scenarioDtoFactory.build(resource);
        return new ResponseEntity<>(scenarioDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public final ResponseEntity<Void> create(@RequestBody final ScenarioDto scenarioDto) throws Exception {

        log.info("add: scenarioDto={}", scenarioDto);

        scenarioDto.setUpdatedAt(new DateTime());
        scenarioDto.setUpdatedBy("user"); // FIXME: get real user

        final Scenario scenario = scenarioDtoFactory.convertToScenario(scenarioDto);
        log.info("add: scenario={}", scenario);

        Scenario createdScenario;
        try {
            createdScenario = scenarioService.create(scenario);
        } catch (Exception e) {
            throw new Exception(String.format(SCENARIO_CREATION_ERROR_MESSAGE, scenario.getScenarioId().toString()), e);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ControllerLinkBuilder.linkTo(ScenarioController.class).slash(createdScenario.getScenarioId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public final ResponseEntity<ScenarioDto> update(@RequestBody final ScenarioDto scenarioDto) throws Exception {

        final String messageFormat = "Scenario '%s' could not be updated.";
        Assert.notNull(scenarioDto, String.format(messageFormat, scenarioDto.getScenarioId()));

        final ScenarioId id = ScenarioId.fromString(scenarioDto.getScenarioId());

        Assert.notNull(scenarioService.read(id), String.format(messageFormat, scenarioDto.getScenarioId()));

        scenarioDto.setUpdatedAt(new DateTime());
        scenarioDto.setUpdatedBy("user"); // FIXME: get real user

        final Scenario scenario = scenarioDtoFactory.convertToScenario(scenarioDto);

        final Scenario updatedScenario = scenarioService.update(scenario);

        final ScenarioDto updatedScenarioDto = scenarioDtoFactory.build(updatedScenario);
        return new ResponseEntity<>(updatedScenarioDto, HttpStatus.OK);
    }
}
