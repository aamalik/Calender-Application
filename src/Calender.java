/*
 * Asfandyar Ashraf Malik
 * Making a Calender
 */


import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Calender {
	static JLabel lblMonth, lblYear;
	static JButton btnPrev, btnNext;
	static JTable tblCalendar;
	static JComboBox cmbYear;
	static JFrame frmMain; 
	static Container pane;
	static DefaultTableModel mtblCalendar; //Table model
	static JScrollPane stblCalendar; //Der scrollpane
	static JPanel pnlCalendar;
	static int realYear, realMonth, realDay, currentYear, currentMonth;

	public static void main (String args[]){

		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} //This is done to get the theme boyz relative to your awesome operating system
		catch (ClassNotFoundException e) {}
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch (UnsupportedLookAndFeelException e) {}

		
		frmMain = new JFrame ("Meine SpaB Kalender"); //Main frame
		
		pane = frmMain.getContentPane(); //Get content pane via predefined method
		pane.setLayout(null); //Apply null layout
		frmMain.pack();
		frmMain.setSize(500, 500); 
		frmMain.setVisible(true);
		JFrame.setDefaultLookAndFeelDecorated(true); //made inorder to provide window decorations. If you run on windows you should hopefully get it
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked. Cooler than minimize by Omar. 
		
		
		//normale buttons
		lblMonth = new JLabel ("January");
		lblYear = new JLabel ("Select Year");
		cmbYear = new JComboBox();
		btnPrev = new JButton ("<--");
		btnNext = new JButton ("-->");
		
		mtblCalendar = new DefaultTableModel(){
			public boolean isCellEditable(int rowIndex, int mColIndex){
				return false;
				}
			};
		
		tblCalendar = new JTable(mtblCalendar);
		stblCalendar = new JScrollPane(tblCalendar);
		pnlCalendar = new JPanel(null);

		//Border of the program. With the sexy dark grey color. 
		pnlCalendar.setBorder(BorderFactory.createLineBorder(Color.darkGray, 10));
		pane.add(Box.createRigidArea(new Dimension(15,10)));//DImensions to tell where you want the border to be

		
		//Register action listeners
		btnPrev.addActionListener(new btnPrev_Action()); //assigning what actions to be done
		btnNext.addActionListener(new btnNext_Action());
		cmbYear.addActionListener(new cmbYear_Action());
		
		//Add controls to pane
		pane.add(pnlCalendar);
		pnlCalendar.add(lblMonth);
		pnlCalendar.add(lblYear);
		pnlCalendar.add(cmbYear);
		pnlCalendar.add(btnPrev);
		pnlCalendar.add(btnNext);
		pnlCalendar.add(stblCalendar);
		
		//Set bounds
		pnlCalendar.setBounds(0, 0, 500, 500);
		lblMonth.setBounds(350, 300, 100, 25);
		lblYear.setBounds(280, 395, 200, 20);
		cmbYear.setBounds(370, 390, 100, 30);
		btnPrev.setBounds(10, 25, 50, 25);
		btnNext.setBounds(440, 25, 50, 25);
		stblCalendar.setBounds(20, 70, 450, 300);
		
		//Make frame visible
		frmMain.setResizable(false); //cannot be resized because then I have to resize the scrollpanel as well. Can be implemented if desired
		frmMain.setVisible(true); //is visible
		
		//Get real month/year
		GregorianCalendar cal = new GregorianCalendar(); //Create calendar
		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
		realMonth = cal.get(GregorianCalendar.MONTH); //Get month
		realYear = cal.get(GregorianCalendar.YEAR); //Get year
		currentMonth = realMonth; //Match month and year
		currentYear = realYear;
		
		//Add headers
		String[] headers = {"Sun","Mon", "Tue", "Wed", "Thu", "Fri", "Sat" }; 
		for (int i=0; i<7; i++){
			mtblCalendar.addColumn(headers[i]);
		}
		
		tblCalendar.getParent().setBackground(tblCalendar.getBackground());

		
		tblCalendar.getTableHeader().setResizingAllowed(true); //can be resized
		tblCalendar.getTableHeader().setReorderingAllowed(true); //can be resized

		//Single cell selection
		tblCalendar.setColumnSelectionAllowed(true);
		tblCalendar.setRowSelectionAllowed(true);
		tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//Set row/column count
		tblCalendar.setRowHeight(50);
		mtblCalendar.setColumnCount(7);
		mtblCalendar.setRowCount(6);
		
		//Only allowing variation of +50 to -50 years. Can be increased if intended. I believe 50 is enough
		for (int i=realYear-50; i<=realYear+50; i++){
			cmbYear.addItem(String.valueOf(i));
		}
		
		//Refresh calendar
		refreshCalendar (realMonth, realYear); //Refresh calendar
	}
	
	public static void refreshCalendar(int month, int year){
		//Variables
		String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		int nod, som; //Number Of Days, Start Of Month
			
		//Allow/disallow buttons
		btnPrev.setEnabled(true);
		btnNext.setEnabled(true);
		
		if (month == 0 && year <= realYear-10){
			btnPrev.setEnabled(true);
		} //Too early
		
		
		lblMonth.setText(months[month]); //Refresh the month label (at the top)
		lblMonth.setBounds(220, 25, 180, 25); //Re-align label with calendar
		cmbYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box
		
		//Clear table
		for (int i=0; i<6; i++){
			for (int j=0; j<7; j++){
				mtblCalendar.setValueAt(null, i, j);
			}
		}
		
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get((GregorianCalendar.DAY_OF_WEEK));
		
		for (int i=1; i<=nod; i++){
			int row = new Integer((i+som-2)/7);
			int column  =  (i+som-2)%7;
			mtblCalendar.setValueAt(i, row, column);
		}

		tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0), new tblCalendarRenderer());
	}

	static class tblCalendarRenderer extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);
			if (column == 5 || column == 6){ //Week-end
				setBackground(new Color(200, 220, 220));
			}
			else{ //Week
				setBackground(new Color(0,139,139));
			}
			if (value != null){
				if (Integer.parseInt(value.toString()) == realDay && currentMonth == realMonth && currentYear == realYear){ //Today
					setBackground(new Color(32,178,170));
				}
			}
			setBorder(null);
			setForeground(Color.black);
			return this;  
		}
	}

	static class btnPrev_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentMonth == 0){ //If moving it back one month in jaunary it should give december of last year
				currentMonth = 11;
				currentYear -= 1;
			}
			else{ //Back one month
				currentMonth -= 1;
			}
			refreshCalendar(currentMonth, currentYear);
		}
	}
	static class btnNext_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentMonth == 11){ 
				currentMonth = 0;
				currentYear += 1;
			}
			else{ 
				currentMonth += 1;
			}
			refreshCalendar(currentMonth, currentYear);
		}
	}
	static class cmbYear_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (cmbYear.getSelectedItem() != null){
				String b = cmbYear.getSelectedItem().toString();
				currentYear = Integer.parseInt(b);
				refreshCalendar(currentMonth, currentYear);
			}
		}
	}
}

