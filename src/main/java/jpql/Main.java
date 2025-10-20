package jpql;

import jakarta.persistence.*;

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
            /*Team team = new Team();
            team.setName("teamA");
            em.persist(team);
            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);


            em.flush();
            em.clear();*/

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

            //경로표현식
            // 상태필드 - 경로 탐색의 끝, 탐색 X
            /*String query = "select m.username From Member m";
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();
            // 단일값 연관경로 - 묵시적 내부조인(inner join) 발생, 탐색O
            //묵시적 내부조인 발생하므로 직관적이지 않음 - > 주의할것
            String query1 = "select m.team.name From Member m";
            List<String> result1 = em.createQuery(query1, String.class)
                    .getResultList();
            // 컬렉션값 연관 경로 - 묵시적 내부조인 발생, 탐색X
            String query2 = "select t.members From Team t";
            List<Collection> result2 = em.createQuery(query2, Collection.class)
                    .getResultList();
            // 컬렉션값 연관 경로 - 대안 -> 탐색 O -->>>> 묵시적 조인은 사용하지 말것... 명시적으로 할것
            String query3 = "select m.username From Team t join t.members m";
            List<String> result3 = em.createQuery(query3, String.class)
                    .getResultList();*/
            /** !!묵시적 조인은 사용하지 말것... 명시적으로 할것 **/

            /**
             * 페치조인(fetch join)
             * 회원조회하며 연관된 팀 함께조회(SQL한번)
             * jpql ) select m from Member m join fetch m.team
             * sql ) select m.*,t.* from member m inner join team t on m.team_id=t.id
             */

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);
            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(10);
            member1.changeTeam(teamA);
            em.persist(member1);
            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(10);
            member2.changeTeam(teamB);
            em.persist(member2);
            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(10);
            member3.changeTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            /*String query = "select m From Member m";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();
            for(Member m : result){
                System.out.println("m = " + m.getUsername() + ", " + m.getTeam().getName());
                //회원1, 팀A(SQL) //팀이 프록시 이므로 조회쿼리 발생
                //회원2, 팀B(SQL)
                //회원3, 팀B(1차캐시)
                //회원 100명 -> N+1문제 -> 페치조인 사용
            }*/

            //페치조인 사용
            /*String query = "select m From Member m join fetch m.team";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();
            for(Member m : result){
                System.out.println("m = " + m.getUsername() + ", " + m.getTeam().getName());
            }*/
            //컬렉션 페치 조인 - 일대다 관계
            /**!! Hibernate 6 부터 컬렉션 내의 중복값 처리가 자동으로 됨.
             * 6이전 -> TeamB에 멤버가 2명이면 Team 은 TeamA, TeamB, TeamB 세개가 리스트에 들어가 있음.
             * 이전 버전에서는 DISTINCT 로 제거.-> JPQL의 DISTINCT 는 엔티티 중복도 처리함.
             * */
            /*String query = "select distinct t from Team t join fetch t.members";
            List<Team> result = em.createQuery(query, Team.class)
                    .getResultList();
            for(Team t : result){
                System.out.println("t = " + t.getName() + ", " + t.getMembers().size());
                for(Member m : t.getMembers()){
                    System.out.println("m = " + m.getUsername());
                }
            }*/


            //페치 조인의 특징과 한계
            // 1.페치조인 대상에 별칭 X
            // 단, 정합성 이슈때문에 안되지만 페치조인 여러번 사용시 별칭 사용해야 할 수도 있기는 함.
            //String query = "select distinct t from Team t join fetch t.members m join fetch m.team";
            // 2. 둘이상 컬렉션 페치조인 불가


            // 3. 컬렉션을 페치조인시 페이징 API 사용불가 --->>>, 경고 로그 남기고 메모리에서 페이징(위험)
            // 단, 일대일, 다대일 단일 값 연관 필드들은 페이징 가능(데이터복제가 생기므로)
            /*String query = "select t from Team t join fetch t.members m";
            List<Team> result = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();
            *//**
             * !!!WARN: HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
             *//*
            System.out.println("result.size() = " + result.size());
            for(Team t : result){
                System.out.println("t = " + t.getName() + ", " + t.getMembers().size());
            }*/

            //해결 -> member 기준으로 join fetch
            /*String query1 = "select m from Member m join fetch m.team t";
            List<Team> result1 = em.createQuery(query1, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();
*/
            //해결 -> member 기준으로 join fetch
            /*String query = "select t from Team t";
            List<Team> result = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();
            for(Team team : result){
                System.out.println("team = |members= " + team.getName());
                for(Member member : team.getMembers()){
                    System.out.println("->member = " + member);
                }
            }*/

            //엔티티 직접 사용 - 기본 키 값 : 엔티티 직접 사용하면 SQL 에서 해당 엔티티 기본키 값을 사용
            //jpql1 ) select count(m.id) from Member m
            //jpql2 ) select count(m) from Member m
            // sql ) select count(m.id) as cnt from Member m; //1과 2는 동일한 sql 쿼리로 나옴

            //엔티티 직접 사용 - 파라미터에 사용
            /*String query1 = "select m from Member m where m = :member";
            Member result1 = em.createQuery(query1, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();
            System.out.println("result1 = " + result1.getUsername());
            String query2 = "select m from Member m where m.id = :memberId";
            Member result2 = em.createQuery(query2, Member.class)
                    .setParameter("memberId", member1.getId())
                    .getSingleResult();
            System.out.println("result2 = " + result2.getUsername());*/

            //엔티티 직접 사용 - 외래키 값
            String query1 = "select m from Member m where m.team = :team";
            List<Member> result = em.createQuery(query1, Member.class)
                    .setParameter("team", teamA)//id 로 주어도 동일함
                    .getResultList();
            for(Member m : result){
                System.out.println("m = " + m.getUsername());
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