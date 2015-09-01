package uk.co.foyst.smalldata.cep.dao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StreamViewDao extends JpaRepository<StreamView, String> {

    public StreamView findByName(final String streamName);
}