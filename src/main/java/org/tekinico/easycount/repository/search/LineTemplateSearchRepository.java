package org.tekinico.easycount.repository.search;

import org.tekinico.easycount.domain.LineTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LineTemplate entity.
 */
public interface LineTemplateSearchRepository extends ElasticsearchRepository<LineTemplate, Long> {
}
