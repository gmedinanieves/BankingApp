package learn.banking_app.domain;

import learn.banking_app.data.AccountRepository;
import learn.banking_app.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account){
        return accountRepository.save(account);
    }

    // This method might return an Account… or it might return nothing.
    // If Account exists it returns Optional with Account inside
    // If Account doesn't exist it returns a container Optional.empty() but never null.
    // This helps prevent a NullPointerException if not found
    public Optional<Account> getAccount(Long id) {
        return accountRepository.findById(id);
    }

    public Account deposit (Long id, double amount) {
        // Find account by id, if not found throw "Account not found" exception to the user
        Account account = getAccount(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    public Account withdraw(Long id, double amount) {
        // Find account by id, if not found throw "Account not found" exception to the user
        Account account = getAccount(id).orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

}
