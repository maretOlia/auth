package giraffe.auth.repository;

import giraffe.auth.domain.GiraffeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@NoRepositoryBean
public interface GiraffeRepository<T extends GiraffeEntity> extends CrudRepository<T, String> {

    @Query("select t from #{#entityName} t where t.uuid = ?1 and t.status = ?2")
    T findByUuidAndStatus(String uuid, GiraffeEntity.Status status);

}
