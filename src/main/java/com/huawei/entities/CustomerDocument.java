package com.huawei.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Indexed;
import dev.morphia.annotations.Property;

import java.util.Date;

/**
 * Los nombres de las colecciones los usamos en plural y snake_case.
 * Entidad de ejemplo. La convencion de nombre es Entidad+Document o Entidad+Embebbed si es embebida.
 */
@Entity(value = "customers", noClassnameStored = true)
public class CustomerDocument {

    @Id
    private String id;

    private String name;

    /**
     * La convencion para la base es snake case
     */
    @Property("creation_date")
    private Date creationDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
