package com.nexigroup.spring.jira;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.web.client.RestClientException;

import com.nexigroup.spring.jira.infrastructure.Response;
import com.nexigroup.spring.jira.infrastructure.RestConnectorJIRA;

public class IssueClient {
	
	private RestConnectorJIRA con;
	
	public IssueClient() {
		con = RestConnectorJIRA.getInstance();
	}


	/**
	 * Creates new issue.
	 *
	 * @param json populated with data to create new issue
	 * @return basicIssue with generated <code>issueKey</code>
	 * @throws Exception 
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 * @since com.atlassian.jira.rest.client.api 1.0, server 5.0
	 */
    public String updateAssignee(String issueKey, String assignee) throws Exception {
		String defectsUrl = con.buildUrl("rest/api/latest/issue/"+issueKey+"/assignee");
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Accept", "application/json");
		JSONObject json = new JSONObject();
		json.put("name", assignee);
		Response response = con.httpPut(defectsUrl, json.toString().getBytes(), map);
//		JSONObject result = new String(response.getResponseData());
		return new String(response.getResponseData());
    }

	public JSONObject updateIssue(String issueKey, JSONObject json) throws Exception {
		String defectsUrl = con.buildUrl("rest/api/latest/issue/"+issueKey);
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Accept", "application/json");
		Response response = con.httpPut(defectsUrl, json.toString().getBytes(), map);
		JSONObject result = new JSONObject(new String(response.getResponseData()));
		return result;
    }
    public JSONObject createIssue(JSONObject json) throws Exception {
    	
		String defectsUrl = con.buildUrl("rest/api/latest/issue");
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Accept", "application/json");

		Response response = con.httpPost(defectsUrl, json.toString().getBytes(), map);
		
		JSONObject result = new JSONObject(new String(response.getResponseData()));
		
		
    	return result;
    }

	/**
	 * Update an existing issue.
	 *
	 * @param issueKey issue key (like TST-1, or JRA-9)
	 * @param issue    populated with fields to set (no other verbs) in issue
	 * @return Void
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 * @since com.atlassian.jira.rest.client.api 3.0, server 5.0
	 */
//    Promise<Void> updateIssue(String issueKey, IssueInput issue);

	/**
	 * Retrieves CreateIssueMetadata with specified filters.
	 *
	 * @param options optional request configuration like filters and expandos. You
	 *                may use {@link GetCreateIssueMetadataOptionsBuilder} to build
	 *                them. Pass <code>null</code> if you don't want to set any
	 *                option.
	 * @return List of {@link CimProject} describing projects, issue types and
	 *         fields.
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 * @since com.atlassian.jira.rest.client.api 1.0, server 5.0
	 */
//    Promise<Iterable<CimProject>> getCreateIssueMetadata(@Nullable GetCreateIssueMetadataOptions options);

	/**
	 * Creates new issues in batch.
	 *
	 * @param issues populated with data to create new issue
	 * @return BulkOperationResult&lt;BasicIssues&gt; with generated
	 *         <code>issueKey</code> and errors for failed issues
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 * @since com.atlassian.jira.rest.client.api 2.0, server 6.0
	 */

//    Promise<BulkOperationResult<BasicIssue>> createIssues(Collection<IssueInput> issues);

//    Promise<Page<IssueType>> getCreateIssueMetaProjectIssueTypes(@Nonnull String projectIdOrKey, @Nullable Long startAt, @Nullable Integer maxResults);

//    Promise<Page<CimFieldInfo>> getCreateIssueMetaFields(@Nonnull String projectIdOrKey, @Nonnull String issueTypeId, @Nullable Long startAt, @Nullable Integer maxResults);

	/**
	 * Retrieves issue with selected issue key.
	 *
	 * @param issueKey issue key (like TST-1, or JRA-9)
	 * @return issue with given <code>issueKey</code>
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 */
//    Promise<Issue> getIssue(String issueKey);

	/**
	 * Retrieves issue with selected issue key, with specified additional expandos.
	 *
	 * @param issueKey issue key (like TST-1, or JRA-9)
	 * @param expand   additional expands. Currently CHANGELOG is the only supported
	 *                 expand that is not expanded by default.
	 * @return issue with given <code>issueKey</code>
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 * @since 0.6
	 */
//    Promise<Issue> getIssue(String issueKey, Iterable<Expandos> expand);

