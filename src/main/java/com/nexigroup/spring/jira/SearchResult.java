package com.nexigroup.spring.jira;

import lombok.ToString;

@ToString
public class SearchResult {
    private final int startIndex;
    private final int maxResults;
    private final int total;
    private final Iterable<Issue> issues;

    public SearchResult(int startIndex, int maxResults, int total, Iterable<Issue> issues) {
        this.startIndex = startIndex;
        this.maxResults = maxResults;
        this.total = total;
        this.issues = issues;
    }

    /**
     * @return 0-based start index of the returned issues (e.g. "3" means that 4th, 5th...maxResults issues matching given query
     * have been returned.
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * @return maximum page size (the window to results).
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * @return total number of issues (regardless of current maxResults and startIndex) matching given criteria.
     * Query JIRA another time with different startIndex to get subsequent issues
     */
    public int getTotal() {
        return total;
    }

    public Iterable<Issue> getIssues() {
        return issues;
    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof SearchResult) {
//            SearchResult that = (SearchResult) obj;
//            return Objects.equal(this.startIndex, that.startIndex)
//                    && Objects.equal(this.maxResults, that.maxResults)
//                    && Objects.equal(this.total, that.total)
//                    && Objects.equal(this.issues, that.issues);
//        }
//        return false;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(startIndex, maxResults, total, issues);
//    }

}
