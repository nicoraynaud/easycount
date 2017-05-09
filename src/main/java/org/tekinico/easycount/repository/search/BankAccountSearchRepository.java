package org.tekinico.easycount.repository.search;

import org.tekinico.easycount.domain.BankAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BankAccount entity.
 */
public interface BankAccountSearchRepository extends ElasticsearchRepository<BankAccount, Long> {
}
