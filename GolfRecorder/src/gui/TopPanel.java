package gui;

/**
 * The JPanel containing all Swing components for the program.
 * 
 * @author Arjun Gopisetty, Christopher Hsiao
 */

import game.Course;
import io.FileIO;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TopPanel extends JPanel {

	private static final long serialVersionUID = -1497372163980722353L;

	private JButton back, save, saveAs, nRow, nCol;
	private JButton newCourse;
	private JButton calcRoundIndex;
	private JButton print;
	private JPanel buttonPanel, coursePanel;

	private TablePanel tp;
	private GolfFrame gf;

	/**
	 * Constructs a TopPanel.
	 * 
	 * @param tp Current TablePanel
	 * @param gf Current GolfFrame
	 */
	public TopPanel(TablePanel tp, GolfFrame gf) {
		super();
		this.tp = tp;
		this.gf = gf;
		setLayout(new GridLayout(1, 2));

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 4));

		ButtonHandler bh = new ButtonHandler();
		back = new JButton("<- Back");
		back.setActionCommand("back");
		back.addActionListener(bh);
		save = new JButton("Save");
		save.setActionCommand("save");
		save.addActionListener(bh);
		saveAs = new JButton("Save As");
		saveAs.setActionCommand("save as");
		saveAs.addActionListener(bh);
		nRow = new JButton("New Player");
		nRow.setActionCommand("new player");
		nRow.addActionListener(bh);
		nCol = new JButton("New Game");
		nCol.setActionCommand("new game");
		nCol.addActionListener(bh);

		buttonPanel.add(back, BorderLayout.WEST);
		buttonPanel.add(save, BorderLayout.CENTER);
		buttonPanel.add(nRow, BorderLayout.EAST);
		buttonPanel.add(nCol);
		add(buttonPanel);

		coursePanel = new JPanel();
		newCourse = new JButton("New Course");
		newCourse.addActionListener(new ButtonHandler());
		newCourse.setActionCommand("newCourse");
		calcRoundIndex = new JButton("Calculate Round Index");
		calcRoundIndex.setActionCommand("calcroundindex");
		calcRoundIndex.addActionListener(bh);
		print = new JButton("Print table");
		print.addActionListener(new ButtonHandler());
		print.setActionCommand("print");
		
		coursePanel.add(newCourse);
		coursePanel.add(calcRoundIndex);
		coursePanel.add(print);
		add(coursePanel);
	}

	/**
	 * Represents a class that handles buttons in this panel.
	 */
	class ButtonHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("save")) {			
				String fileName = JOptionPane.showInputDialog("Enter File Name: ");
				if(fileName == null || fileName.equals("")){
					JOptionPane.showMessageDialog(null, "Empty filename! " + fileName, "Notice!", JOptionPane.WARNING_MESSAGE);
				}else {
					FileIO.savePlayers(fileName, tp.dumpData());
					FileIO.saveCourses(tp.getCourses());
					tp.setSave(true);
				}
			} else if (e.getActionCommand().equals("new player")) {
				String name = JOptionPane.showInputDialog("Enter name of player:");
				if(name != null && !name.equals(""))
					tp.addRow(name);
			} else if (e.getActionCommand().equals("new game")) {
				tp.addGame();
			} else if (e.getActionCommand().equals("back")) {
				if (tp.isSaved()) {
					gf.changeScene("MenuPanel");
				} else {
					int a = JOptionPane.showConfirmDialog(gf, "Your table is not saved. \n" 
							+ "Exit without saving?", "Unsaved Progress", JOptionPane.OK_CANCEL_OPTION);
					if (a == 0) {
						gf.changeScene("MenuPanel");
					}
				}
			} else if (e.getActionCommand().equals("newCourse")) {
				String course = JOptionPane.showInputDialog("Enter name of new course:");
				int slope = Integer.parseInt(JOptionPane.showInputDialog("Enter slope: "));
				double courseRating = Double.parseDouble(JOptionPane.showInputDialog("Enter course rating: "));
				tp.addNewCourse(new Course(courseRating, slope, course));
			} else if (e.getActionCommand().equals("calcroundindex")) {
				tp.updateRoundIndex();
				tp.updateFinalAverage();
			} else if(e.getActionCommand().equals("print")) {
				try {
					tp.getTable().print();
				} catch (PrinterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
