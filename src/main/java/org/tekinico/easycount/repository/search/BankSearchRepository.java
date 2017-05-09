package org.tekinico.easycount.repository.search;

import org.tekinico.easycount.domain.Bank;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bank entity.
 */
public interface BankSearchRepository extends ElasticsearchRepository<Bank, Long> {
}
