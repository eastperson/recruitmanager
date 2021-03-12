package com.recruitmanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.util.List;

@Entity @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
@Getter @Builder
@Setter @ToString
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    //@Column(unique = true)
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String logo;

    @Lob
    private String link;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Job> jobList;
}
