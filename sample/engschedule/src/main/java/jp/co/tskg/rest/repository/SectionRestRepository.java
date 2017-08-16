package jp.co.example.rest.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import jp.co.example.entity.Section;

/**
 * spring-boot-starter-data-rest を使ったサンプル。
 * PagingAndSortingRepositoryを継承すると、Controllerなどを作らなくとも
 * 自動的に application/hal+json タイプのCRUDワンセットのAPIを生成する。
 *
 * @author user01
 *
 */
public interface SectionRestRepository extends PagingAndSortingRepository<Section, Long> {


}
