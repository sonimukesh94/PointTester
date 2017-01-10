import javafx.scene.control.TableRow;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Font;

import javax.swing.JTextField;

import java.awt.Insets;

import com.jgoodies.*;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;

import java.awt.GridLayout;

import net.miginfocom.swing.MigLayout;

import java.awt.FlowLayout;

import javax.swing.*;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.plaf.ListUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import sun.misc.Queue;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.ListSelectionModel;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


public class AddTemplateWindow extends JFrame{
	 Queue<Message> queue;
	 static Logger logger;
	 JTextField textFieldTemplateName;
	 JList listSource;
	//JSpinnerList spinnerList;
	//DefaultListModel<JCheckBox> listModelCheckBox = new DefaultListModel<JCheckBox>();
	//DefaultListModel<JSpinner> listModelSpinner = new DefaultListModel<JSpinner>();
	 DefaultListModel<String> listModelSource = new DefaultListModel<String>();	
	 DefaultTableModel tableModel = new DefaultTableModel();
	
	 JTable tableCounter;
	 String templtName;
	 
	
	public void setTemplateName(String tName) {
		this.templtName = tName;
	}
	
	public String getTemplateName() {
		return this.templtName;
	}
	
	public AddTemplateWindow(Queue<Message> q, XmlUtils xmlutils, Commands commands, Logger log) {
		setAlwaysOnTop(true);
		queue = q;
		logger = log;
		setTitle("Template");
		this.setSize(700, 600);
		this.setLocationRelativeTo(null);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{36, 238, 20, 247, 72, 0};
		gridBagLayout.rowHeights = new int[]{35, 37, 30, 31, 13, 0, 327, 36, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblTemplateName = new JLabel("Template Name");
		lblTemplateName.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblTemplateName = new GridBagConstraints();
		gbc_lblTemplateName.insets = new Insets(0, 0, 5, 5);
		gbc_lblTemplateName.gridx = 1;
		gbc_lblTemplateName.gridy = 1;
		getContentPane().add(lblTemplateName, gbc_lblTemplateName);
		
		textFieldTemplateName = new JTextField();
		GridBagConstraints gbc_textFieldTemplateName = new GridBagConstraints();
		gbc_textFieldTemplateName.gridwidth = 3;
		gbc_textFieldTemplateName.fill = GridBagConstraints.BOTH;
		gbc_textFieldTemplateName.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldTemplateName.gridx = 2;
		gbc_textFieldTemplateName.gridy = 1;
		getContentPane().add(textFieldTemplateName, gbc_textFieldTemplateName);
		textFieldTemplateName.setColumns(10);
		
		JLabel lblText1 = new JLabel("Select Commands From The Below List");
		lblText1.setFont(new Font("Tahoma", Font.ITALIC, 12));
		GridBagConstraints gbc_lblText1 = new GridBagConstraints();
		gbc_lblText1.anchor = GridBagConstraints.NORTH;
		gbc_lblText1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblText1.insets = new Insets(0, 0, 5, 5);
		gbc_lblText1.gridx = 1;
		gbc_lblText1.gridy = 2;
		getContentPane().add(lblText1, gbc_lblText1);
		
		JLabel lblSlectedCommandList = new JLabel("Selected Command List");
		lblSlectedCommandList.setFont(new Font("Tahoma", Font.ITALIC, 12));
		GridBagConstraints gbc_lblSlectedCommandList = new GridBagConstraints();
		gbc_lblSlectedCommandList.insets = new Insets(0, 0, 5, 5);
		gbc_lblSlectedCommandList.gridx = 3;
		gbc_lblSlectedCommandList.gridy = 2;
		getContentPane().add(lblSlectedCommandList, gbc_lblSlectedCommandList);
		
		JLabel lblCounter = new JLabel("Counter");
		GridBagConstraints gbc_lblCounter = new GridBagConstraints();
		gbc_lblCounter.insets = new Insets(0, 0, 5, 0);
		gbc_lblCounter.gridx = 4;
		gbc_lblCounter.gridy = 2;
		getContentPane().add(lblCounter, gbc_lblCounter);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 4;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 3;
		getContentPane().add(scrollPane, gbc_scrollPane);
		
		listSource = new JList<String>(listModelSource);
		listSource.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
			}
		});
		listSource.setLayoutOrientation(JList.VERTICAL_WRAP);
		listSource.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 14));
		listSource.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(listSource);
		
		JButton btnInsert = new JButton(">");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logger.info("Insert Button Pressed in Template Window");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.SERVICE;
				newMsg.iSource = Message.sourceType.ADDTEMPLATE;
				newMsg.iServiceType = Message.serviceType.INSERT;
				queue.enqueue(newMsg);
			}
		});
		GridBagConstraints gbc_btnInsert = new GridBagConstraints();
		gbc_btnInsert.fill = GridBagConstraints.BOTH;
		gbc_btnInsert.insets = new Insets(0, 0, 5, 5);
		gbc_btnInsert.gridx = 2;
		gbc_btnInsert.gridy = 3;
		getContentPane().add(btnInsert, gbc_btnInsert);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.gridheight = 4;
		gbc_scrollPane_2.gridwidth = 2;
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 3;
		gbc_scrollPane_2.gridy = 3;
		getContentPane().add(scrollPane_2, gbc_scrollPane_2);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		centerRenderer.setDoubleBuffered(false);
		
		tableModel = new DefaultTableModel();
		tableCounter = new JTable(tableModel);
		scrollPane_2.setViewportView(tableCounter);
		tableCounter.setRowHeight(23);
		tableCounter.setTableHeader(null);
		tableModel.setColumnCount(2);
		
		tableCounter.getColumnModel().getColumn(0).setPreferredWidth(250);
		tableCounter.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		
		JButton btnDelete = new JButton("<");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Delete Button Pressed in Template Window");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.SERVICE;
				newMsg.iSource = Message.sourceType.ADDTEMPLATE;
				newMsg.iServiceType = Message.serviceType.DELETE;
				queue.enqueue(newMsg);
			}
		});
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.fill = GridBagConstraints.BOTH;
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 2;
		gbc_btnDelete.gridy = 4;
		getContentPane().add(btnDelete, gbc_btnDelete);
		
		JButton btnDeleteAll = new JButton("<<");
		btnDeleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("DeleteAll Button Pressed in Template Window");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.SERVICE;
				newMsg.iSource = Message.sourceType.ADDTEMPLATE;
				newMsg.iServiceType = Message.serviceType.DELETEALL;
				queue.enqueue(newMsg);
			}
		});
		GridBagConstraints gbc_btnDeleteAll = new GridBagConstraints();
		gbc_btnDeleteAll.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteAll.gridx = 2;
		gbc_btnDeleteAll.gridy = 5;
		getContentPane().add(btnDeleteAll, gbc_btnDeleteAll);
		
		JButton btnCancel = new JButton("CANCEL");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.fill = GridBagConstraints.VERTICAL;
		gbc_btnCancel.anchor = GridBagConstraints.EAST;
		gbc_btnCancel.insets = new Insets(0, 0, 5, 5);
		gbc_btnCancel.gridx = 3;
		gbc_btnCancel.gridy = 7;
		getContentPane().add(btnCancel, gbc_btnCancel);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! textFieldTemplateName.getText().isEmpty() && tableCounter.getRowCount() > 0 )
				{		
					setTemplateName(textFieldTemplateName.getText());
					Message newMsg = new Message();
					newMsg.iMsgType = Message.msgType.SERVICE;
					newMsg.iSource = Message.sourceType.ADDTEMPLATE;
					newMsg.iServiceType = Message.serviceType.SAVE;
					queue.enqueue(newMsg);
					dispose();
				}
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.fill = GridBagConstraints.BOTH;
		gbc_btnSave.insets = new Insets(0, 0, 5, 0);
		gbc_btnSave.gridx = 4;
		gbc_btnSave.gridy = 7;
		getContentPane().add(btnSave, gbc_btnSave);
		
		for(int i = 0; i < commands.getCmdList().size(); i++)
			this.listModelSource.addElement(commands.getCmdList().getElementAt(i));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//		this.setSize(800, 600);
	}

	public void updateTmpltListModel(String cmdName) throws Exception {
		this.listModelSource.addElement(cmdName);
		listSource.validate();
	}
	public void showAddTmpltWin(Commands cmd) throws Exception {
		this.textFieldTemplateName.setText("");
		this.tableModel.setRowCount(0);
		this.show();
	}
	
	public void showEditTmpltWin(String tmpltName, Vector<Vector<String>> tableData) throws Exception {
		this.textFieldTemplateName.setText(tmpltName);
		logger.info("Edit Template Size " + tableData.size());
		this.tableModel.setRowCount(0);
		for(int i = 0; i < tableData.size(); i++)
			this.tableModel.addRow(tableData.elementAt(i));
		this.show();
	}
		
	public void insertCmdToDestList() throws Exception {
		if(listSource.getSelectedIndex() != -1)
		{
			Vector<String> rowData = new Vector<String>();
			rowData.add(listModelSource.getElementAt(listSource.getSelectedIndex()));
			rowData.add("1");
			tableModel.addRow(rowData);
			logger.info("Insert "+ listModelSource.getElementAt(listSource.getSelectedIndex())+ " Command to Destination List");
		}
	}

	public void deleteCmdFromDestList() throws Exception {
		int rowId = tableCounter.getSelectedRow();
		if(rowId != -1)
		{
			logger.info("Delete "+  tableModel.getValueAt(rowId, 0).toString() + " Command from Destination List");
			tableModel.removeRow(rowId);			
		}
	}
	
	public void deleteAllCmdFromDestList() throws Exception {
		logger.info("DeleteAll Command from Destination List of Size " + tableModel.getRowCount());
		if(tableModel.getRowCount() > 0)
			tableModel.setRowCount(0);
	}	

	
}
