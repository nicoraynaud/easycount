package org.tekinico.easycount.repository.search;

import org.tekinico.easycount.domain.Line;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Line entity.
 */
public interface LineSearchRepository extends ElasticsearchRepository<Line, Long> {
}
