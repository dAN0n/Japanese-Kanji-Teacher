package com.company.ui;

import com.company.lang.Word;
import com.company.lang.WordDao;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class DictionaryForm extends JFrame {

//На случай ошибки
	public static final Word WORD_ERROR = new Word("Error", "Error", "Error", "Error");

	private JButton addButton;
	private JLabel wordText;
	private JLabel translateText;
	private JLabel firstAccent;
	private JLabel secondAccent;
	private JPanel root;
	private JList<Object> list1;
	private JButton deleteButton;
	private List<Word> wordList;

	public DictionaryForm() throws HeadlessException {
		super("Dictionary");
		install();

		setContentPane(root);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		setSize(640, 480);
		setVisible(true);
	}

	public void install() {
		try {
			wordList = WordDao.getInstance().getListWord();
		} catch (IOException e) {
			wordList = new ArrayList<>();
			wordList.add(WORD_ERROR);
		}
		DefaultListModel<Object> model = new DefaultListModel<>();
		for (Word aWordList : wordList)
			model.addElement(aWordList.getTranslate());
//Установка списочной модели и реакция на выбор строки
		list1.setModel(model);
		list1.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && list1.getSelectedIndex() >= 0)
				setSelectWordAt(list1.getSelectedIndex());
		});
		list1.setSelectedIndex(0);
//Реакции кнопок
		addButton.addActionListener(e -> showAddWordDialog());
		deleteButton.addActionListener(e-> showDeleteWordDialog());
	}

//Обновление списка
	public void update() {
		try {
			wordList = WordDao.getInstance().getListWord();
		} catch (IOException e) {
			wordList = new ArrayList<>();
			wordList.add(WORD_ERROR);
		}

		DefaultListModel<Object> model = new DefaultListModel<>();
		for (Word aWordList : wordList)
			model.addElement(aWordList.getTranslate());

		list1.setModel(model);
		list1.setSelectedIndex(0);
	}

//Диалог добавления слова
	public void showAddWordDialog() {
//Создание окна
		JTextField[] fields = new JTextField[]{
				new JTextField(),
				new JTextField(),
				new JTextField(),
				new JTextField()
		};
		JLabel[] labels = new JLabel[]{
				new JLabel("Word"),
				new JLabel("Accent Hiragana"),
				new JLabel("Accent Katakana"),
				new JLabel("Translate")
		};
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		for (int i = 0; i < 4; i++) {
			panel.add(labels[i]);
			panel.add(fields[i]);
		}
//Нажатие ОК
		int reply = JOptionPane.showConfirmDialog(null, panel, "Add word", JOptionPane.DEFAULT_OPTION);
		if (reply == JOptionPane.OK_OPTION) {
			Word word = new Word(fields[0].getText(), fields[1].getText(), fields[2].getText(), fields[3].getText());
			if (!word.isAnyEmpty()) {
				try {
					WordDao.getInstance().addWord(word);
				} catch (IOException e) {
					e.printStackTrace();
				}
				update();
			}
		}
	}

//Диалог удаления слова
	public void showDeleteWordDialog() {
//Образование выпадающего списка с переводами слов
		ComboBoxModel<String> model = new DefaultComboBoxModel<>(
				wordList.stream().map(Word::getTranslate).toArray(String[]::new)
		);
		JComboBox<String> jComboBox = new JComboBox<>(model);
//Нажатие ОК
	 	int res = JOptionPane.showConfirmDialog(null, jComboBox, "Delete word", JOptionPane.OK_CANCEL_OPTION);
		if (res == JOptionPane.OK_OPTION) {
			int index = jComboBox.getSelectedIndex();
			try {
				WordDao.getInstance().removeWord(wordList.get(index));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		update();
	}

//Размещение данных слова в окне при выборе
	public void setSelectWordAt(int index) {
		if (index >= wordList.size() || index < 0) throw new IllegalArgumentException();
		Word word = wordList.get(index);
		wordText.setText(word.getWordOrigin());
		firstAccent.setText(word.getAccentHiragana());
		secondAccent.setText(word.getAccentKatakana());
		translateText.setText(word.getTranslate());
	}
}
