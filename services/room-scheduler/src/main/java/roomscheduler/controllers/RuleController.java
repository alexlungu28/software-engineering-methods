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
import roomscheduler.entities.Rule;
import roomscheduler.repositories.RuleRepository;


@Controller // This means that this class is a Controller
public class RuleController {

    @Autowired
    private RuleRepository ruleRepository;

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
    public @ResponseBody String addNewRule(@RequestHeader("Authorization") String token,
                                           @RequestBody Rule r) throws IOException {
        if (Authorization.authorize(token, "Teacher")) {
            ruleRepository.saveAndFlush(r);
            return "Saved rule";
        } else {
            throw new RuntimeException("You do not have the privilege to perform " +
                    "this action.");
        }
    }

    /**
     * Method for retrieving all rules.
     *
     * @return Array List with all rules
     */
    @GetMapping(path = "/allRules")
    public @ResponseBody List<Rule> getAllRules(@RequestHeader("Authorization") String token) {
        if (Authorization.authorize(token, "Student")) {
            return ruleRepository.findAll();
        } else {
            throw new RuntimeException("You do not have the privilege to perform " +
                    "this action.");
        }
    }

    /**
     * Method for modifying a rule.
     *
     * @param r rule to be modified
     * @return message
     */
    @PutMapping(path = "/modifyRule")
    public @ResponseBody String editRule(@RequestHeader("Authorization") String token,
                                         @RequestBody Rule r) {
        if (Authorization.authorize(token, "Teacher")) {
            Optional<Rule> rule = ruleRepository.findById(r.getIdrules());
            if (rule.isPresent()) {
                rule.get().setName(r.getName());
                rule.get().setValue(r.getValue());
                ruleRepository.saveAndFlush(rule.get());
                return "Changed Rule";
            } else {
                throw new RuntimeException("Rule not found for the id " + r.getIdrules());
            }
        } else {
            throw new RuntimeException("You do not have the privilege to perform " +
                    "this action.");
        }
    }

    /**
     * Method for deleting a rule.
     *
     * @param id id of the rule to be deleted
     * @return message
     */
    @DeleteMapping(path = "/deleteRule/{id}")
    public @ResponseBody String deleteRule(@RequestHeader("Authorization") String token,
                                           @PathVariable int id) {
        if (Authorization.authorize(token, "Teacher")) {
            Optional<Rule> rule = ruleRepository.findById(id);
            if (rule.isPresent()) {
                ruleRepository.deleteById(id);
                return "Deleted Rule";
            } else {
                throw new RuntimeException("Rule not found for the id " + id);
            }
        } else {
            throw new RuntimeException("You do not have the privilege to perform " +
                    "this action.");
        }
    }

}



