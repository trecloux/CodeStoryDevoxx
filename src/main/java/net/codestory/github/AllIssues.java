package net.codestory.github;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Ordering;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import java.io.IOException;
import java.util.*;

import static com.google.common.collect.Lists.transform;

public class AllIssues {
    private String owner;
    private String repository;

    public AllIssues(String owner, String repository) {
        this.owner = owner;
        this.repository = repository;
    }

    public List<IssueEvent> list() {
        List<Issue> allIssues = getAllIssues();
        List<IssueEvent> events = transform(allIssues, TO_EVENT);
        return sortIssueEvents(events);
    }

    private List<IssueEvent> sortIssueEvents(List<IssueEvent> events) {
        return EVENT_ORDERING.sortedCopy(events);
    }

    private final Function<Issue, IssueEvent> TO_EVENT = new Function<Issue, IssueEvent>() {
        public IssueEvent apply(Issue issue) {
            return new IssueEvent(issue.getNumber(), issue.getCreatedAt(), issue.getUser().getLogin(), issue.getUser().getAvatarUrl());
        }
    };

    private final Ordering<IssueEvent> EVENT_ORDERING = Ordering.natural().onResultOf(new Function<IssueEvent, Date>() {
        public Date apply(IssueEvent event) {
            return event.getDate();
        }
    });


    private List<Issue> getAllIssues() {
        List<Issue> allIssues = loadOpenIssues();
        allIssues.addAll(loadClosedIssues());
        return allIssues;
    }

    private List<Issue> loadOpenIssues() {
        return loasIssuesWithState(IssueService.STATE_OPEN);
    }

    private List<Issue> loadClosedIssues() {
        return loasIssuesWithState(IssueService.STATE_CLOSED);
    }

    private List<Issue> loasIssuesWithState(String state) {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(IssueService.FILTER_STATE, state);
        return loadIssuesWithFilter(filter);
    }

    private List<Issue> loadIssuesWithFilter(Map<String, String> filter) {
        IssueService issueService = new IssueService(new GitHubClient());
        try {
            return issueService.getIssues(owner, repository, filter);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
