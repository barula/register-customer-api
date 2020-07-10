package com.huawei.entities.access;

import com.huawei.entities.CustomerDocument;
import com.paypertic.filter.util.datastore.SearcheableDatastoreAccess;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
/**
 * Ejemplo de datastore que implementa la clase SearchableDatastoreAccess  de filter utils.
 */
public class CustomersDatastore extends SearcheableDatastoreAccess<CustomerDocument> {

    private Datastore datastore;
    private static final Logger log = LogManager.getLogger(CustomersDatastore.class);

    @Autowired
    public CustomersDatastore(Datastore datastore) {
        super(CustomerDocument.class, datastore, false);
        this.datastore = datastore;
    }

    public CustomerDocument save(CustomerDocument customerDocument) {
        customerDocument.setId(UUID.randomUUID().toString());
        datastore.save(customerDocument);
        return customerDocument;
    }

    public Optional<CustomerDocument> find(String id) {
        Query<CustomerDocument> query = datastore.createQuery(CustomerDocument.class);
        query.field("_id").equal(id);
        List<CustomerDocument> result = query.find().toList();
        if (result.size() == 1) {
            return Optional.of(result.get(0));
        } else if (result.isEmpty()) {
            return Optional.empty();
        } else {
            log.error("Duplicated example for id {}", id);
            throw new RuntimeException("Duplicated example for id " + id);
        }
    }

}
