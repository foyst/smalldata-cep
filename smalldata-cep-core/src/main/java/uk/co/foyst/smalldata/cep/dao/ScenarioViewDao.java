package uk.co.foyst.smalldata.cep.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioViewDao extends JpaRepository<ScenarioView, String> {

    List<ScenarioView> findAll();
}