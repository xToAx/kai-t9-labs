package org.courseworks.ris.mappings.orgelqueue;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.courseworks.ris.mappings.AbstractEntity;

@Entity
@Table(name = "engine")
public class Engine extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	public String mark;

	@Temporal(value = TemporalType.DATE)
	public Date makedate;

	@Override
	public String getName() {
		return mark;
	}

	@Override
	public String getFieldPresent(String fieldName) {
		if ("id".equals(fieldName)) {
			return "Идентификатор";
		} else if ("mark".equals(fieldName)) {
			return "Марка";
		} else if ("makedate".equals(fieldName)) {
			return "Дата производства";
		}
		return null;
	}

	@Override
	public Field[] getFields() {
		return getClass().getFields();
	}

	@Override
	public Field[] getViewableFields() {
		Field[] allFields = getClass().getDeclaredFields();
		List<Field> viewableFields = new LinkedList<Field>();

		for (Field field : allFields) {
			if ("id".equals(field.getName())) {
				continue;
			}
			viewableFields.add(field);
		}

		return viewableFields.toArray(new Field[] {});
	}

}
