package GUI_Package;


import java.awt.Component;
import java.awt.TextArea;
import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author alex
 */


class ServTableCellRender  extends DefaultTableCellRenderer{
    final static int RowHeight = 20;

    public ServTableCellRender() {
        super();
    }
    
    public Component getTableCellRendererComponent(
                            JTable table, Object us,
                            boolean isSelected, boolean hasFocus,
                            int row, int column)  {
                
                super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
                
                ServListModel users = (ServListModel)us;
                return (JLabel)users.getServName();
    }
    
}
class ServTableEditor extends AbstractCellEditor implements TableCellEditor {
    ServListModel component;
    public ServTableEditor() {
        super();
    }
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
          int rowIndex, int vColIndex) {
        component = ((ServListModel)value);
        return component.getServName();
    }
    public Object getCellEditorValue() {
        return component;
    }

}


public class UsersTableCellRender  extends DefaultTableCellRenderer{
    final static int RowHeight = 20;

    public UsersTableCellRender() {
        super();
    }
    
    public Component getTableCellRendererComponent(
                            JTable table, Object us,
                            boolean isSelected, boolean hasFocus,
                            int row, int column)  {
                
                super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
                
                ServListModel users = (ServListModel)us;
                return (JList)users.getJList();
    }
    
}
class UsersTableEditor extends AbstractCellEditor implements TableCellEditor {
    ServListModel component;
    public UsersTableEditor() {
        super();
    }
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
          int rowIndex, int vColIndex) {
        component = ((ServListModel)value);
        return component.getJList();
    }
    public Object getCellEditorValue() {
        return component;
    }

}

class StateTableCellRender  extends DefaultTableCellRenderer{

    public StateTableCellRender() {
        super();
    }
    
    public Component getTableCellRendererComponent(
                            JTable table, Object users,
                            boolean isSelected, boolean hasFocus,
                            int row, int column)  {
            super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);                
            return (((ServListModel)users).getState());
        }
    
}

class StateTableEditor extends AbstractCellEditor implements TableCellEditor {
    ServListModel component;
    public StateTableEditor() {
        super();
    }
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
         int rowIndex, int vColIndex) {
        component = ((ServListModel)value);
        return component.getState();
    }
    public Object getCellEditorValue() {
        return component;
    }

}



class ProgressTableCellRender  extends DefaultTableCellRenderer{

    public ProgressTableCellRender() {
        super();
    }
    
    public Component getTableCellRendererComponent(
                            JTable table, Object users,
                            boolean isSelected, boolean hasFocus,
                            int row, int column)  {
            super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);                
            return (((ServListModel)users).getProgress());
        }    
}

class ProgressTableEditor extends AbstractCellEditor implements TableCellEditor {
    ServListModel component;
    public ProgressTableEditor() {
        super();
    }
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
         int rowIndex, int vColIndex) {
        component = ((ServListModel)value);
        return component.getProgress();
    }
    public Object getCellEditorValue() {
        return component;
    }

}