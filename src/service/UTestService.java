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

/**
 * A service class for UTest
 */
public class UTestService {

    EntityManagerFactory emf;
    EntityManager em;

    private UTest test;
    private String testname;
    private String id;

    private List<String> commentList;
    private List<Integer> scoreList;
    String correctComment;
    private int testId;

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

    public String getUTestsForResultPage(User u){
        String result = "UTESTSFORRESULTPAGE#";
        TypedQuery<UTest> query = em.createQuery("SELECT c FROM UTest c JOIN User u WHERE c MEMBER OF u.takenTests AND u = :u", UTest.class);
        try{
            ArrayList<UTest> tests = new ArrayList<>(query.setParameter("u", u).getResultList());
            for(UTest ut : tests){
                result += ut.getTestAnswered().getTitle() + "#" + ut.getUTestId() + "#";
            }
        } catch (NoResultException e){
            System.out.println(e);
        }
        return result;
    }

    public String getUTest(String id){
        String result = "UTESTFORRESULTPAGE#";

        TypedQuery<UTest> query = em.createQuery("SELECT c FROM UTest c WHERE c.UTestId = :id", UTest.class);
        try{
            test = query.setParameter("id", Integer.parseInt(id)).getSingleResult();
            result += test.getTestAnswered().getTitle() + "#"
                    + test.getScore() + "#"
                    + test.getTestAnswered().getMaxPoints() + "#"
                    + test.getGrade() + "#"
                    + test.getComment();
            for (Object temp : test.getQuestions()) {
                UQuestion question = (UQuestion) temp;
                result += "#UQUESTION";
                result += "#" + question.getQuestion().getQuestion() + "#" + question.getScore() + "#" + question.getQuestion().getScore();
                for (Object answerTemp : question.getUserAnswers()) {
                    UAnswer answer = (UAnswer) answerTemp;
                    result += "#ANSWER" + "#" + question.getQuestion().getQuestionType() + "#" + answer.getAnswerText() + "#" + answer.getAnswer().isCorrect() + "#" + answer.isChecked() + "#" + answer.getAnswerOrder();
                }
            }
        } catch (NoResultException e){
            System.out.println(e);
        }

        return result;
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
                s += "#ANSWER" + "#" + question.getQuestion().getQuestionType() + "#" + answer.getAnswerText() + "#" + answer.getAnswer().isCorrect() + "#" + answer.isChecked() + "#" + answer.getAnswerOrder();
            }
        }
        return s;
    }

    public void updateCorrect() {

        StringBuilder builder = new StringBuilder();
        for (String value : commentList) {
            builder.append(value);
        }
        correctComment = builder.toString();
        System.out.println("result: "+correctComment);

        int correctScore = 0;
        for(int i : scoreList) {
            correctScore += i;
        }
        System.out.println("sum: "+correctScore);

        String s = "";
        TypedQuery<UTest> query =
                em.createQuery("SELECT c FROM UTest c JOIN User u WHERE c MEMBER OF u.takenTests AND u.uid = :uid AND c.testAnswered.title = :testName", UTest.class);
                //em.createQuery("SELECT c FROM UTest c JOIN User c WHERE c MEMBER OF u.takenTests AND u.testAnswered.testId = :testId AND u.uid = :uid", UTest.class);
        try{
            test = query.setParameter("testName", testname).setParameter("uid", Integer.parseInt(id)).getSingleResult();

        } catch (NoResultException e){
            System.out.println(e);
        }

        s += test.getUTestId();
        testId = Integer.parseInt(s);
        System.out.println(testId);
        //System.out.println("namn: "+ testname + " id: " +id);

        EntityTransaction updateTransaction = em.getTransaction();
        updateTransaction.begin();
        Query q = em.createQuery("UPDATE UTest u SET u.comment = :comment WHERE u.UTestId = :uid");
        q.setParameter("comment", "Hej");
        q.setParameter("uid", testId);

        int updated = q.executeUpdate();
            if (updated > 0) {
                System.out.println("Done...");
            }
            updateTransaction.commit();
    }


    public String correctComment(String i) {
        commentList = new ArrayList<>();
        commentList.add(i);
        //System.out.println("comment: "+commentList);

        return "nothing";
    }


    public int correctScore(Integer x) {
        scoreList = new ArrayList<>();
        scoreList.add(x);
        //System.out.println("score: "+scoreList);

        return 0;
    }

}


