package service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PdfWriter;
import core.Connection;
import core.Main;
import entity.Answer;
import entity.Question;
import entity.Test;
import entity.User;
import entity.useranswers.UAnswer;
import entity.useranswers.UQuestion;
import entity.useranswers.UTest;


import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * A service class for creating PDF's
 */

public class PdfService {
    private UTest test;
    private UAnswer answer;
    private Question question;
    private TestService ts;
    private UTestService us;
    private Connection c;

    public PdfService() {
    }

    public void createPdf() {
        Document document = new Document();
        test = new UTest();
        ts = new TestService();


        Font red = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
        Font green = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GREEN);
        Font myContentStyle = new Font();

        try {

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("testar.pdf"));
            document.open();
            String s ="COMBOBOX#";

            UTest test = ts.getUserTest(214);
            document.addTitle("PDF TEST");
            myContentStyle.setStyle("underline");
            document.add(new Paragraph("Title: " + test.getTestAnswered().getTitle(), myContentStyle));
            Paragraph result = new Paragraph("Your result is...!");
            result.setSpacingAfter(20f);
            document.add(result);
            for (int i = 0; i < test.getQuestions().size(); i++) {
                UQuestion tempQuestion = ((UQuestion) test.getQuestions().get(i));
                Paragraph p = new Paragraph(("Question: " + tempQuestion.getQuestion().getQuestion()));
                document.add( p );


                for (int j = 0; j < tempQuestion.getUserAnswers().size(); j++) {

                    UAnswer tempAnswer = ((UAnswer) tempQuestion.getUserAnswers().get(j));

                    if (tempAnswer.getAnswer().isCorrect()) {
                        document.add(new Paragraph("Answers: " + tempAnswer.getAnswer().getAnswer(), green));
                    } else {
                        document.add(new Paragraph("Answers: " + tempAnswer.getAnswer().getAnswer(), red));

                    }
                }

            }

            document.close();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}


