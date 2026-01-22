package learn.banking_app.data;

import learn.banking_app.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountHolderNameIgnoreCaseAndEmailIgnoreCase(
            String accountHolderName,
            String email
    );

    /*
    Equivalent to
    select exists (
      select 1 from account
      where lower(account_holder_name) = lower(?) and lower(email) = lower(?)
    );

    */

}
