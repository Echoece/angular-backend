package com.echo.backend.repository.tenant;

import com.echo.backend.entity.tenant.DefaultPackage;
import com.echo.backend.entity.tenant.DefaultPackageFeature;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefaultPackageFeatureRepository extends JpaRepository<DefaultPackageFeature, Long> {
    @EntityGraph(value = "DefaultPackageFeature.feature", type = EntityGraph.EntityGraphType.FETCH)
    List<DefaultPackageFeature> findAllByDefaultPackage(DefaultPackage defaultPackage);
    @EntityGraph(value = "DefaultPackageFeature.feature", type = EntityGraph.EntityGraphType.FETCH)
    List<DefaultPackageFeature> findAllByDefaultPackageId(Long id);
}
