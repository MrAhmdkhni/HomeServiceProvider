package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.person.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
