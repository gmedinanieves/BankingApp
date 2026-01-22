package learn.banking_app.domain;

import learn.banking_app.data.AccountRepository;
import learn.banking_app.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Result<Account> createAccount(Account account){
        Result<Account> result = validate(account);
        if (!result.isSuccess()) {
            return result;
        }

        if (account.getId() != null) {
            result.addMessage("Account ID must not be set for create", ResultType.INVALID);
            return result;
        }

        boolean duplicate = accountRepository
                .existsByAccountHolderNameIgnoreCaseAndEmailIgnoreCase(
                        account.getAccountHolderName(),
                        account.getEmail()
                );

        if (duplicate) {
            result.addMessage("An account with this name and email already exists", ResultType.INVALID);
            return result;
        }

        Account saved = accountRepository.save(account);
        result.setPayload(saved);
        return result;
    }

    // This method might return an Account… or it might return nothing.
    // If Account exists it returns Optional with Account inside
    // If Account doesn't exist it returns a container Optional.empty() but never null.
    // This helps prevent a NullPointerException if not found
    public Optional<Account> getAccount(Long id) {
        return accountRepository.findById(id);
    }

    public Result<Account> deposit (Long id, BigDecimal amount) {
        Result<Account> result = new Result<>();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            result.addMessage("Amount must be greater than zero", ResultType.INVALID);
            return result;
        }

        Optional<Account> optionalAccount = getAccount(id);
        if (optionalAccount.isEmpty()) {
            result.addMessage("Account not found", ResultType.NOT_FOUND);
            return result;
        }

        Account account = optionalAccount.get();
        account.setBalance(account.getBalance().add(amount)); // Balance + amount

        Account saved = accountRepository.save(account);
        result.setPayload(saved);
        return result;
    }

    public Result<Account> withdraw(Long id, BigDecimal amount) {
        Result<Account> result = new Result<>();

        Optional<Account> optionalAccount = getAccount(id);
        if (optionalAccount.isEmpty()) {
            result.addMessage("Account not found", ResultType.NOT_FOUND);
            return result;
        }
        Account account = optionalAccount.get();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            result.addMessage("Amount must be greater than zero", ResultType.INVALID);
            return result;
        } else if (account.getBalance().compareTo(amount) < 0) {
            result.addMessage("Insufficient funds", ResultType.INVALID);
            return result;
        }

        account.setBalance(account.getBalance().subtract(amount)); // Balance - amount

        Account saved = accountRepository.save(account);
        result.setPayload(saved);
        return result;
    }

    private Result<Account> validate(Account account) {
        Result<Account> result = new Result<>();

        if (account == null) {
            result.addMessage("Account cannot be null", ResultType.INVALID);
            return result;
        }

        if (account.getAccountHolderName() == null || account.getAccountHolderName().isBlank()) {
            result.addMessage("Account Holder Name is required", ResultType.INVALID);
            return result;
        } else if (account.getAccountHolderName().length() > 100) {
            result.addMessage("Account holder name is too long", ResultType.INVALID);
        }

        if (account.getEmail() == null || account.getEmail().isBlank()) {
            result.addMessage("Email is required", ResultType.INVALID);
            return result;
        } else if (!account.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            result.addMessage("Email format is invalid", ResultType.INVALID);
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            result.addMessage("Account balance cannot be negative", ResultType.INVALID);
        }

        return result;
    }

}
