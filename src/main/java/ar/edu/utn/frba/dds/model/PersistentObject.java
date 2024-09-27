package ar.edu.utn.frba.dds.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class PersistentObject {

    @Id @GeneratedValue
    private Long id;

}
