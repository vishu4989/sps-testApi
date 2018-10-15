package com.capgemini.repository;

import static com.capgemini.config.FriendManagementConstants.FRIENDMANAGEMENT_ALREADY_FRIEND;
import static com.capgemini.config.FriendManagementConstants.FRIENDMANAGEMENT_BLOCKED;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
@Component
public class FriendManagementHelper {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	JdbcTemplate jdbcTemplate;
		
	/**
	 * This method is invoked to insert new record in a friendmanagement table
	 * @param email
	 * @return
	 */
	public int insertEmail(String email) {
		return jdbcTemplate.update("insert into friendmanagement(email, friend_list, subscriber, subscribedBy, updated, updated_timestamp) values(?,?,?,?,?,?)",
				new Object[] { email,"" , "", "", "", 
						new Timestamp((new Date()).getTime()) });
	} 


	/**
	 * This method is used to get the all the email by ID
	 * @param friendListQueryParam
	 * @return
	 */
	public List<String> getEmailByIds(List<String> friendListQueryParam){

		StringJoiner email_Ids = new StringJoiner(",", "SELECT email FROM friendmanagement WHERE id in (", ")");

		for (String friendId : friendListQueryParam) {
			email_Ids.add(friendId);
		}
		String query = email_Ids.toString();
		return (List<String>) jdbcTemplate.queryForList(query, new Object[] {}, String.class);
	} 

	/**
	 * This method is used to get all the subscriber for an email
	 * @param email
	 * @return
	 */
	public String getSubscriptionList(String email) {
		String sqlrFriendList = "SELECT subscriber FROM friendmanagement WHERE email=?";
		String friendList = (String) jdbcTemplate.queryForObject(sqlrFriendList, new Object[] { email }, String.class);
		return friendList;
	}


	/**
	 * This method is used to get the subscribedBy Id for a particular email
	 * @param email
	 * @return
	 */
	public String getSubscribedByList(String email) {
		String sqlrFriendList = "SELECT subscribedBy FROM friendmanagement WHERE email=?";
		String friendList = (String) jdbcTemplate.queryForObject(sqlrFriendList, new Object[] { email }, String.class);
		return friendList;
	}


	/**
	 * This method is invoked to connect friend
	 * 
	 * @param firstEmail
	 * @param secondEmail
	 */
	public void connectFriend(String firstEmail, String secondEmail) {
		String requestorId = getId(firstEmail);
		String friendList = getFriendList(secondEmail);

		friendList = friendList.isEmpty() ? requestorId : friendList + "," + requestorId;

		jdbcTemplate.update("update friendmanagement " + " set friend_list = ?" + " where email = ?",
				new Object[] { friendList, secondEmail });
	}

	/**
	 * This method is invoked to check whether the friend is already connected
	 * 
	 * @param requestor
	 * @param target
	 * @return
	 */
	public boolean isAlreadyFriend(String requestor, String target) {
		boolean alreadyFriend = false;

		String requestorId = getId(requestor);
		String targetId = getId(target);

		String requestorFriendList = getFriendList(requestor);
		String[] requestorFriends = requestorFriendList.split(",");

		String targetFirendList = getFriendList(target);
		String[] targetFriends = targetFirendList.split(",");

		if (Arrays.asList(requestorFriends).contains(targetId) && Arrays.asList(targetFriends).contains(requestorId)) {
			alreadyFriend = true;
		}
		LOG.info(FRIENDMANAGEMENT_ALREADY_FRIEND + alreadyFriend);
		return alreadyFriend;

	}

	/**
	 * this method is invoked to get Id of particular email
	 * 
	 * @param email
	 * @return
	 */
	public String getId(String email) {
		String sql = "SELECT id FROM friendmanagement WHERE email=?";
		String requestorId = (String) jdbcTemplate.queryForObject(sql, new Object[] { email }, String.class);
		return requestorId;
	}

	/**
	 * This method is invoked to get the list of friends
	 * 
	 * @param email
	 * @return
	 */
	public String getFriendList(String email) {
		String sqlrFriendList = "SELECT friend_list FROM friendmanagement WHERE email=?";
		String friendList = (String) jdbcTemplate.queryForObject(sqlrFriendList, new Object[] { email }, String.class);
		return friendList;

	}

	/**
	 * This method is invoked to check whether the target is blocked or not
	 * 
	 * @param requestor_email
	 * @param target_email
	 * @return
	 */
	public boolean isBlocked(String requestor_email, String target_email) {
		boolean status = false;
		try {
			String sqlrFriendList = "SELECT Subscription_Status FROM unsubscribe WHERE Requestor_email=? AND Target_email=?";
			String Subscription_Status = (String) jdbcTemplate.queryForObject(sqlrFriendList,
					new Object[] { requestor_email, target_email }, String.class);
			LOG.info("Subscription_Status " + Subscription_Status);
			if (Subscription_Status.equalsIgnoreCase(FRIENDMANAGEMENT_BLOCKED)) {
				status = true;
			}
		} catch (Exception e) {

		}
		return status;
	}

	public void updateUnsubscribeTable(String requestor, String target,String status) {
		jdbcTemplate.update("insert into UNSUBSCRIBE(Requestor_email, Target_email, Subscription_Status) values(?, ?, ?)",
				new Object[] { requestor, target, status });
	}

	/**
	 * This method is used to update the subscribedBy column
	 * @param requestor
	 * @param target
	 */
	public void updateSubscribedBy(String requestor, String target) {
		String requestorId = getId(requestor);
		String subscribedList = getSubscribedByList(target);
		if (subscribedList.isEmpty()) {
			updateQueryForSubscribedBy(requestorId, target);
		}else {
			String[] subscr = subscribedList.split(",");
			ArrayList<String> subscrList = new ArrayList<String>(Arrays.asList(subscr));

			if (!subscrList.contains(requestorId)) {
				requestorId = subscribedList + "," + requestorId;
				updateQueryForSubscribedBy(requestorId, target);
			}
		}
	}

	public void updateQueryForSubscriber(String targetId, String requestor) {
		jdbcTemplate.update(
				"update friendmanagement " + " set subscriber = ? " + " where email = ?",
				new Object[] { targetId, requestor });
	}

	public void updateQueryForSubscribedBy(String requestorId, String target) {
		jdbcTemplate.update(
				"update friendmanagement " + " set subscribedBy = ? " + " where email = ?",
				new Object[] { requestorId, target });
	}

	public void updateQueryForUpdated(String recieverId, String sender) {
		jdbcTemplate.update(
				"update friendmanagement " + " set updated = ? " + " where email = ?",
				new Object[] { recieverId, sender });
	}
	public List<String> getListOfAllEmails(){
		final String query = "SELECT email FROM friendmanagement";
		final List<String> emails = jdbcTemplate.queryForList(query, String.class);
		return emails;
	}
}
