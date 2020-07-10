package com.huawei.converters;

import com.huawei.entities.CustomerDocument;
import com.huawei.models.CustomerModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
/**
 * Conversor entre modelo y entidad. Se utiliza para quitarle esta responsabilidad a los servicios y poder
 * reutilizarlo a traves del ConversionService de spring.
 */
public class ExampleDocumentToModelConverter implements Converter<CustomerDocument, CustomerModel> {

    @Override
    public CustomerModel convert(CustomerDocument customerDocument) {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setCreationDate(customerDocument.getCreationDate());
        customerModel.setId(customerDocument.getId());
        customerModel.setName(customerDocument.getName());
        return customerModel;
    }
}
