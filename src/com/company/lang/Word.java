package com.company.lang;

public class Word {
	public static final int COUNT_ACCENT = 2;

	private String wordOrigin;      //слово
	private String accentHiragana;  //чтение в хирагане (Кунъёми)
	private String accentKatakana;  //чтение в катакане (Онъёми)
	private String translate;       //перевод

	public Word(String wordOrigin, String accentHiragana, String accentKatakana, String translate) {
		this.wordOrigin = wordOrigin.trim();
		this.accentHiragana = accentHiragana.trim();
		this.accentKatakana = accentKatakana.trim();
		this.translate = translate.trim();
	}

	public String getWordOrigin() {
		return wordOrigin;
	}

	public void setWordOrigin(String wordOrigin) {
		this.wordOrigin = wordOrigin;
	}

	public String getAccentHiragana() {
		return accentHiragana;
	}

	public void setAccentHiragana(String accentHiragana) {
		this.accentHiragana = accentHiragana;
	}

	public String getAccentKatakana() {
		return accentKatakana;
	}

	public void setAccentKatakana(String accentKatakana) {
		this.accentKatakana = accentKatakana;
	}

	public String getTranslate() {
		return translate;
	}

	public void setTranslate(String translate) {
		this.translate = translate;
	}
    //Получение всех чтений
	public String[] getAccents() {
		return new String[]{accentHiragana, accentKatakana};
	}
    //Количество чтений
	public int getCountAccent() {
		return COUNT_ACCENT;
	}

    //Проверка на заполненность
	public boolean isAnyEmpty() {
		return wordOrigin.isEmpty() || accentHiragana.isEmpty() || accentKatakana.isEmpty() || translate.isEmpty();
	}
}