	/**
	 * Deletes issue with given issueKey. You can set {@code deleteSubtasks} to
	 * delete issue with subtasks. If issue have subtasks and {@code deleteSubtasks}
	 * is set to false, then issue won't be deleted.
	 *
	 * @param issueKey       issue key (like TST-1, or JRA-9)
	 * @param deleteSubtasks Determines if subtask of issue should be also deleted.
	 *                       If false, and issue has subtasks, then it won't be
	 *                       deleted.
	 * @return Void
	 * @since 2.0
	 */
//    Promise<Void> deleteIssue(String issueKey, boolean deleteSubtasks);

	/**
	 * Retrieves complete information (if the caller has permission) about watchers
	 * for selected issue.
	 *
	 * @param watchersUri URI of watchers resource for selected issue. Usually
	 *                    obtained by calling
	 *                    <code>Issue.getWatchers().getSelf()</code>
	 * @return detailed information about watchers watching selected issue.
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 * @see com.atlassian.jira.rest.client.api.domain.Issue#getWatchers()
	 */
//    Promise<Watchers> getWatchers(URI watchersUri);

	/**
	 * Retrieves complete information (if the caller has permission) about voters
	 * for selected issue.
	 *
	 * @param votesUri URI of voters resource for selected issue. Usually obtained
	 *                 by calling <code>Issue.getVotesUri()</code>
	 * @return detailed information about voters of selected issue
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 * @see com.atlassian.jira.rest.client.api.domain.Issue#getVotesUri()
	 */
//    Promise<Votes> getVotes(URI votesUri);

	/**
	 * Retrieves complete information (if the caller has permission) about
	 * transitions available for the selected issue in its current state.
	 *
	 * @param transitionsUri URI of transitions resource of selected issue. Usually
	 *                       obtained by calling
	 *                       <code>Issue.getTransitionsUri()</code>
	 * @return transitions about transitions available for the selected issue in its
	 *         current state.
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 */
//    Promise<Iterable<Transition>> getTransitions(URI transitionsUri);

	/**
	 * Retrieves complete information (if the caller has permission) about
	 * transitions available for the selected issue in its current state.
	 *
	 * @param issue issue
	 * @return transitions about transitions available for the selected issue in its
	 *         current state.
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 * @since v0.5
	 */
//    Promise<Iterable<Transition>> getTransitions(Issue issue);

	/**
	 * Performs selected transition on selected issue.
	 *
	 * @param transitionsUri  URI of transitions resource of selected issue. Usually
	 *                        obtained by calling
	 *                        <code>Issue.getTransitionsUri()</code>
	 * @param transitionInput data for this transition (fields modified, the
	 *                        comment, etc.)
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 */
//    Promise<Void> transition(URI transitionsUri, TransitionInput transitionInput);

	/**
	 * Performs selected transition on selected issue.
	 *
	 * @param issue           selected issue
	 * @param transitionInput data for this transition (fields modified, the
	 *                        comment, etc.)
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 * @since v0.5
	 */
//    Promise<Void> transition(Issue issue, TransitionInput transitionInput);

	/**
	 * Casts your vote on the selected issue. Casting a vote on already votes issue
	 * by the caller, causes the exception.
	 *
	 * @param votesUri URI of votes resource for selected issue. Usually obtained by
	 *                 calling <code>Issue.getVotesUri()</code>
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 */
//    Promise<Void> vote(URI votesUri);

	/**
	 * Removes your vote from the selected issue. Removing a vote from the issue
	 * without your vote causes the exception.
	 *
	 * @param votesUri URI of votes resource for selected issue. Usually obtained by
	 *                 calling <code>Issue.getVotesUri()</code>
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 */
//    Promise<Void> unvote(URI votesUri);

	/**
	 * Starts watching selected issue
	 *
	 * @param watchersUri URI of watchers resource for selected issue. Usually
	 *                    obtained by calling
	 *                    <code>Issue.getWatchers().getSelf()</code>
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 */
//    Promise<Void> watch(URI watchersUri);

