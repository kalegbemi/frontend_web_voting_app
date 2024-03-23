package com.capstone_project.web_voting_app.model;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class AdminResource extends RepresentationModel<AdminResource> {
    private Admin admin;

}
