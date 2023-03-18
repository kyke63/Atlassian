package com.nexigroup.spring.jira;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Issue {
	
    private final String key;
    private final Long id;
    
//    private final Status status;
//    private final IssueType issueType;
//    private final BasicProject project;
//    private final URI transitionsUri;
//    private final Iterable<String> expandos;
//    private final Collection<BasicComponent> components;
    private String summary;
    private String description;
    private String reporter;
    private String assignee;
    private Map<String, IssueField> issueFields;

//    private final Resolution resolution;
//    private final DateTime creationDate;
//    private final DateTime updateDate;
//    private final DateTime dueDate;
//    private final BasicPriority priority;
//    private final BasicVotes votes;
//    @Nullable
//    private final Collection<Version> fixVersions;
//    @Nullable
//    private final Collection<Version> affectedVersions;
//
//    private final Collection<Comment> comments;
//
//    @Nullable
//    private final Collection<IssueLink> issueLinks;
//
//    private final Collection<Attachment> attachments;
//
//    private final Collection<Worklog> worklogs;
//    private final BasicWatchers watchers;
//
//    @Nullable
//    private final TimeTracking timeTracking;
//    @Nullable
//    private final Collection<Subtask> subtasks;
//    @Nullable
//    private final Collection<ChangelogGroup> changelog;
//    @Nullable
//    private final Operations operations;
//    private final Set<String> labels;
    
    public Issue(String key, Long id) {
        this.key = key;
        this.id = id;
    }

	
	public static Issue parse (final JSONObject issueJson, Map<String, Map<String, String>> customField) {
//		System.out.println(issueJson.getJSONObject("fields").toString(3));
		Issue issue = new Issue(issueJson.getString("key"), issueJson.getLong("id"));
		JSONObject jsonFields = issueJson.getJSONObject("fields");
		
        final JSONObject commentsJson = jsonFields.optJSONObject("comment");
//        final Collection<Comment> comments = (commentsJson == null) ? Collections.<Comment>emptyList()
//                : parseArray(commentsJson, new JsonWeakParserForJsonObject<Comment>(commentJsonParser), "comments");

        issue.summary = jsonFields.getString("summary");
        issue.description = jsonFields.optString("description");
        final Map<String, IssueField> fields = new HashMap<>(jsonFields.length());
        jsonFields.toMap().forEach((key, value ) -> {
        	String realname = key;
	       	if (realname.startsWith("customfield_")) {
	    		realname = customField.get(realname).get("name");
	    	}

        	fields.put(realname, new IssueField(
        			key,
        			realname,
        			null,
        			value == JSONObject.NULL || value == null || "null".equals(value.toString()) ? null : value
        			));
        }); 
        issue.issueFields = fields;

//        final Collection<Attachment> attachments = parseOptionalArray(issueJson, new JsonWeakParserForJsonObject<Attachment>(attachmentJsonParser), FIELDS, ATTACHMENT_FIELD.id);
//        final Collection<IssueField> fields = parseFields(issueJson);
//
//        final IssueType issueType = issueTypeJsonParser.parse(getFieldUnisex(issueJson, ISSUE_TYPE_FIELD.id));
//        final DateTime creationDate = JsonParseUtil.parseDateTime(getFieldStringUnisex(issueJson, CREATED_FIELD.id));
//        final DateTime updateDate = JsonParseUtil.parseDateTime(getFieldStringUnisex(issueJson, UPDATED_FIELD.id));
//
//        final String dueDateString = getOptionalFieldStringUnisex(issueJson, DUE_DATE_FIELD.id);
//        final DateTime dueDate = dueDateString == null ? null : JsonParseUtil.parseDateTimeOrDate(dueDateString);
//
//        final BasicPriority priority = getOptionalNestedField(issueJson, PRIORITY_FIELD.id, priorityJsonParser);
//        final Resolution resolution = getOptionalNestedField(issueJson, RESOLUTION_FIELD.id, resolutionJsonParser);
        JSONObject tmp;
        tmp = jsonFields.optJSONObject("assignee");
        issue.assignee = (tmp == null || tmp == JSONObject.NULL || "null".equals(tmp.toString()) ? null : tmp.optString("name"));
        tmp = jsonFields.optJSONObject("reporter");
        issue.reporter = (tmp == null || tmp == JSONObject.NULL || "null".equals(tmp.toString()) ? null : tmp.optString("name"));

        
//
//        final BasicProject project = projectJsonParser.parse(getFieldUnisex(issueJson, PROJECT_FIELD.id));
//        final Collection<IssueLink> issueLinks;
//        issueLinks = parseOptionalArray(issueJson, new JsonWeakParserForJsonObject<IssueLink>(issueLinkJsonParserV5), FIELDS, LINKS_FIELD.id);
//
//        Collection<Subtask> subtasks = parseOptionalArray(issueJson, new JsonWeakParserForJsonObject<Subtask>(subtaskJsonParser), FIELDS, SUBTASKS_FIELD.id);
//
//        final BasicVotes votes = getOptionalNestedField(issueJson, VOTES_FIELD.id, votesJsonParser);
//        final Status status = statusJsonParser.parse(getFieldUnisex(issueJson, STATUS_FIELD.id));
//
//        final Collection<Version> fixVersions = parseOptionalArray(issueJson, new JsonWeakParserForJsonObject<Version>(versionJsonParser), FIELDS, FIX_VERSIONS_FIELD.id);
//        final Collection<Version> affectedVersions = parseOptionalArray(issueJson, new JsonWeakParserForJsonObject<Version>(versionJsonParser), FIELDS, AFFECTS_VERSIONS_FIELD.id);
//        final Collection<BasicComponent> components = parseOptionalArray(issueJson, new JsonWeakParserForJsonObject<BasicComponent>(basicComponentJsonParser), FIELDS, COMPONENTS_FIELD.id);
//
//        final Collection<Worklog> worklogs;
//        final URI selfUri = basicIssue.getSelf();
//
//        final String transitionsUriString;
//        if (issueJson.has(IssueFieldId.TRANSITIONS_FIELD.id)) {
//            Object transitionsObj = issueJson.get(IssueFieldId.TRANSITIONS_FIELD.id);
//            transitionsUriString = (transitionsObj instanceof String) ? (String) transitionsObj : null;
//        } else {
//            transitionsUriString = getOptionalFieldStringUnisex(issueJson, IssueFieldId.TRANSITIONS_FIELD.id);
//        }
//        final URI transitionsUri = parseTransisionsUri(transitionsUriString, selfUri);
//
//        if (JsonParseUtil.getNestedOptionalObject(issueJson, FIELDS, WORKLOG_FIELD.id) != null) {
//            worklogs = parseOptionalArray(issueJson,
//                    new JsonWeakParserForJsonObject<Worklog>(new WorklogJsonParserV5(selfUri)),
//                    FIELDS, WORKLOG_FIELD.id, WORKLOGS_FIELD.id);
//        } else {
//            worklogs = Collections.emptyList();
//        }


//        final BasicWatchers watchers = getOptionalNestedField(issueJson, WATCHER_FIELD.id, watchersJsonParser);
//        final TimeTracking timeTracking = getOptionalNestedField(issueJson, TIMETRACKING_FIELD.id, new TimeTrackingJsonParserV5());
//
//        final Set<String> labels = Sets
//                .newHashSet(parseOptionalArrayNotNullable(issueJson, jsonWeakParserForString, FIELDS, LABELS_FIELD.id));
//
//        final Collection<ChangelogGroup> changelog = parseOptionalArray(
//                issueJson, new JsonWeakParserForJsonObject<ChangelogGroup>(changelogJsonParser), "changelog", "histories");
//        final Operations operations = parseOptionalJsonObject(issueJson, "operations", operationsJsonParser);
//
//        return new Issue(summary, selfUri, basicIssue.getKey(), basicIssue.getId(), project, issueType, status,
//                description, priority, resolution, attachments, reporter, assignee, creationDate, updateDate,
//                dueDate, affectedVersions, fixVersions, components, timeTracking, fields, comments,
//                transitionsUri, issueLinks,
//                votes, worklogs, watchers, expandos, subtasks, changelog, operations, labels);
        
		
		return issue;
	}

}
