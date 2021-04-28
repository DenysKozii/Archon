package com.company.archon.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "images")
@EqualsAndHashCode(callSuper = true)
public class Image extends Media {

  @OneToMany(mappedBy = "image", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<Question> questions = new ArrayList<>();

}