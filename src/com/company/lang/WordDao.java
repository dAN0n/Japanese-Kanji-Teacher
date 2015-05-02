package com.company.lang;

//Apache POI Library
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WordDao {
	private static WordDao dao;
	private static final String FILE_DICTIONARY_NAME = "Kanji.xls"; //Исходник словаря

	public WordDao() {
	}

	public static WordDao getInstance() {
		if (dao == null) dao = new WordDao();
		return dao;
	}

//Образование словаря из файла
	public synchronized List<Word> getListWord() throws IOException {
		List<Word> words = new ArrayList<>();
		try (FileInputStream in = new FileInputStream(FILE_DICTIONARY_NAME)) {
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row;
//Заполнение списка слов
			for (int i = 1, l = sheet.getPhysicalNumberOfRows(); i < l; i++) {
				row = sheet.getRow(i);
				if (row == null || isEmptyRow(row)) continue;
				words.add(
						new Word(
								row.getCell(0).getStringCellValue(),
								row.getCell(1).getStringCellValue(),
								row.getCell(2).getStringCellValue(),
								row.getCell(3).getStringCellValue()
						)
				);
			}
		}
		return words;
	}

//Добавление слова в словарь
	public synchronized WordDao addWord(Word word) throws IOException {
		HSSFWorkbook workbook;
		try (
				FileInputStream in = new FileInputStream(FILE_DICTIONARY_NAME)
		) {
			workbook = new HSSFWorkbook(in);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row;
//Создание строки с новым словом
			for (int i = 1, l = sheet.getPhysicalNumberOfRows(); i < l; i++) {
				row = sheet.getRow(i);
				if (row == null || isEmptyRow(row)) {
					sheet.createRow(i);
					readWordAt(row, word);
					break;
				} else if (i == l - 1) {
					sheet.createRow(l);
					readWordAt(sheet.getRow(l), word);
					break;
				}
			}
		}
//Перезапись исходника
		try (FileOutputStream out = new FileOutputStream(FILE_DICTIONARY_NAME)) {
			workbook.write(out);
		}
		return this;
	}

//Удаление слова из словаря
	public synchronized WordDao removeWord(Word word) throws IOException {
		HSSFWorkbook workbook;
		try (
				FileInputStream in = new FileInputStream(FILE_DICTIONARY_NAME)
		) {
			workbook = new HSSFWorkbook(in);
			HSSFSheet sheet = workbook.getSheetAt(0);
            //Поиск удаляемого слова по кандзи и удаление
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				HSSFCell cell = sheet.getRow(i).getCell(0);
				if (cell != null && cell.getStringCellValue().trim().equals(word.getWordOrigin())) {
					removeRow(sheet, i);
					break;
				}
			}
		}
//Перезапись исходника
		try (FileOutputStream out = new FileOutputStream(FILE_DICTIONARY_NAME)) {
			workbook.write(out);
		}
		return this;
	}

//Процесс удаления
	private static void removeRow(HSSFSheet sheet, int rowIndex) {
		int lastRowNum = sheet.getLastRowNum();
		if (rowIndex >= 0 && rowIndex < lastRowNum) {
            //подъём всех строк на одну строку вверх
			sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
		}
		if (rowIndex == lastRowNum) {
			HSSFRow removingRow = sheet.getRow(rowIndex);
			if (removingRow != null) {
				sheet.removeRow(removingRow);
			}
		}
	}

//Пока есть слова в Words вызываем метод addWord
	public synchronized WordDao addCollectionWords(Collection<Word> words) throws IOException {
		for (Word word : words)
			addWord(word);
		return this;
	}

//Внесение информации в строку с новым словом
	private void readWordAt(HSSFRow row, Word word) {
		row.createCell(0).setCellValue(word.getWordOrigin());
		row.createCell(1).setCellValue(word.getAccentKatakana());
		row.createCell(2).setCellValue(word.getAccentHiragana());
		row.createCell(3).setCellValue(word.getTranslate());
	}

//Проверка на совпадения
	private boolean isEmptyRow(HSSFRow row) {
		return Arrays.stream(
				new HSSFCell[]{
						row.getCell(0),
						row.getCell(1),
						row.getCell(2),
						row.getCell(3)
				})
				.anyMatch(x -> x == null || x.getStringCellValue().trim().isEmpty());
	}
}
