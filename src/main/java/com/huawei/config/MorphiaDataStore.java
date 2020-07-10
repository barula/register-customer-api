package com.huawei.config;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * Configuracion de morphia. Utilizamos morphia como ORM. Se debe especificar el nombre de la base en la creacion del
 * datastore.
 */
public class MorphiaDataStore {
    private final MongoClient mongoClient;

    @Autowired
    public MorphiaDataStore(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Bean
    public Datastore datastore() {
        Morphia morphia = new Morphia();

        morphia.mapPackage("com.huawei.entities");

        Datastore datastore = morphia.createDatastore(mongoClient, "register-customer");
        datastore.ensureIndexes();
        return datastore;
    }

}


