package org.br.mineradora.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.br.mineradora.entity.OpportunityEntity;

import java.util.Optional;

@ApplicationScoped
public class OpportunityRepository implements PanacheRepository<OpportunityEntity> {

    public Optional<OpportunityEntity> findByProposal(String proposal){

        return Optional.of(find("proposal", proposal).firstResult());

    }

}
