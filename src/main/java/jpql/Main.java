package jpql;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

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
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            //Type Query : 반환타입 명확
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            //Query : 반환타입 명확하지 않음
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            //결과가 하나 이상 일경우 - 결과가 없으면 빈 리스트 반환
            List<Member> resultList = query1.getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }
            //단일값일 경우 - 결과가 정확히 하나.
            // 표준스펙 : 결과가 없으면 NoResultException, 결과가 둘이상이면 NonUniqueResultException 발생
            //Spring Data JPA -> 결과가 없으면 Null 이나 Optional
            Member memberA = query1.getSingleResult();

            //파라미터 바인딩
            TypedQuery<Member> query = em.createQuery("select m from Member m where m.username=:username", Member.class);
            query.setParameter("username", "member1");
            Member singleResult = query.getSingleResult();
            System.out.println("singleResult = "+ singleResult.getUsername());
            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
                            .setParameter("username", "member1")
                            .getSingleResult();
            System.out.println("singleResult = "+ singleResult.getUsername());


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