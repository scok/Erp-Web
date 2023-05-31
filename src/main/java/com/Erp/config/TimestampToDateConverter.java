package com.Erp.config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.util.Date;
@Converter(autoApply = true)
public class TimestampToDateConverter implements AttributeConverter<Timestamp, Date> {

    @Override
    public Date convertToDatabaseColumn(Timestamp timestamp) {
        if(timestamp != null){
            return new Date(timestamp.getTime());
        }
        return null;
    }

    @Override
    public Timestamp convertToEntityAttribute(Date date) {
        if(date != null){
            return new Timestamp(date.getTime());
        }

        return null;
    }
}
