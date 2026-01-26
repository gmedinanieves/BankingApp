package learn.banking_app.data;

import learn.banking_app.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountHolderNameIgnoreCase(String accountHolderName);

    boolean existsByEmailIgnoreCase(String email);

    /*
    Equivalent to
    select exists (
      select 1 from account
      where lower(account_holder_name) = lower(?)
    );

    and

    select exists (
      select 1 from account
      lower(email) = lower(?)
    );

    */

}
