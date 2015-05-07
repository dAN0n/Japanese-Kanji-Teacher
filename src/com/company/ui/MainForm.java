package com.company.ui;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {
	private JButton testButton;
	private JButton dictionaryButton;
	private JPanel root;
	private JButton test2Button;
    private JButton translationTestButton;
    private Test1Form test1Form;
	private Test2Form test2Form;
    private Test3Form test3Form;
	private DictionaryForm dictionaryForm;

	public MainForm() throws HeadlessException {
		super("Japanese Kanji Teacher");
		dictionaryButton.addActionListener(e -> {
            if (dictionaryForm == null || !dictionaryForm.isVisible()) {
                if ((test1Form == null || !test1Form.isVisible())
                        && (test2Form == null || !test2Form.isVisible())
                        && (test3Form == null || !test3Form.isVisible()))
                    dictionaryForm = new DictionaryForm();
            }
		});
		testButton.addActionListener(e -> {
            if (test1Form == null || !test1Form.isVisible()) {
                if ((test2Form == null || !test2Form.isVisible())
                        && (dictionaryForm == null || !dictionaryForm.isVisible())
                        && (test3Form == null || !test3Form.isVisible()))
                    test1Form = new Test1Form();
            }
		});
		test2Button.addActionListener(e -> {
            if (test2Form == null || !test2Form.isVisible()) {
                if ((test1Form == null || !test1Form.isVisible())
                        && (dictionaryForm == null || !dictionaryForm.isVisible())
                        && (test3Form == null || !test3Form.isVisible()))
                    test2Form = new Test2Form();
            }
        });
        translationTestButton.addActionListener(e ->{
            if (test3Form == null || !test3Form.isVisible()) {
                if ((test1Form == null || !test1Form.isVisible())
                        && (dictionaryForm == null || !dictionaryForm.isVisible())
                        && (test2Form == null || !test2Form.isVisible()))
                    test3Form = new Test3Form();
            }
        });

		setContentPane(root);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setSize(300, 180);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		setVisible(true);
	}

	public static void main(String[] args) {
		new MainForm();
	}
}
