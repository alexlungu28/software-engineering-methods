package roomscheduler.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import roomscheduler.communication.authorization.Authorization;
import roomscheduler.communication.authorization.Role;
import roomscheduler.entities.Rule;
import roomscheduler.repositories.RuleRepository;


@Controller // This means that this class is a Controller
public class RuleController {

    @Autowired
    private RuleRepository ruleRepository;

    final transient String authHeader = "Authorization";

    transient String errorMessage = "You do not have the privilege to perform this action.";

    public RuleRepository getRuleRepository() {
        return ruleRepository;
    }

    public void setRuleRepository(RuleRepository ruleRepo) {
        this.ruleRepository = ruleRepo;
    }

    /**
     * Create a new rule.
     *
     * @param token jwt token
     * @param r rule
     * @return saved if successful, error if otherwise
     * @throws IOException if something goes wrong
     */
    @PostMapping(path = "/createRule") // Map ONLY POST Requests
    public @ResponseBody String addNewRule(@RequestHeader(authHeader) String token,
                                           @RequestBody Rule r) throws IOException {
        if (!Authorization.authorize(token, Role.Teacher)) {
            throw new RuntimeException(errorMessage);
        }

        ruleRepository.saveAndFlush(r);
        return "Saved rule";
    }

    /**
     * Method for retrieving all rules.
     *
     * @return Array List with all rules
     */
    @GetMapping(path = "/allRules")
    public @ResponseBody List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    /**
     * Method for modifying a rule.
     *
     * @param r rule to be modified
     * @return message
     */
    @PutMapping(path = "/modifyRule")
    public @ResponseBody String editRule(@RequestHeader(authHeader) String token,
                                         @RequestBody Rule r) {
        if (!Authorization.authorize(token, Role.Teacher)) {
            throw new RuntimeException(errorMessage);
        }

        Optional<Rule> rule = ruleRepository.findById(r.getIdrules());
        if (rule.isPresent()) {
            rule.get().setName(r.getName());
            rule.get().setValue(r.getValue());
            ruleRepository.saveAndFlush(rule.get());
            return "Changed Rule";
        } else {
            throw new RuntimeException("Rule not found for the id " + r.getIdrules());
        }
    }

    /**
     * Method for deleting a rule.
     *
     * @param id id of the rule to be deleted
     * @return message
     */
    @DeleteMapping(path = "/deleteRule/{id}")
    public @ResponseBody String deleteRule(@RequestHeader(authHeader) String token,
                                           @PathVariable int id) {
        if (!Authorization.authorize(token, Role.Teacher)) {
            throw new RuntimeException(errorMessage);
        }

        Optional<Rule> rule = ruleRepository.findById(id);
        if (rule.isPresent()) {
            ruleRepository.deleteById(id);
            return "Deleted Rule";
        } else {
            throw new RuntimeException("Rule not found for the id " + id);
        }
    }

}



