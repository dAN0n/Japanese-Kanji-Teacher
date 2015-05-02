package com.company.ui;

import com.company.lang.Word;
import com.company.lang.WordDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Test1Form extends JFrame {
	public static final int COUNT_PAGES = 10;

	private int rightIndexButton = -1;
	private int currentCountPage = -1;
	private int countRightAnswer;
	private JPanel root;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton[] buttons = new JButton[]{button1, button2, button3, button4};
	private JLabel accentLabel;
	private JLabel wordLabel;
	private JLabel infoLabel;
	private List<Word> wordList;
	private Set<Character> setCharHiragana = new HashSet<>(), setCharKatakana = new HashSet<>();
	private Set<Character>[] sets = new Set[]{setCharHiragana, setCharKatakana};
	private int sizeWordList;
	private int prevIndexWord = -1;

	public Test1Form() throws HeadlessException {
		super("Gap Test");
		install();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setContentPane(root);
		pack();
		setSize(300, 260);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		setVisible(true);
	}

	public void install() {
		try {
//список слов
			wordList = WordDao.getInstance().getListWord();
//количество слов
			sizeWordList = wordList.size();
//список символов слоговых азбук
			for (Word word : wordList) {
				for (char c : word.getAccentHiragana().toCharArray()) setCharHiragana.add(c);
				for (char c : word.getAccentKatakana().toCharArray()) setCharKatakana.add(c);
			}
		} catch (IOException e) {
			dispose();
		}
//количество верных ответов
		countRightAnswer = 0;

		button1.addActionListener(getButtonActionListener(0));
		button2.addActionListener(getButtonActionListener(1));
		button3.addActionListener(getButtonActionListener(2));
		button4.addActionListener(getButtonActionListener(3));
//генерация упражнения
		nextPageTest();
	}

	public void nextPageTest() {
		currentCountPage++;
		updateInfo();
		if (currentCountPage == COUNT_PAGES)
			showEndMessage();
		else if (currentCountPage > COUNT_PAGES)
			dispose();
		String[] textButton = new String[4];

		Word word = getRandomWord(true);
        //Получаем рандомное чтение
		int numAccent = new Random().nextInt(word.getCountAccent());
		String mes = word.getAccents()[numAccent];
        //Выбираем случайный символ строки
		int tmp = new Random().nextInt(mes.length());
        //Запоминаем символ для ответа
		textButton[0] = String.valueOf(mes.charAt(tmp));
        //Создаём строку с пропуском
		mes = mes.substring(0, tmp) + "_" + mes.substring(tmp + 1);
        //Удаляем правильный символ из базы
		List<Character> characterList = new ArrayList<>(sets[numAccent]);
		characterList.remove(new Character(textButton[0].charAt(0)));
        //Создаём неправильные ответы
		for (int i = 1; i < textButton.length; i++) {
			textButton[i] = String.valueOf(characterList.get(new Random().nextInt(characterList.size())));
			characterList.remove(new Character(textButton[i].charAt(0)));
		}
		setTextButton(textButton);
		accentLabel.setText(mes);
		wordLabel.setText(word.getWordOrigin());
	}

	public void showEndMessage() {
		String message = "Right answers: " + countRightAnswer + " / " + COUNT_PAGES;
		int res = JOptionPane.showConfirmDialog(null, message, "End", JOptionPane.DEFAULT_OPTION);
		if (res == JOptionPane.OK_OPTION || res == JOptionPane.CLOSED_OPTION)
			dispose();
	}

	public ActionListener getButtonActionListener(int numButton) {
		return e -> {
            for(int i=0; i<4; i++) buttons[i].setBackground(null);
            if (rightIndexButton == numButton){
                countRightAnswer++;
                buttons[numButton].setBackground(Color.GREEN);
            }
            else buttons[numButton].setBackground(Color.RED);
            nextPageTest();
		};
	}

	public void setTextButton(String[] textButton) {
		List<Integer> integers = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
		for (int i = 0; i < textButton.length; i++) {
            //Помещение ответа в случайную кнопку
            int index = (integers.size() != 1) ? new Random().nextInt(integers.size()) : 0;
            //Получение её индекса
            int numButton = integers.get(index);
            //Запомнить индекс правильной кнопки
			if (i == 0) rightIndexButton = numButton;
			buttons[numButton].setText(textButton[i]);
			integers.remove(new Integer(numButton));
		}
	}

	public void updateInfo() {
		infoLabel.setText(String.format("%s / %S / %s", countRightAnswer, currentCountPage, COUNT_PAGES));
	}

	public Word getRandomWord(boolean changePrevIndexWord) {
		int tmp;
        //Исключение повторов
		do {
			tmp = new Random().nextInt(sizeWordList);
		} while (tmp == prevIndexWord);
		if (changePrevIndexWord) prevIndexWord = tmp;
		return wordList.get(tmp);
	}
}
