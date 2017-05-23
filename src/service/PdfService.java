package service;

/**
 * Created by phili on 2017-05-23.
 */
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

        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("testar.pdf"));
            document.open();
            document.add(new Paragraph("Här har du en liten pdf"));
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
