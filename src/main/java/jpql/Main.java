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