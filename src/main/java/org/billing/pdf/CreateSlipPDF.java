package org.billing.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;


import org.billing.configuration.EnvironmentConfig;
import org.billing.pagepojo.ProductPojo;
import org.billing.pagepojo.ScrapPojo;
import org.billing.pagepojo.TransactionsPojo;
import org.billing.pages.MessageScreen;
import org.billing.utility.CommonUtils;
import org.billing.utility.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

public class CreateSlipPDF {
    public String fullFilePath = null;

    public void createPdf(TransactionsPojo transactionsPojo) {
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);

        float margins = 25f;

        String filename = SystemUtils.getStringForFileName(transactionsPojo.getCustomerName() + "_" + transactionsPojo.getTransactionDateTime());
        fullFilePath = EnvironmentConfig.homeDirectoryPath + filename + ".pdf";

        try {
            File file = new File(fullFilePath);
            if (file.exists())
                file.delete();
            file.createNewFile();

            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(PageSize.A5);
            Document document = new Document(pdf);
            document.setMargins(margins, margins, margins, margins);


            Table mainTable = new Table(5);
            setMainTableFormatting(mainTable);

            Cell headingCell = getCell(0, 4).setWidth(300).setPaddingLeft(5);
            Paragraph shopName = new Paragraph(EnvironmentConfig.shopName.toUpperCase());
            shopName.setFontSize(23).setBold();

            Paragraph address = new Paragraph(EnvironmentConfig.shopAddress + " Mob: " + EnvironmentConfig.mobileNumber);
            address.setPaddingBottom(10).setBold();

            headingCell.add(shopName);
            headingCell.add(address);

            mainTable.addCell(headingCell);

            Cell logoCell = getCell().setPaddingBottom(10);
            Image image = new Image(ImageDataFactory.create(EnvironmentConfig.logoImagePathForSlip));
            image.setHeight(30);
            image.setWidth(55);
            image.setAutoScale(false);
            logoCell.add(image);
            logoCell.setTextAlignment(TextAlignment.LEFT);
            mainTable.addCell(logoCell);

            // Add Customer Number
            mainTable.addCell(setHeadingCell("Customer : ", 80));
            mainTable.addCell(setValueCell(transactionsPojo.getCustomerName()));

            // Add Date
            mainTable.addCell(getCell(transactionsPojo.getTransactionDateOnly()).setWidth(80));

            // Add Address
            mainTable.addCell(setHeadingCell("Address : ", 80));
            mainTable.addCell(setValueCell(transactionsPojo.getAddress()));
            mainTable.addCell(getCell());

            Table productTable = new Table(6);
            setTableFormatting(productTable);
            Cell productListHeading = getCell("List of Products", 0, 6).setBold();
            productTable.addCell(productListHeading);
            productTable.addCell(setTableHeadingCell("#").setWidth(20));
            productTable.addCell(setTableHeadingCell("Product Name").setWidth(150));
            productTable.addCell(setTableHeadingCell("QTY"));
            productTable.addCell(setTableHeadingCell("Rate"));
            productTable.addCell(setTableHeadingCell("GST%"));
            productTable.addCell(setTableHeadingCell("Total"));


            List<ProductPojo> product = transactionsPojo.getProductsAsPojo();
            for (ProductPojo productPojo : product) {
                productTable.addCell(setTableValueCell(productPojo.getId(), true).setWidth(20));
                productTable.addCell(setTableValueCell(productPojo.getProductName(), false).setWidth(150));
                productTable.addCell(setTableValueCell(productPojo.getQuantity() + " " + productPojo.getQuantityType(), false));
                productTable.addCell(setTableValueCell(productPojo.getRate(), false));
                productTable.addCell(setTableValueCell(productPojo.getGst(), false));
                productTable.addCell(setTableValueCell(decimalFormat.format(CommonUtils.convertToNumeric(productPojo.getTotal())), false));
            }
            mainTable.addCell(getCell(0, 5).add(productTable));

            List<ScrapPojo> scrapPojos = transactionsPojo.getScrapsAsPojo();

            if (scrapPojos.size() > 0) {
                Table scrapTable = new Table(5);
                setTableFormatting(scrapTable);
                Cell scrapListHeading = getCell("List of Scraps", 0, 5).setBold();
                scrapTable.addCell(scrapListHeading);
                scrapTable.addCell(setTableHeadingCell("#").setWidth(20));
                scrapTable.addCell(setTableHeadingCell("Scrap Name").setWidth(150));
                scrapTable.addCell(setTableHeadingCell("QTY"));
                scrapTable.addCell(setTableHeadingCell("Rate"));
                scrapTable.addCell(setTableHeadingCell("Total"));

                for (ScrapPojo scrapPojo : scrapPojos) {
                    scrapTable.addCell(setTableValueCell(scrapPojo.getId(), true));
                    scrapTable.addCell(setTableValueCell(scrapPojo.getProductName(), false).setWidth(150));
                    scrapTable.addCell(setTableValueCell(scrapPojo.getQuantity() + " " + scrapPojo.getQuantityType(), false));
                    scrapTable.addCell(setTableValueCell(scrapPojo.getRate(), false));
                    scrapTable.addCell(setTableValueCell(decimalFormat.format(CommonUtils.convertToNumeric(scrapPojo.getTotal())), false));
                }
                mainTable.addCell(getCell(0, 5).add(scrapTable));
            }

            // Table for Calculation of all transactions
            Table transactionTable = new Table(4);
            setTableFormatting(transactionTable);

            Double shopTotal = transactionsPojo.getTotalOfProduct() + transactionsPojo.getOldCredit();
            Double customerTotal = transactionsPojo.getTotalOfScrap() + transactionsPojo.getCustomerAmount();

            transactionTable.addCell(setTotalTableHeadingCell("Product Total :"));
            transactionTable.addCell(setTotalTableValueCell(decimalFormat.format(transactionsPojo.getTotalOfProduct())));

            transactionTable.addCell(setTotalTableHeadingCell("Scrap Total :"));
            transactionTable.addCell(setTotalTableValueCell(decimalFormat.format(transactionsPojo.getTotalOfScrap())));

            transactionTable.addCell(setTotalTableHeadingCell("Discount :"));
            transactionTable.addCell(setTotalTableValueCell(decimalFormat.format(transactionsPojo.getDiscount())));

            transactionTable.addCell(setTotalTableHeadingCell("Deposit :"));
            transactionTable.addCell(setTotalTableValueCell(decimalFormat.format(transactionsPojo.getCustomerAmount())));

            transactionTable.addCell(setTotalTableHeadingCell("Old Credit:"));
            transactionTable.addCell(setTotalTableValueCell(decimalFormat.format(transactionsPojo.getOldCredit())));


            transactionTable.addCell(getCell(0, 2));

            transactionTable.addCell(setTotalTableHeadingCell("Shop total :"));
            transactionTable.addCell(setTotalTableValueCell(decimalFormat.format(shopTotal)));

            transactionTable.addCell(setTotalTableHeadingCell("Customer total :"));
            transactionTable.addCell(setTotalTableValueCell(decimalFormat.format(customerTotal)));

            Cell totalHeading = getCell("Total [Shop - Customer] :", 0, 2).setPaddingTop(10);
            totalHeading.setBold();
            transactionTable.addCell(totalHeading);
            Cell total = getCell(decimalFormat.format(shopTotal - customerTotal), 0, 2).setPaddingTop(10);
            transactionTable.addCell(total);

            mainTable.addCell(getCell(0, 5).add(transactionTable));

            // Adding Thank you for your business
            Cell thankYouLabel = getCell("Thank you for your business", 0, 5).setPaddingTop(20);
            mainTable.addCell(thankYouLabel);
            document.add(mainTable);
            document.close();

            Path pdfFilePath = Paths.get(fullFilePath);
            SystemUtils.executeCommand("start chrome " + pdfFilePath.toString());
        } catch (Exception exception) {
            MessageScreen.showMessage("Error Occurred while creating Slip PDF");
            exception.printStackTrace();
        }
    }


    public void setMainTableFormatting(Table table) throws IOException {
        table.setBorder(null);
        table.setWidth(365);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER);
        table.setFont(font);
        table.setFontSize(10);
    }

    public void setTableFormatting(Table table) {
        table.setWidth(360);
        table.setBorder(Border.NO_BORDER);
        table.setPaddingTop(20f);
        table.setFontSize(10);
        table.setMarginTop(10);

    }

    public Cell setHeadingCell(String value, int width) {
        Cell cell = new Cell();
        cell.add(new Paragraph(value));
        cell.setPaddingLeft(5);
        cell.setWidth(width);
        cell.setBorder(Border.NO_BORDER);
        cell.setBold();
        return cell;
    }

    public Cell setValueCell(String value) {
        Cell cell = new Cell(0, 3);
        cell.add(new Paragraph(value));
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    public Cell setTotalTableHeadingCell(String value) throws IOException {
        Cell cell = new Cell();
        cell.setWidth(100);
        cell.add(new Paragraph(value));
        cell.setBorder(Border.NO_BORDER);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER);
        cell.setFont(font);
        cell.setBold();
        return cell;
    }

    public Cell setTotalTableValueCell(String value) throws IOException {
        Cell cell = new Cell();
        cell.add(new Paragraph(value));
        cell.setBorder(Border.NO_BORDER);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER);
        cell.setFont(font);
        return cell;
    }

    public Cell setTableHeadingCell(String value) {
        Cell cell = new Cell();
        cell.add(new Paragraph(value));
        cell.setFontColor(ColorConstants.WHITE);
        cell.setBackgroundColor(ColorConstants.GRAY);
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setBold();
        return cell;
    }

    public Cell setTableValueCell(String value, boolean textAlignmentCenter) {
        Cell cell = new Cell();
        cell.add(new Paragraph(value));
        cell.setFontColor(ColorConstants.DARK_GRAY);
        cell.setBorder(Border.NO_BORDER);
        if (textAlignmentCenter)
            cell.setTextAlignment(TextAlignment.CENTER);
        cell.setBold();
        return cell;
    }


    public Cell getCell() {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    public Cell getCell(String value) {
        Cell cell = new Cell();
        cell.add(new Paragraph(value));
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    public Cell getCell(int rowspan, int colspan) {
        Cell cell = new Cell(rowspan, colspan);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    public Cell getCell(String value, int rowspan, int colspan) {
        Cell cell = new Cell(rowspan, colspan);
        cell.add(new Paragraph(value));
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

}