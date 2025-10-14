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