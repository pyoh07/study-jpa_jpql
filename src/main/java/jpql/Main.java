package jpql;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {

        // Entity Manager Factory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        // Entity Manager
        EntityManager em = emf.createEntityManager();
        //Transaction 생성
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        //code
        try{
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);
            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);


            em.flush();
            em.clear();

            //내부조인
            /*String query = "select m from Member m inner join m.team t";
            List<Member> resultList = em.createQuery(query, Member.class)
                    .getResultList();*/
            //외부조인
            /*String query2 = "select m from Member m left join m.team t";
            List<Member> resultList2 = em.createQuery(query2, Member.class)
                    .getResultList();*/
            //세타조인
            /*String query3 = "select m from Member m, Team t where m.username = t.name";
            List<Member> resultList3 = em.createQuery(query3, Member.class)
                    .getResultList();

            System.out.println("size : " + resultList3.size());*/

            //hibernate 5.1 이상부터 조인대상 필터링 가능
            // ex-jpql) select m, t from Member m left join m.team t on t.name='a'
            // ex-sql ) select m.*, t.* from Member m left join Team t on m.TEAM_ID=t.id and t.name='A'
            //연관관계없는 엔티티 외부 조인 가능
            // ex-jpql) select m, t from Member m left join Team t on m.username = t.name
            // ex-sql ) select m.* t.* from Member m left join Team t on m.username = t.name

            //서브쿼리
            // FROM 절에서는 서브쿼리 사용 불가. JOIN 으로 해결하거나 application 에서 데이터 가공방식, 또는 native 로 해결할것.

            //타입 표현식
            /*String query = "select m.username, 'HELLO', TRUE from Member m "
                    + "where m.memberType = jpql.MemberType.ADMIN";
            List<Object[]> resultList = em.createQuery(query, Object[].class)
                    .getResultList();
            for(Object[] objects : resultList){
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
            }*/
            //타입 표현식
            /*String query = "select m.username, 'HELLO', TRUE from Member m "
                    + "where m.memberType = :userType "
                    + "AND m.username is not NULL";
            List<Object[]> resultList = em.createQuery(query, Object[].class)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();
            for(Object[] objects : resultList){
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
            }*/
            //상속관계 엔티티 타입 조회 -> DTYPE 값을 @DiscriminatorColumn 값과 일치한지 조회함.
            //em.createQuery("select i from Item i where type(i) = Book ", Item.class);

            //조건식-CASE 식
            /*String query =
                    "select " +
                            "case when m.age <=10 then '학생요금' " +
                            "   when m.age >=60 then '경로요금' " +
                            "   else '일반요금' " +
                            "end " +
                            "from Member m";
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();
            for(String s : result){
                System.out.println("s = " + s);
            }*/

            //COALESCE
            /*String query =
                    "select coalesce(m.username, '이름 없는 회원') as username " +
                            "from Member m ";
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();
            for(String s : result){
                System.out.println("s = " + s);
            }*/
            //NULLIF
            /*String query =
                    "select nullif(m.username, '관리자') as username " +
                            "from Member m ";
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();
            for(String s : result){
                System.out.println("s = " + s);
            }*/

            //JPQL 기본함수
            /*String query =
                    "select 'a' || 'b' From Member m";
            //"select substring(m.username, 2, 3) From Member m";
            // ... CONCAT, TRIM, LOWER, UPPER, LENGTH, LOCATE, ABS, SQRT, MOD
            //"select locate('de', 'abcdefg') From Member m"; -> Type Integer.class
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();
            for(String s : result){
                System.out.println("s = " + s);
            }*/
            //LOCATE
            /*String query =
                    "select locate('de', 'abcdefg') From Member m";
            List<Integer> result = em.createQuery(query, Integer.class)
                    .getResultList();
            for(Integer s : result){
                System.out.println("s = " + s);
            }*/

            //SIZE - 컬렉션 크기
            /*String query =
                    "select size(t.members) From Team t";
            List<Integer> result = em.createQuery(query, Integer.class)
                    .getResultList();
            for(Integer s : result){
                System.out.println("s = " + s);
            }*/

            //INDEX - @OrderColumn 리스트 값타입 컬렉션 위치값구할때 ; 사용 권장X
            /*String query =
                    "select index(t.members) From Team t";
            List<Integer> result = em.createQuery(query, Integer.class)
                    .getResultList();
            for(Integer s : result){
                System.out.println("s = " + s);
            }*/

            //사용자 정의함수 호출 - Dialect 를 만들어서 미리 등록해 두어야함.
            //1. 데이터베이스에 맞게 ex) H2Dialect 를 상속받은 MyH2Dialect 를 생성
            //2. registerFunction 으로 등록
            //3. 세팅에 H2Dialect 말고 MyH2Dialect 를 dialect 로 등록
            //아래 사용법 예시
            /*String query =
                    //"select function('group_concat', m.username) From Member m";
                    //"select group_concat(m.username) From Member m";
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();
            for(String s : result){
                System.out.println("s = " + s);*/

            tx.commit();

        } catch (Exception e){
            tx.rollback();
            System.out.println("e = " + e);
        } finally {
            em.close();
        }

        emf.close();
    }

}