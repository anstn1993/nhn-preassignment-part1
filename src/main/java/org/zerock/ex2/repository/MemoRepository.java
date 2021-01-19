package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    //mno 값이 70부터 80사이에 해당하면서 mno를 기준으로 내림차순 정렬
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
    //위와 동일한 기능을 하는 메서드지만 정렬의 역할을 파라미터 pageable이 담당
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    void deleteByMnoLessThan(Long num);

    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getListDesc();

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
    int updateMemoText(@Param("memoText") String memoText, @Param("mno") Long mno);

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
    int updateMomoText(@Param("param") Memo memo);

    @Query(value = "select m from Memo m where m.mno < :mno",
    countQuery = "select count(m) from Memo m where m.mno < :mno")
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);

    //엔티티 타입의 데이터 이외에 선별적으로 추출하고 싶은 데이터가 있을 때 Object[] 타입으로 fetch할 수 있다.
    @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno",
    countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);

    @Query(value = "select * from memo where mno > 0", nativeQuery = true)
    List<Object[]> getNativeResult();
}
