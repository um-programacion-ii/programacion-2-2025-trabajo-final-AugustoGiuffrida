package com.um.programacion2.trabajo_final.repository;

import com.um.programacion2.trabajo_final.domain.Venta;
import java.util.List;
import java.util.Optional;

import com.um.programacion2.trabajo_final.enumeration.EstadoVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Venta entity.
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query("select venta from Venta venta where venta.user.login = ?#{authentication.name}")
    List<Venta> findByUserIsCurrentUser();

    @Query("select distinct v from Venta v left join fetch v.evento left join fetch v.user where v.user.login = :login")
    List<Venta> findAllByUserLogin(@Param("login") String login);

    List<Venta> findByUser_Login(String login);

    List<Venta> findAllByEstadoVenta(EstadoVenta estadoVenta);


    default Optional<Venta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Venta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Venta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select venta from Venta venta left join fetch venta.evento left join fetch venta.user",
        countQuery = "select count(venta) from Venta venta"
    )
    Page<Venta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select venta from Venta venta left join fetch venta.evento left join fetch venta.user")
    List<Venta> findAllWithToOneRelationships();

    @Query("select venta from Venta venta left join fetch venta.evento left join fetch venta.user where venta.id =:id")
    Optional<Venta> findOneWithToOneRelationships(@Param("id") Long id);
}
