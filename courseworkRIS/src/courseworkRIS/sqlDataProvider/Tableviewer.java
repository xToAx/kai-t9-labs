package courseworkRIS.sqlDataProvider;

import java.lang.reflect.Field;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class Tableviewer {

	private Table _tb;
	private Field[] _fields;

	public Tableviewer(Composite parent, int style) {
		Composite _compt = new Composite(parent, SWT.NONE);
		// _compt.setLayout(new FillLayout(SWT.HORIZONTAL));
		_compt.setLayout(new GridLayout(2, false));
		tbInit(_compt, style);
		toolsPanelInit(_compt, style);
	}

	public Table getTb() {
		return _tb;
	}

	public void setTb(Table _tb) {
		this._tb = _tb;
	}

	public void tbInit(Composite parent, int style) {
		_tb = new Table(parent, style);
		_tb.setLinesVisible(true);
		_tb.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.TOP, true, true);
		data.heightHint = 200;
		_tb.setLayoutData(data);
	}

	public void fill(List<Object> objs) throws IllegalArgumentException,
			IllegalAccessException {
		fillObjectData(objs.get(0).getClass());
		fillTb(objs);
		packTb();
	}

	public void fillObjectData(Class<?> cls) {
		_fields = cls.getDeclaredFields();
		for (Field fld : _fields) {
			TableColumn newColumn = new TableColumn(_tb, SWT.NONE);
			newColumn.setText(fld.getName());
		}
	}

	public void fillTb(List<Object> objs) throws IllegalArgumentException,
			IllegalAccessException {
		for (Object obj : objs) {
			TableItem newRow = new TableItem(_tb, SWT.NONE);
			int index = 0;
			for (Field fld : _fields) {
				newRow.setText(index, fld.get(obj).toString());
				index++;
			}
		}
	}

	public void packTb() {
		for (TableColumn tbc : _tb.getColumns()) {
			tbc.pack();
		}
	}

	public void toolsPanelInit(Composite parent, int style) {
		final Composite toolsPanel = new Composite(parent, SWT.NONE);
		// toolsPanel.setLayout(new FillLayout(SWT.VERTICAL));
		toolsPanel.setLayout(new GridLayout(1, false));
		toolsPanel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

		Composite mainPanel = new Composite(toolsPanel, SWT.NONE);
		// mainPanel.setLayout(new FillLayout(SWT.HORIZONTAL));
		mainPanel.setLayout(new GridLayout(3, false));

		Button filterButton = new Button(mainPanel, SWT.TOGGLE);
		Button findButton = new Button(mainPanel, SWT.TOGGLE);
		filterButton.setText("Filter");
		findButton.setText("Find");

		final Composite filterPanel = new Composite(toolsPanel, SWT.NONE);
		final Composite findPanel = new Composite(toolsPanel, SWT.NONE);

		filterPanel.setBackground(new Color(null, 1, 1, 1));
		findPanel.setBackground(new Color(null, 1, 1, 1));

		filterPanel.setLayout(new GridLayout());
		filterPanel.setLayoutData(new GridData());
		findPanel.setLayout(new GridLayout());
		findPanel.setLayoutData(new GridData());

		filterPanel.setVisible(false);
		findPanel.setVisible(false);

		new Text(findPanel, SWT.NONE).setText("find");
		new Text(filterPanel, SWT.NONE).setText("filter");

		filterButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				if (true == filterPanel.getVisible()) {
					filterPanel.setVisible(false);
				} else {
					GridData filterpGridData = ((GridData) filterPanel
							.getLayoutData());
					filterpGridData.exclude = false;
					filterPanel.setVisible(true);
					findPanel.setVisible(false);
					toolsPanel.layout();
				}
			}
		});
		findButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				if (true == findPanel.getVisible()) {
					findPanel.setVisible(false);
				} else {
					GridData filterpGridData = ((GridData) filterPanel
							.getLayoutData());
					GridData findpGridData = ((GridData) findPanel
							.getLayoutData());
					filterpGridData.exclude = true;
					findpGridData.exclude = false;
					findPanel.setVisible(true);
					filterPanel.setVisible(false);
					toolsPanel.layout();
				}
			}
		});
	}
}