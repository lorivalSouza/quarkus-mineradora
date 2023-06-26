package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.entity.ProposalEntity;
import org.br.mineradora.message.KafkaEvent;
import org.br.mineradora.repository.ProposalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
@ApplicationScoped
public class ProposalServiceImpl implements ProposalService{

    private final Logger LOG = LoggerFactory.getLogger(ProposalService.class);
    @Inject
    ProposalRepository proposalRepository;

    @Inject
    KafkaEvent kafkaEvent;

    @Override
    public ProposalDetailsDTO findFullProposal(long id) {

        LOG.info("-- Finding proposal by Id --");

        ProposalEntity proposal = proposalRepository.findById(id);

        return ProposalDetailsDTO
                .builder()
                .proposalId(proposal.getId())
                .priceTonne(proposal.getPriceTonne())
                .proposalValidityDays(proposal.getProposalValidityDays())
                .country(proposal.getCountry())
                .customer(proposal.getCustomer())
                .tonnes(proposal.getTonnes())
                .build();

    }

    @Override
    @Transactional
    public void createNewProposal(ProposalDetailsDTO proposalDetailsDTO) {

        LOG.info("-- Creating new proposal --");

        ProposalDTO proposal = buildAndSaveNewProposal(proposalDetailsDTO);
        kafkaEvent.sendNewKafkaEvent(proposal);


    }

    @Override
    @Transactional
    public void removeProposal(long id) {

        LOG.info("-- Deleting proposal by Id --");

        proposalRepository.deleteById(id);

    }

    private ProposalDTO buildAndSaveNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        try {

            ProposalEntity proposal = new ProposalEntity();

            proposal.setCreated(new Date());
            proposal.setProposalValidityDays(proposalDetailsDTO.getProposalValidityDays());
            proposal.setCountry(proposalDetailsDTO.getCountry());
            proposal.setCustomer(proposalDetailsDTO.getCustomer());
            proposal.setPriceTonne(proposalDetailsDTO.getPriceTonne());
            proposal.setTonnes(proposalDetailsDTO.getTonnes());

           proposalRepository.persist(proposal);

           return ProposalDTO.builder()
                   .proposalId(proposalRepository.findByCustomer(proposal.getCustomer()).get().getId())
                   .priceTonne(proposal.getPriceTonne())
                   .customer(proposal.getCustomer())
                   .build();

        }catch (Exception e){

            LOG.info("-- Exception proposal --" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();

        }

    }

}
