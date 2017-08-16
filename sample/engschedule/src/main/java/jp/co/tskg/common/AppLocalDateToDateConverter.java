package jp.co.example.common;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AppLocalDateToDateConverter implements AttributeConverter<LocalDate, Date> {

	/**
	 * LocalDate→Date
	 *
	 * @param localDate
	 *            タイムゾーンのない日付
	 * @return タイムゾーンのある日付
	 */
	public static java.sql.Date getDate(LocalDate localDate) {
		return (localDate == null ? null : java.sql.Date.valueOf(localDate));
	}

	/**
	 * Date→LocalDate
	 *
	 * @param date
	 *            タイムゾーンのある日付
	 * @return タイムゾーンのない日付
	 */
	public static LocalDate getLocalDate(java.sql.Date date) {
		return (date == null ? null : date.toLocalDate());
	}

	/**
	 * データベースに保存するときに使われる(LocalDate→Date)
	 *
	 * @param localDate
	 * @return
	 */
	@Override
	public java.sql.Date convertToDatabaseColumn(LocalDate localDate) {
		return getDate(localDate);
	}

	/**
	 * データベースから復元するときに使われる(Date→LocalDate)
	 *
	 * @param date
	 * @return
	 */
	@Override
	public LocalDate convertToEntityAttribute(java.sql.Date date) {
		return getLocalDate(date);
	}
}
