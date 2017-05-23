package service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import entity.Test;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class PdfService
{
    public static void main(String[] args)
    {
        Document document = new Document();
        Test test = new Test();

        test.setTitle("Hejsan");
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("testar.pdf"));
            document.open();
            document.add(new Paragraph(test.getTitle()));
            document.close();
            writer.close();
        } catch (DocumentException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
