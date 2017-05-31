package service;

import core.Main;
import entity.Question;
import entity.Test;
import entity.User;
import entity.useranswers.UAnswer;
import entity.useranswers.UQuestion;
import entity.useranswers.UTest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Markus on 2017-05-24.
 */
public class UTestService {

    EntityManagerFactory emf;
    EntityManager em;

    private UTest test;
    private String testname;
    private String id;

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
                    boolean allCorrectSingle = correctMultipleChoice(temp);
                   if (allCorrectSingle){
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

    public String getTestList() {

        String s = "GETTESTLIST";
        TypedQuery<Test> query =
                em.createQuery("SELECT c FROM Test c", Test.class);

        List<Test> utest = query.getResultList();
        if(utest.size() > 0){
            for (Test u : utest) {
                s += "#" + u.getTestId() + "#" + u.getTitle();
            }
            return s;
        }
        return "nothing";
    }

    public String getUserList() {
        TypedQuery<User> studentQuery = em.createNamedQuery("User.findUserWhoTookTest", User.class);

        String temp = "GETTESTUSER";
        try{
            ArrayList<User> userResults = new ArrayList<>(studentQuery.setParameter("testTitle", testname).getResultList());
            for(User u : userResults){
                temp += "#" + u.getFirstName() + " " + u.getLastName() + "#" + u.getUid();
            }
        } catch (NoResultException e){

        }
        return temp;
    }

    public String setTestname(String s) {
        this.testname = s;
        System.out.println(testname);
        return testname;

    }

    public String setUserId(String i) {
        this.id = i;
        System.out.println(i);
        return i;
    }

    public String getUTest() {

        String s = "UTEST#";
        TypedQuery<UTest> query =
                em.createQuery("SELECT c FROM UTest c JOIN User u WHERE c MEMBER OF u.takenTests AND u.uid = :uid AND c.testAnswered.title = :testName", UTest.class);
        try{
            test = query.setParameter("testName", testname).setParameter("uid", Integer.parseInt(id)).getSingleResult();
        } catch (NoResultException e){
            System.out.println(e);
            return "nothing#";
        }

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
