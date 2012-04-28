package net.codestory.github;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;


public class AllIssuesTest {
    
    @Test
    public void should_retrieve_issue_creation_events() throws IOException {
        List<IssueEvent> issueEvents = new AllIssues("jsevellec", "cassandra-unit").list();

        assertThat(issueEvents).hasSize(11);
        IssueEvent firstEvent = issueEvents.get(0);
        assertThat(firstEvent.getUser()).isEqualTo("zznate");
        assertThat(firstEvent.getIssueNumber()).isEqualTo(1);
        assertThat(firstEvent.getAvatarUrl()).startsWith("https://secure.gravatar.com/avatar/dd82bef73b3c840320dca550ca8a1012");

    }
}
