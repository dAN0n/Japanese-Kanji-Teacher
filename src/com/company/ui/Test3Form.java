package com.company.ui;

import com.company.lang.Word;
import com.company.lang.WordDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Test3Form extends JFrame {
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
    private int sizeWordList;
    private int prevIndexWord = -1;

    public Test3Form() throws HeadlessException {
        super("Translate Test");
        install();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(root);
        pack();
        setSize(600, 260);
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
        Word[] words = new Word[]{getRandomWord(true), getRandomWord(false), getRandomWord(false), getRandomWord(false)};
        //Получаем рандомное чтение
        int numAccent = new Random().nextInt(words[0].getCountAccent());
        String mes = words[0].getAccents()[numAccent];
//        numAccent = (numAccent == 1) ? 0 : 1;
        //Тип задания: поиск перевода или иероглифа
        int RandomCounter = new Random().nextInt(2);
        if(RandomCounter == 0)
            for (int i = 0; i < textButton.length; i++)
                textButton[i] = words[i].getTranslate();
        else for (int i = 0; i < textButton.length; i++)
            textButton[i] = words[i].getWordOrigin();

        setTextButton(textButton);
        accentLabel.setText(mes);
        if(RandomCounter == 0) wordLabel.setText(words[0].getWordOrigin());
        else wordLabel.setText(words[0].getTranslate());
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
