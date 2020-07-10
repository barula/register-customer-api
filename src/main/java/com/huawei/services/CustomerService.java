package com.huawei.services;

import com.huawei.entities.CustomerDocument;
import com.huawei.entities.access.CustomersDatastore;
import com.huawei.models.CustomerModel;
import com.paypertic.common.enums.security.Realm;
import com.paypertic.filter.util.SearchFactory;
import com.paypertic.filter.util.SearchUtils;
import com.paypertic.filter.util.models.Page;
import com.paypertic.filter.util.models.SearchRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
/**
 * Servicio de ejemplo.
 */
public class CustomerService {

    private CustomersDatastore customersDatastore;
    private ConversionService conversionService;

    /**
     * El logger lo maneja log4j y esta configurado con un archivo de config que esta en resources.
     * https://documentos.paypertic.com/display/kase/Logs+-+Formato+general
     */
    private static final Logger log = LogManager.getLogger(CustomerService.class);

    @Autowired
    public CustomerService(CustomersDatastore customersDatastore, ConversionService conversionService) {
        this.customersDatastore = customersDatastore;
        this.conversionService = conversionService;
    }
    /**
     *  Anotación de security utils, valida que el usuario tenga el rol correspondiente
     */
    public CustomerModel create(CustomerModel model) {
        CustomerDocument customerDocument = new CustomerDocument();
        customerDocument.setCreationDate(new Date());
        customerDocument.setId(UUID.randomUUID().toString());
        customerDocument.setName(model.getName());
        customersDatastore.save(customerDocument);
        return conversionService.convert(customerDocument, CustomerModel.class);
    }

    public CustomerModel get(String id) {
        log.info("Customer log for id {}", id);
        CustomerDocument exporterDocument =
                customersDatastore.find(id).orElseThrow(() -> new RuntimeException("Parameter not found."));

        /**
         * Utilizamos el conversion service para la conversion entre documento y modelo.
         */
        return conversionService.convert(exporterDocument, CustomerModel.class);
    }

    /**
     * Implementacion de filter utils. En este caso no se agregan mas filtros pero podrian añadirse luego de crear el
     * search request filtros de seguridad por usuario o collector id.
     */
    public Page<CustomerModel> search(String queryString) {
        SearchRequest searchRequest = SearchFactory.fromUri(queryString, Realm.SYSTEM,
                CustomerModel.class);

        return (Page<CustomerModel>) SearchUtils.search(searchRequest, customersDatastore, document -> conversionService.convert(document,
                CustomerModel.class));
    }
}