package views;

import javax.swing.table.AbstractTableModel;

public class TPTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private Object[][] data;
    private String[] columns;

    public TPTableModel(String[] columns,Object[][] data) {
        this.columns = columns;
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(final Object value, final int row, final int column) {
        data[row][column] = value;
    }

    @Override
    public String getColumnName(final int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return data[0][columnIndex].getClass();
    }
}