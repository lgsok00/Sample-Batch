package com.example.samplebatch.batch;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelRowReader implements ItemStreamReader<Row> {

  // 파일 경로
  private final String filePath;
  // 파일 스트림
  private FileInputStream fileInputStream;
  // 엑셀 파일
  private Workbook workbook;
  // 엑셀 파일 시트 내부에서 행을 가져오기 위한 Iterator
  private Iterator<Row> rowCursor;

  public ExcelRowReader(String filePath) throws IOException {
    this.filePath = filePath;
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {
    try {
      fileInputStream = new FileInputStream(filePath);  // 파일 경로를 받아 fileInputStream 생성
      workbook = WorkbookFactory.create(fileInputStream); // WorkbookFactory 를 통해 해당 파일을 workbook 객체로 생성
      Sheet sheet = workbook.getSheetAt(0); // Sheet 가져오기
      this.rowCursor = sheet.iterator();

      // 첫 행을 제목이므로 넘어가기
      if (rowCursor.hasNext()) {
        rowCursor.next();
      }
    } catch (IOException e) {
      throw new ItemStreamException(e);
    }
  }

  @Override
  public Row read() {
    if (rowCursor != null && rowCursor.hasNext()) {
      return rowCursor.next();
    } else {
      return null;
    }
  }

  @Override
  public void close() throws ItemStreamException {
    try {
      if (workbook != null) {
        workbook.close();
      }
      if (fileInputStream != null) {
        fileInputStream.close();
      }
    } catch (IOException e) {
      throw new ItemStreamException(e);
    }
  }
}
