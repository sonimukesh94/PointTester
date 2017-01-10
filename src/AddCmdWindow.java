import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Font;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JButton;

import com.sun.xml.internal.bind.v2.Messages;

import sun.misc.Queue;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.JCheckBox;


public class AddCmdWindow extends JFrame{
	static Queue<Message> queue;
	static Logger logger;
	private JTextField textFieldCmdName;
	private JTextArea textArea;
	private JTextField textLIStartId;
	private JTextField textLIEndId;
	
	private String cmdName;
	private String xmlInput;
	private String startLIId;
	private boolean LICmd;
	
	public boolean isLICmd() {
		return LICmd;
	}

	public void setLICmd(boolean LICmd) {
		this.LICmd = LICmd;
	}

	public String getStartLIId() {
		return startLIId;
	}

	public void setStartLIId(String startLIId) {
		this.startLIId = startLIId;
	}

	public String getEndLIId() {
		return endLIId;
	}

	public void setEndLIId(String endLIId) {
		this.endLIId = endLIId;
	}

	private String endLIId;
	
	public String getCmdName() {
		return cmdName;
	}

	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}

	public String getXmlInput() {
		return xmlInput;
	}

	public void setXmlInput(String xmlInput) {
		this.xmlInput = xmlInput;
	}


	
	public AddCmdWindow(Queue<Message> q, XmlUtils xmlutils, Logger log) {
		setAlwaysOnTop(true);
		queue = q;
		logger = log;
		setTitle("Add Command");
		this.setSize(500, 600);
		this.setLocationRelativeTo(null);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{34, 0, 121, 102, 105, 0, 0};
		gridBagLayout.rowHeights = new int[]{16, 31, 17, 0, 411, 34, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblCmdName = new JLabel("Command Name");
		lblCmdName.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblCmdName = new GridBagConstraints();
		gbc_lblCmdName.insets = new Insets(0, 0, 5, 5);
		gbc_lblCmdName.gridx = 1;
		gbc_lblCmdName.gridy = 1;
		getContentPane().add(lblCmdName, gbc_lblCmdName);
		
		textFieldCmdName = new JTextField();
		GridBagConstraints gbc_textFieldCmdName = new GridBagConstraints();
		gbc_textFieldCmdName.gridwidth = 3;
		gbc_textFieldCmdName.fill = GridBagConstraints.BOTH;
		gbc_textFieldCmdName.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldCmdName.gridx = 2;
		gbc_textFieldCmdName.gridy = 1;
		getContentPane().add(textFieldCmdName, gbc_textFieldCmdName);
		textFieldCmdName.setColumns(10);
		
		JLabel lblNewLabelXmlInput = new JLabel("XML Input");
		lblNewLabelXmlInput.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabelXmlInput = new GridBagConstraints();
		gbc_lblNewLabelXmlInput.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabelXmlInput.gridx = 1;
		gbc_lblNewLabelXmlInput.gridy = 2;
		getContentPane().add(lblNewLabelXmlInput, gbc_lblNewLabelXmlInput);
		
		JCheckBox chckbxLiCommand = new JCheckBox("LI Command");
		chckbxLiCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxLiCommand.isSelected())
				{
					textLIStartId.setEnabled(true);
					textLIEndId.setEnabled(true);
				}
				else
				{
					textLIStartId.setEnabled(false);
					textLIEndId.setEnabled(false);
				}
			}
		});
		chckbxLiCommand.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_chckbxLiCommand = new GridBagConstraints();
		gbc_chckbxLiCommand.anchor = GridBagConstraints.WEST;
		gbc_chckbxLiCommand.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxLiCommand.gridx = 2;
		gbc_chckbxLiCommand.gridy = 2;
		getContentPane().add(chckbxLiCommand, gbc_chckbxLiCommand);
		
		textLIStartId = new JTextField();
		textLIStartId.setEnabled(false);
		GridBagConstraints gbc_textLIStartId = new GridBagConstraints();
		gbc_textLIStartId.insets = new Insets(0, 0, 5, 5);
		gbc_textLIStartId.fill = GridBagConstraints.BOTH;
		gbc_textLIStartId.gridx = 3;
		gbc_textLIStartId.gridy = 2;
		getContentPane().add(textLIStartId, gbc_textLIStartId);
		textLIStartId.setColumns(10);
		
		textLIEndId = new JTextField();
		textLIEndId.setEnabled(false);
		GridBagConstraints gbc_textLIEndId = new GridBagConstraints();
		gbc_textLIEndId.insets = new Insets(0, 0, 5, 5);
		gbc_textLIEndId.fill = GridBagConstraints.BOTH;
		gbc_textLIEndId.gridx = 4;
		gbc_textLIEndId.gridy = 2;
		getContentPane().add(textLIEndId, gbc_textLIEndId);
		textLIEndId.setColumns(10);
		
		JLabel lblStartLiId = new JLabel("Start LI ID");
		lblStartLiId.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		GridBagConstraints gbc_lblStartLiId = new GridBagConstraints();
		gbc_lblStartLiId.insets = new Insets(0, 0, 5, 5);
		gbc_lblStartLiId.gridx = 3;
		gbc_lblStartLiId.gridy = 3;
		getContentPane().add(lblStartLiId, gbc_lblStartLiId);
		
		JLabel lblEndLiId = new JLabel("End LI ID");
		lblEndLiId.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		GridBagConstraints gbc_lblEndLiId = new GridBagConstraints();
		gbc_lblEndLiId.insets = new Insets(0, 0, 5, 5);
		gbc_lblEndLiId.gridx = 4;
		gbc_lblEndLiId.gridy = 3;
		getContentPane().add(lblEndLiId, gbc_lblEndLiId);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 4;
		getContentPane().add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Verdana", Font.PLAIN, 15));
		scrollPane.setViewportView(textArea);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(! textFieldCmdName.getText().isEmpty() && ! textArea.getText().isEmpty() && xmlutils.validateXml(textArea.getText()))
				{		
					setCmdName(textFieldCmdName.getText());
					setXmlInput(textArea.getText());
					if(chckbxLiCommand.isSelected())
					{
						setLICmd(true);
						setStartLIId(textLIStartId.getText());
						setEndLIId(textLIEndId.getText());						
					}
					else
					{
						setLICmd(false);
						setStartLIId("");
						setEndLIId("");	
					}
					Message newMsg = new Message();
					newMsg.iMsgType = Message.msgType.SERVICE;
					newMsg.iSource = Message.sourceType.ADDCMD;
					newMsg.iServiceType = Message.serviceType.SAVE;
					newMsg.data = getXmlInput();
					queue.enqueue(newMsg);
				}
			}
		});
		
		JButton btnCancel = new JButton("CANCEL");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.fill = GridBagConstraints.BOTH;
		gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancel.gridx = 1;
		gbc_btnCancel.gridy = 5;
		getContentPane().add(btnCancel, gbc_btnCancel);
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 0, 5);
		gbc_btnSave.anchor = GridBagConstraints.WEST;
		gbc_btnSave.fill = GridBagConstraints.BOTH;
		gbc_btnSave.gridx = 2;
		gbc_btnSave.gridy = 5;
		getContentPane().add(btnSave, gbc_btnSave);
		
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setSize(500, 400);
		
	}
	
	public void showAddCmdWin() throws Exception {
		this.textFieldCmdName.setText("");
		this.textArea.setText("");
		this.textLIEndId.setText("");
		this.textLIStartId.setText("");
		this.show();
	}
	

}
