package com.huawei.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paypertic.common.enums.security.Realm;
import com.paypertic.filter.util.annotations.FilterAllow;
import com.paypertic.filter.util.enums.Operation;

import java.util.Date;

/**
 * Modelo(DTO) de ejemplo. Utilzamos Entidad+Model.
 */
public class CustomerModel {

    /**
     * Anotación de filter utils. Field name indica el nombre del campo en la base si difiere al del modelo, realms
     * los realms permitidos a filtrar por ese campo y avalaibleOperations los operandos permitidos.
     */
    @FilterAllow(fieldName = "example_id", realms = {Realm.COLLECTOR, Realm.OPERATOR, Realm.SYSTEM, Realm.PAYER},
            availableOperations = {Operation.EQUAL})
    private String id;

    @FilterAllow(realms = {Realm.COLLECTOR, Realm.OPERATOR, Realm.SYSTEM, Realm.PAYER},
            availableOperations = {Operation.EQUAL})
    private String name;

    /**
     * La convencion en los modelos es snake case.
     */
    @JsonProperty("creation_date")
    @FilterAllow(realms = {Realm.COLLECTOR, Realm.OPERATOR, Realm.SYSTEM, Realm.PAYER},
            availableOperations = {Operation.LESS_THAN, Operation.LESS_THAN_OR_EQUAL_TO,
                    Operation.GREATER_THAN_OR_EQUAL_TO, Operation.GREATER_THAN, Operation.EQUAL})
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