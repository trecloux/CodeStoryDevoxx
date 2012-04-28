package net.codestory.github;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class IssueEvent {
    private int issueNumber;
    private Date date;
    private String user;
    private String avatarUrl;
}
