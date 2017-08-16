package jp.co.example.common;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AppLocalDateTimeToTimestampConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    //データベースに保存するときに使われる(LocalDateTime→Timestamp)
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
    //データベースから復元するときに使われる(Timestamp→LocalDateTime)
    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }
}
