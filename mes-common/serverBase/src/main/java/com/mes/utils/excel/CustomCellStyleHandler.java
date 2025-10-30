package com.mes.utils.excel;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 * @Author : zhoush
 * @Date: 2025/5/12 20:32
 * @Description:
 */
public class CustomCellStyleHandler implements CellWriteHandler {

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        // 获取单元格
        Cell cell = context.getCell();
        CellStyle cellStyle = cell.getCellStyle();

        // 设置水平和垂直居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        cellStyle.setFont(new XSSFFont());

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        // 设置样式回到单元格
        cell.setCellStyle(cellStyle);
    }
}
