package org.br.mineradora.message;

import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.QuotationDTO;
import org.br.mineradora.service.OpportunityService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaEvent {
    private final Logger LOG = LoggerFactory.getLogger(KafkaEvent.class);

    @Inject
    OpportunityService opportunityService;
    @Incoming("proposal")
    @Transactional
    public void receiveProposal(ProposalDTO proposal){

        LOG.info("-- Receiving new proposal of Kafka Topics --");
        opportunityService.buildOpportunity(proposal);
    }

    @Incoming("quotation")
    @Blocking
    public void receiveQuotation(QuotationDTO quotation){

        LOG.info("-- Receiving new quotation of Kafka Topics --");
        opportunityService.saveQuotation(quotation);
    }

}