	/**
	 * Stops watching selected issue
	 *
	 * @param watchersUri URI of watchers resource for selected issue. Usually
	 *                    obtained by calling
	 *                    <code>Issue.getWatchers().getSelf()</code>
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 */
//    Promise<Void> unwatch(URI watchersUri);

	/**
	 * Adds selected person as a watcher for selected issue. You need to have
	 * permissions to do that (otherwise the exception is thrown).
	 *
	 * @param watchersUri URI of watchers resource for selected issue. Usually
	 *                    obtained by calling
	 *                    <code>Issue.getWatchers().getSelf()</code>
	 * @param username    user to add as a watcher
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 */
//    Promise<Void> addWatcher(final URI watchersUri, final String username);

	/**
	 * Removes selected person from the watchers list for selected issue. You need
	 * to have permissions to do that (otherwise the exception is thrown).
	 *
	 * @param watchersUri URI of watchers resource for selected issue. Usually
	 *                    obtained by calling
	 *                    <code>Issue.getWatchers().getSelf()</code>
	 * @param username    user to remove from the watcher list
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, etc.)
	 */
//    Promise<Void> removeWatcher(final URI watchersUri, final String username);

	/**
	 * Creates link between two issues and adds a comment (optional) to the source
	 * issues.
	 *
	 * @param linkIssuesInput details for the link and the comment (optional) to be
	 *                        created
	 * @throws RestClientException in case of problems (connectivity, malformed
	 *                             messages, invalid argument, permissions, etc.)
	 * @since com.atlassian.jira.rest.client.api 0.2, server 4.3
	 */
//    Promise<Void> linkIssue(LinkIssuesInput linkIssuesInput);

	/**
	 * Uploads attachments to JIRA (adding it to selected issue)
	 *
	 * @param attachmentsUri where to upload the attachment. You can get this URI by
	 *                       examining issue resource first
	 * @param in             stream from which to read data to upload
	 * @param filename       file name to use for the uploaded attachment
	 * @since com.atlassian.jira.rest.client.api 0.2, server 4.3
	 */
//    Promise<Void> addAttachment(URI attachmentsUri, InputStream in, String filename);

	/**
	 * Uploads attachments to JIRA (adding it to selected issue)
	 *
	 * @param attachmentsUri where to upload the attachments. You can get this URI
	 *                       by examining issue resource first
	 * @param attachments    attachments to upload
	 * @since com.atlassian.jira.rest.client.api 0.2, server 4.3
	 */
//    Promise<Void> addAttachments(URI attachmentsUri, AttachmentInput... attachments);

	/**
	 * Uploads attachments to JIRA (adding it to selected issue)
	 *
	 * @param attachmentsUri where to upload the attachments. You can get this URI
	 *                       by examining issue resource first
	 * @param files          files to upload
	 * @since com.atlassian.jira.rest.client.api 0.2, server 4.3
	 */
//    Promise<Void> addAttachments(URI attachmentsUri, File... files);

	/**
	 * Adds a comment to JIRA (adding it to selected issue)
	 *
	 * @param commentsUri where to add comment
	 * @param comment     the {@link Comment} to add
	 * @since com.atlassian.jira.rest.client.api 1.0, server 5.0
	 */
//    Promise<Void> addComment(URI commentsUri, Comment comment);

	/**
	 * Retrieves the content of given attachment.
	 *
	 * @param attachmentUri URI for the attachment to retrieve
	 * @return stream from which the caller may read the attachment content (bytes).
	 *         The caller is responsible for closing the stream.
	 */
//	@Beta
//    Promise<InputStream> getAttachment(URI attachmentUri);

	/**
	 * Adds new worklog entry to issue.
	 *
	 * @param worklogUri   URI for worklog in issue
	 * @param worklogInput worklog input object to create
	 */
//    Promise<Void> addWorklog(URI worklogUri, WorklogInput worklogInput);

	/**
	 * Expandos supported by {@link IssueRestClient#getIssue(String, Iterable)}
	 */
	public enum Expandos {
		CHANGELOG("changelog"), OPERATIONS("operations"), SCHEMA("schema"), NAMES("names"), TRANSITIONS("transitions");

		private final String value;

		private Expandos(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

}
