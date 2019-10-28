package seedu.algobase.logic.parser;

import static seedu.algobase.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.algobase.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.algobase.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.algobase.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.algobase.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.algobase.logic.parser.ParserUtil.parseDate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.algobase.logic.commands.FindPlanCommand;
import seedu.algobase.logic.parser.exceptions.ParseException;
import seedu.algobase.model.searchrule.plansearchrule.FindPlanDescriptor;
import seedu.algobase.model.searchrule.plansearchrule.Keyword;
import seedu.algobase.model.searchrule.plansearchrule.PlanDescriptionContainsKeywordsPredicate;
import seedu.algobase.model.searchrule.plansearchrule.PlanNameContainsKeywordsPredicate;
import seedu.algobase.model.searchrule.plansearchrule.TimeRange;
import seedu.algobase.model.searchrule.plansearchrule.TimeRangePredicate;


/**
 * Parses input arguments and creates a new FindPlanCommand object
 */
public class FindPlanCommandParser implements Parser {

    private List<String> getArgumentValueAsList(String argValue) {
        String trimmedArg = argValue.trim();
        String[] keywords = trimmedArg.split("\\s+");
        return Arrays.asList(keywords);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the FindPlanCommand
     * and returns a FindPlanCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FindPlanCommand parse(String args) throws ParseException {
        ArgumentMultimap argumentMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DESCRIPTION,
                        PREFIX_START_DATE, PREFIX_END_DATE);

        // According to the command format, no preamble should be present.
        if (!argumentMultimap.getPreamble().isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPlanCommand.MESSAGE_USAGE));
        }

        FindPlanDescriptor findPlanDescriptor = new FindPlanDescriptor();

        if (argumentMultimap.getValue(PREFIX_NAME).isPresent()) {
            List<String> planNameKeywords = getArgumentValueAsList(argumentMultimap.getValue(PREFIX_NAME).get());
            List<Keyword> keywords = planNameKeywords.stream().map(Keyword::new).collect(Collectors.toList());
            findPlanDescriptor.setPlanNamePredicate(new PlanNameContainsKeywordsPredicate(keywords));
        }

        if (argumentMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            List<String> planDescriptionKeywords =
                    getArgumentValueAsList(argumentMultimap.getValue(PREFIX_DESCRIPTION).get());
            List<Keyword> keywords = planDescriptionKeywords.stream().map(Keyword::new).collect(Collectors.toList());
            findPlanDescriptor.setPlanDescriptionPredicate(
                    new PlanDescriptionContainsKeywordsPredicate(keywords));
        }

        if (argumentMultimap.getValue(PREFIX_START_DATE).isPresent()
                || argumentMultimap.getValue(PREFIX_END_DATE).isPresent()) {
            if (argumentMultimap.getValue(PREFIX_START_DATE).isEmpty()
                    || argumentMultimap.getValue(PREFIX_END_DATE).isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TimeRange.MESSAGE_CONSTRAINTS));
            }
            String start = argumentMultimap.getValue(PREFIX_START_DATE).get();
            String end = argumentMultimap.getValue(PREFIX_END_DATE).get();
            try {
                LocalDate startDate = parseDate(start);
                LocalDate endDate = parseDate(end);
                TimeRange timeRange = new TimeRange(startDate, endDate);
                System.out.print(timeRange);
                findPlanDescriptor.setTimeRangePredicate(new TimeRangePredicate(timeRange));
            } catch (ParseException e) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPlanCommand.MESSAGE_USAGE), e);
            }
        }

        if (!findPlanDescriptor.isAnyFieldProvided()) {
            throw new ParseException(FindPlanCommand.MESSAGE_NO_CONSTRAINTS);
        }

        return new FindPlanCommand(findPlanDescriptor);
    }
}
