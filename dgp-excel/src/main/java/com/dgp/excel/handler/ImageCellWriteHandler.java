package com.dgp.excel.handler;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.Units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ImageCellWriteHandler implements CellWriteHandler {

    private final HashMap<String, List<ImageData>> imageDataMap = new HashMap<>(16);

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, WriteCellData<?> cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (isHead) {
            return;
        }
        // 将单元格图片数据复制出来，清空单元格图片数据
        if (!CollectionUtils.isEmpty(cellData.getImageDataList())) {
            imageDataMap.put(cell.getRowIndex() + "_" + cell.getColumnIndex(), cellData.getImageDataList());
            cellData.setType(CellDataTypeEnum.EMPTY);
            cellData.setImageDataList(new ArrayList<>());
        }
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (isHead || CollectionUtils.isEmpty(cellDataList)) {
            return;
        }
        String key = cell.getRowIndex() + "_" + cell.getColumnIndex();
        List<ImageData> imageDataList = imageDataMap.get(key);
        if (CollectionUtils.isEmpty(imageDataList)) {
            return;
        }

        // 设置单元格行高和列宽
        Sheet sheet = cell.getSheet();
        sheet.getRow(cell.getRowIndex())
                .setHeight((short) 900);
        sheet.setColumnWidth(cell.getColumnIndex(), 250 * 50);
        // 插入图片
        for (int i = 0; i < imageDataList.size(); i++) {
            ImageData imageData = imageDataList.get(i);
            if (Objects.isNull(imageData)) {
                continue;
            }
            byte[] image = imageData.getImage();
            this.insertImage(sheet, cell, image, i);
        }
        imageDataMap.remove(key);
    }

    private void insertImage(Sheet sheet, Cell cell, byte[] pictureData, int i) {
        // 图片宽度
        int pictureWidth = Units.pixelToEMU(150);
        int index = sheet.getWorkbook().addPicture(pictureData, HSSFWorkbook.PICTURE_TYPE_PNG);
        Drawing<?> drawing = sheet.getDrawingPatriarch();
        if (Objects.isNull(drawing)) {
            drawing = sheet.createDrawingPatriarch();
        }
        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();
        // 设置图片在哪个单元格中
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex());
        anchor.setRow1(cell.getRowIndex());
        anchor.setRow2(cell.getRowIndex() + 1);
        // 设置图片在单元格中的位置
        anchor.setDx1(pictureWidth * i);
        anchor.setDx2(pictureWidth + pictureWidth * i);
        anchor.setDy1(0);
        anchor.setDy2(0);
        // 设置图片可以随着单元格移动
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
        drawing.createPicture(anchor, index);
    }
}
