package org.br.mineradora.controller;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.br.mineradora.dto.OpportunityDTO;
import org.br.mineradora.service.ReportService;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Date;
import java.util.List;

@Path("/api/opportunity")
@Authenticated
public class ReportController {

    @Inject
    ReportService reportService;

    @GET
    @Path("/report")
    @RolesAllowed({"user","manager"})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response generateReport(){

        try {
            return  Response.ok(reportService.generateCSVOpportunityReport(),
                    MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition",
                            "attachment; filename = "+ new Date() + "--sell-opportunity.csv").build();
        }catch (ServerErrorException errorException){
            return  Response.serverError().build();
        }

    }

    @GET
    @Path("/data")
    @RolesAllowed({"user","manager"})
    public Response generateReportData(){

        try {
            return  Response.ok(reportService.getOpportunityData(),
                    MediaType.APPLICATION_JSON).build();
        }catch (ServerErrorException errorException){
            return  Response.serverError().build();
        }

    }

}
