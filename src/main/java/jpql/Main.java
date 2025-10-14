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
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            /*//entity 프로젝션
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                            .getResultList();
            Member findMember = result.get(0);
            findMember.setAge(20); //update 쿼리 발생함*/

            //아래의 방식은 Join 쿼리 발생함.
            /*List<Team> result = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();*/
            //Join 쿼리 명시적 표기 할것
            /*List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();*/

            //임베디드타입 프로젝션
            /*List<Address> result = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();*/

            //스칼라타입 프로젝션
            em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();

            //프로젝션 여러값 조회
            /*List resultList = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();
            Object o = resultList.get(0);
            Object[] result = (Object[]) o;
            System.out.println("result = " + result[0]);
            System.out.println("result = " + result[1]);*/

            //프로젝션 여러값 조회 2
            /*List<Object[]> resultList = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();
            Object[] result = resultList.get(0);
            System.out.println("result = " + result[0]);
            System.out.println("result = " + result[1]);*/

            //dto로 조회 : new 명령어로 패키지명 작성해야함
            List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();
            MemberDTO dto = resultList.get(0);
            System.out.println("dto.getUsername() = " + dto.getUsername());
            System.out.println("dto.getAge() = " + dto.getAge());



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