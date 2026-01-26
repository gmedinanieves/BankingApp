package learn.banking_app.controllers;

import learn.banking_app.domain.AccountService;
import learn.banking_app.domain.Result;
import learn.banking_app.models.Account;
import learn.banking_app.models.AmountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Object> createAccount(@RequestBody Account account) {
        Result<Account> result = accountService.createAccount(account);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        Optional<Account> accountOptional = accountService.getAccount(id);

        if (accountOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        return ResponseEntity.ok(accountOptional.get()); // 200
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Object> deposit(@PathVariable Long id, @RequestBody AmountRequest request) {

        Result<Account> result = accountService.deposit(id, request.getAmount());

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }

        return ErrorResponse.build(result);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Object> withdraw(
            @PathVariable Long id,
            @RequestBody AmountRequest request) {

        Result<Account> result = accountService.withdraw(id, request.getAmount());

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }

        return ErrorResponse.build(result);
    }

}