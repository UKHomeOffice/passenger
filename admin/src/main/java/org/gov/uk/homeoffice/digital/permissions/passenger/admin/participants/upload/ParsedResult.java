package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.exceptions.ParseError;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;

import java.util.Collections;
import java.util.List;

public class ParsedResult {
    public final List<Tuple<Participant, Visa>> participants;
    public final List<ParseError> parseErrors;

    public ParsedResult(){
        this(Collections.emptyList(), Collections.emptyList());
    }

    public ParsedResult(List<Tuple<Participant, Visa>>  participants, List<ParseError> parseErrors) {
        this.participants = participants;
        this.parseErrors = parseErrors;
    }


    public ParsedResult withParticipants(List<Tuple<Participant, Visa>> participants) {
        return new ParsedResult(participants, parseErrors);
    }

    public ParsedResult withParseErrors( List<ParseError> parseErrors) {
        return new ParsedResult(participants, parseErrors);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParsedResult that = (ParsedResult) o;

        if (participants != null ? !participants.equals(that.participants) : that.participants != null)
            return false;
        return parseErrors != null ? parseErrors.equals(that.parseErrors) : that.parseErrors == null;
    }

    @Override
    public int hashCode() {
        int result = participants != null ? participants.hashCode() : 0;
        result = 31 * result + (parseErrors != null ? parseErrors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ParsedResult{" +
                "participant=" + participants +
                ", parseErrors=" + parseErrors +
                '}';
    }
}
