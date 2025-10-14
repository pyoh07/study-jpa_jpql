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
            for(int i=0;i<100;i++){
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }


            em.flush();
            em.clear();

            //JPA 페이징 API
            // setFirstResult(int startPosition) : 조회시작 위치(0부터시작)
            // setMaxResults(int maxResult) : 조회할 데이터 수
            List<Member> resultList = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();
            System.out.println("result.size = " + resultList.size());
            for(Member member1 : resultList){
                System.out.println("member1 = "+ member1);
            }


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