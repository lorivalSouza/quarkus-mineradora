package org.br.mineradora.controller;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.service.ProposalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/trade")
@Authenticated
public class ProposalController {

    private final Logger LOG = LoggerFactory.getLogger(ProposalController.class);

    @Inject
    ProposalService proposalService;

    @GET
    @Path("/{id}")
    @RolesAllowed({"user", "manager"})
    public Response getProposalDetailsById(@PathParam("id") long id){

        try{
            LOG.info("-- Finding proposal by ID --");
            return Response.ok(proposalService.getProposalDetailsById(id),
                    MediaType.APPLICATION_JSON).build();
        }catch (ServerErrorException errorException){

            LOG.info("-- Error receiving proposal in BFF --");
            return Response.serverError().build();
        }


    }

    @POST
    @RolesAllowed({"proposal-customer"})
    public Response createProposal(ProposalDetailsDTO proposalDetails){

        LOG.info("-- Receiving proposal in BFF--");

        int proposalRequestStatus = proposalService.createProposal(proposalDetails).getStatus();

        if(proposalRequestStatus > 199 || proposalRequestStatus < 205){
            return Response.ok().build();
        }else {
            return Response.status(proposalRequestStatus).build();
        }

    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"manager"})
    public Response removeProposal(@PathParam("id") long id){

        LOG.info("-- Deleting proposal by ID --");
        int proposalRequestStatus = proposalService.removeProposal(id).getStatus();

        if(proposalRequestStatus > 199 || proposalRequestStatus < 205){
            return Response.ok().build();
        }else {
            return Response.status(proposalRequestStatus).build();
        }
    }

}
