package roomscheduler.controllers;

import roomscheduler.entities.Rule;
import roomscheduler.repositories.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;


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

    @PostMapping(path = "/createRule") // Map ONLY POST Requests
    public @ResponseBody
    String addNewRule(@RequestBody Rule r) throws IOException {
        ruleRepository.saveAndFlush(r);
        return "Saved rule";
    }

    /**
     * Method for retrieving all rules.
     *
     * @return Array List with all rules
     */
    @GetMapping(path = "/allrules")
    public @ResponseBody
    ArrayList<Rule> getAllRules() {
        Iterable<Rule> rules = ruleRepository.findAll();
        ArrayList<Rule> res = new ArrayList<>();
        Iterator<Rule> iterator = rules.iterator();
        while (iterator.hasNext()) {
            res.add(iterator.next());
        }
        return res;
    }

    /**
     * Method for modifying a rule.
     *
     * @param r rule to be modified
     * @return message
     */
    @PutMapping(path = "/modifyRule")
    public @ResponseBody
    String editRule(@RequestBody Rule r) {
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
    public @ResponseBody
    String deleteRule(@PathVariable int id) {
        Optional<Rule> rule = ruleRepository.findById(id);
        if (rule.isPresent()) {
            ruleRepository.deleteById(id);
            return "Deleted Rule";
        } else {
            throw new RuntimeException("Rule not found for the id " + id);
        }
    }

}



