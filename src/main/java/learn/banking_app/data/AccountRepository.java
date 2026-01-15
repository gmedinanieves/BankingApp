package learn.banking_app.data;

import learn.banking_app.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
