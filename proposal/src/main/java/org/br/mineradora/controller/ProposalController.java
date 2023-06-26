package org.br.mineradora.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.service.ProposalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/proposal")
public class ProposalController {

    private final Logger LOG = LoggerFactory.getLogger(ProposalController.class);

    @Inject
    ProposalService proposalService;

    @GET
    @Path("/{id}")
    @RolesAllowed({"user", "manager"})
    public ProposalDetailsDTO findDetailsProposal(@PathParam("id") long id){

        LOG.info("-- Finding proposal by ID --");
        return proposalService.findFullProposal(id);
    }

    @POST
    @RolesAllowed({"user", "manager"})
    public Response createProposal(ProposalDetailsDTO proposalDetailsDTO){

        LOG.info("-- Receiving proposal --");

        try {
            proposalService.createNewProposal(proposalDetailsDTO);
            return Response.ok().build();
        }catch (Exception e){
            LOG.info("-- Error receiving proposal --");
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"manager"})
    public Response deleteProposal(@PathParam("id") long id){

        LOG.info("-- Deleting proposal by ID --");
        try {
            proposalService.removeProposal(id);
            return Response.ok().build();
        }catch (Exception e){
            LOG.info("-- Error deleting proposal --");
            return Response.serverError().build();
        }
    }

}
