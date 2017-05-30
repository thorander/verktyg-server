package service;

import entity.Question;
import entity.Test;
import entity.useranswers.UAnswer;
import entity.useranswers.UQuestion;
import entity.useranswers.UTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;

/**
 * Created by Markus on 2017-05-24.
 */
public class UTestService {

    EntityManagerFactory emf;
    EntityManager em;

    private UTest test;

    public UTestService(){
        emf = Persistence.createEntityManagerFactory("JPAVerktyg");
        em = emf.createEntityManager();
    }

    public void setTest(UTest t){
        test = t;
    }

    public UTest getTest(){
        return test;
    }

    public void addQuestion(UQuestion q){
        test.addQuestion(q);
    }

    public void persistTest(){
        System.out.println("Persisting test");
        if(test.getTestAnswered().isSelfcorrecting()){
            autocorrect();
        }
        em.getTransaction().begin();
        em.persist(test);
        em.getTransaction().commit();
    }

    public EntityManager getEm(){
        return em;
    }

    private void autocorrect(){
        int points = 0;
        for(Object q : test.getQuestions()){
            UQuestion temp = ((UQuestion)q);
            switch(temp.getQuestion().getQuestionType()){
                case "One choice":
                   if (((UAnswer)temp.getUserAnswers().get(0)).getAnswer().isCorrect()){
                       temp.setScore(temp.getQuestion().getScore());
                       points += temp.getScore();
                   }
                   break;
                case "Multiple choice":
                    boolean allCorrect = correctMultipleChoice(temp);

                    if(allCorrect){
                        temp.setScore(temp.getQuestion().getScore());
                        points += temp.getScore();
                    }
                    break;
                case "Order":
                    boolean allCorrectOrder = true;
                    for(Object o : temp.getUserAnswers()){
                        if (((UAnswer)o).getAnswer().getAnswerOrder() != ((UAnswer)o).getAnswerOrder()){
                            allCorrectOrder = false;
                            break;
                        }
                    }
                    if(allCorrectOrder){
                        temp.setScore(temp.getQuestion().getScore());
                        points += temp.getScore();
                    }
                    break;
            }
        }
        test.setCorrected(true);
        test.setShowResults(true);
        test.setScore(points);
        test.setComment("This test was automatically corrected.");
        double percentage = ((double)points) / ((double)test.getTestAnswered().getMaxPoints());
        if(percentage > 0.9){
            test.setGrade("VG");
        } else if (percentage > 0.6){
            test.setGrade("G");
        } else {
            test.setGrade("IG");
        }
    }

    private boolean correctMultipleChoice(UQuestion temp){
        for(Object o : temp.getUserAnswers()){
            if(((UAnswer)o).getAnswer().isCorrect() != ((UAnswer)o).isChecked()){
                return false;
            }
        }
        return true;
    }

    public String testList() {

        String s = "UTEST#";
        TypedQuery<UTest> query =
                em.createQuery("SELECT c FROM UTest c", UTest.class);

        List<UTest> utest = query.getResultList();

        for (UTest u : utest) {
            s += "#" + u.getUTestId() + "#" + u.getTestAnswered().getTitle();
        }
        return s;
    }

    public String getUTest() {

        String s = "UTEST#";
        TypedQuery<UTest> query =
                em.createQuery("SELECT c FROM UTest c WHERE c.UTestId = :id", UTest.class);
        test = query.setParameter("id", 109).getSingleResult();

        s += test.getUTestId() + "#" + test.getTestAnswered().getTitle();


        for (Object temp : test.getQuestions()) {
            UQuestion question = (UQuestion) temp;
            s += "#UQUESTION";
            s += "#" + question.getUQuestionId() + "#" + question.getQuestion().getQuestion() + "#" + question.getQuestion().getScore();
            for (Object answerTemp : question.getUserAnswers()) {
                UAnswer answer = (UAnswer) answerTemp;
                s += "#ANSWER" + "#" + answer.getAnswer().getAnswer() + "#" + answer.getAnswer().isCorrect() + "#" + answer.isChecked();
            }
        }
        return s;
    }

}
