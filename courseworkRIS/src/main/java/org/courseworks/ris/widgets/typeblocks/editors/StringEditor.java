package org.courseworks.ris.widgets.typeblocks.editors;

import java.lang.reflect.Field;

import org.courseworks.ris.mappings.AbstractEntity;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Класс для поиска символьных значений
 */
public class StringEditor extends AbstractFieldEditor {

	/** Поле ввода значений */
	protected Text text;

	public StringEditor(Composite composite, int style, Field field,
			int editType) {
		super(composite, style, field, editType);

		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		super.setLayout(layout);

		text = new Text(this, SWT.SINGLE);
		text.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue() {
		if (text.getText() == "") {
			return null;
		}
		return text.getText();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Object aValue) {
		if (!(aValue instanceof String)) {
			throw new IllegalArgumentException("value must be String");
		}

		String value = (String) aValue;
		text.setText(value);
	}

	@Override
	public void setEditingItem(AbstractEntity item) {
	}
}
