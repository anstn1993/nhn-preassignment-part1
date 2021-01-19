package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource("classpath:/application-test.properties")
class MemoRepositoryTest {
    @Autowired
    private MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies() {
        IntStream.rangeClosed(0, 100).forEach(i -> {
            Memo memo = Memo.builder()
                    .memoText("Sample " + i)
                    .build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect() {
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("===============================");

        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Test
    @Transactional
    public void testSelect2() {
        Long mno = 99L;

        Memo memo = memoRepository.getOne(mno);

        System.out.println("============================");
        System.out.println(memo);
    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder()
                .mno(100L)
                .memoText("Update Text")
                .build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long id = 100L;
        memoRepository.deleteById(id);
    }

    @Test
    public void testPageDefault() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);

        System.out.println("---------------------");
        System.out.println("Total pages: " + result.getTotalPages());// 총 페이지
        System.out.println("Total count: " + result.getTotalElements());// 전체 데이터 수
        System.out.println("Page Number: " + result.getNumber());//현재 페이지 번호
        System.out.println("Page Size: " + result.getSize());//페이지당 데이터 수
        System.out.println("has Next Page?: " + result.hasNext());//다음 페이지 존재 여부
        System.out.println("first Page?: " + result.isFirst());//시작 페이지(0) 여부
    }

    @Test
    public void testSort() {
        Sort sort = Sort.by("mno").descending();//mno기준으로 내림차순 정렬
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testQueryMethods() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for (Memo memo : list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(70L, 80L, pageable);
        result.get().forEach(memo -> System.out.println(memo));
    }

    @Test
    @Transactional
    @Commit//트랜잭션 커밋
    public void testDeleteQueryMethods() {
        memoRepository.deleteByMnoLessThan(10L);
    }

    @Test
    public void testUpdateQueryMethods() {
        int count1 = memoRepository.updateMemoText("Updated text", 10L);
        assertThat(count1).isOne();

        Memo memo = Memo.builder()
                .mno(11L)
                .memoText("updated text")
                .build();
        int count2 = memoRepository.updateMomoText(memo);
        assertThat(count2).isOne();
    }

    @Test
    public void testPageQueryMethods() {
        Page<Memo> result = memoRepository.getListWithQuery(11L, PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isNotZero();
    }
}