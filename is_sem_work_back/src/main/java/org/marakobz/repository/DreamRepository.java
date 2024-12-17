package org.marakobz.repository;

import org.marakobz.model.Dream;
import org.marakobz.model.DreamUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

@Repository
public interface DreamRepository extends JpaRepository<Dream, Long>,  JpaSpecificationExecutor<Dream>{
    /*@Query("SELECT b FROM Dream b WHERE " +
            "(COALESCE(:username, NULL) IS NULL OR LOWER(b.creator.username) LIKE CONCAT('%', LOWER(CAST(:username as string)), '%')) AND" +
            "(COALESCE(:name, NULL) IS NULL OR LOWER(b.name) LIKE CONCAT('%', LOWER(CAST(:name as string)), '%')) AND" +
            "(COALESCE(:x, NULL) IS NULL OR b.coordinates.x = :x) AND " +
            "(COALESCE(:y, NULL) IS NULL OR b.coordinates.y = :y) AND " +
            "(COALESCE(:age, NULL) IS NULL OR b.age = :age) AND " +
            "(COALESCE(:creatureType, NULL) IS NULL OR b.creatureType = :creatureType) AND " +
            "(COALESCE(:locationName, NULL) IS NULL OR b.creatureLocation.name = :locationName) AND " +
            "(COALESCE(:area, NULL) IS NULL OR b.creatureLocation.area = :area) AND " +
            "(COALESCE(:population, NULL) IS NULL OR b.creatureLocation.population = :population) AND " +
            "(COALESCE(:governor, NULL) IS NULL OR b.creatureLocation.governor = :governor) AND " +
            "(COALESCE(:populationDensity, NULL) IS NULL OR b.creatureLocation.populationDensity = :populationDensity) AND " +
            "(COALESCE(:attackLevel, NULL) IS NULL OR b.attackLevel >= :attackLevel) AND " +
            "(COALESCE(:defenseLevel, NULL) IS NULL OR b.defenseLevel >= :defenseLevel) AND " +
            "(COALESCE(:ringName, NULL) IS NULL OR b.ring.name = :ringName) AND " +
            "(COALESCE(:ringPower, NULL) IS NULL OR b.ring.power = :ringPower) AND " +
            "(COALESCE(:ringWeight, NULL) IS NULL OR b.ring.weight = :ringWeight)")
    Page<Dream> findByFilters(@Param("username") String username,
                              @Param("name") String name,
                              @Param("x") Double x,
                              @Param("y") Double y,
                              @Param("age") Long age,
                              @Param("creatureType") BookCreatureType creatureType,
                              @Param("locationName") String locationName,
                              @Param("area") Float area,
                              @Param("population") Long population,
                              @Param("governor") BookCreatureType governor,
                              @Param("populationDensity") Double populationDensity,
                              @Param("attackLevel") Float attackLevel,
                              @Param("defenseLevel") Double defenseLevel,
                              @Param("ringName") String ringName,
                              @Param("ringPower") Float ringPower,
                              @Param("ringWeight") Float ringWeight,
                              Pageable pageable);

    */

    Optional<Dream> findById(Long id);

    List<Dream> findByCreator(DreamUser creator);
    boolean existsByNameAndCreator(String name, DreamUser creator);

    boolean existsByName(String name);
    List<Dream> findByName(String name);

}
