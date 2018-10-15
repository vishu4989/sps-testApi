package com.capgemini.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static com.capgemini.config.FriendManagementConstants.*;
import com.capgemini.exceptionhandling.ResourceNotFoundException;
//import com.capgemini.model.BaseResponse;
import com.capgemini.model.CommonFriendsListResponse;
import com.capgemini.model.EmailsListRecievesUpdatesResponse;
import com.capgemini.model.Subscriber;
import com.capgemini.model.UserFriendsListResponse;
import com.capgemini.service.FrientMangmtService;
import com.capgemini.validation.FriendManagementValidation;


@RestController
@Validated
@EntityScan(basePackages = { "com.capgemini.entity" })
@RequestMapping(value = "/test")
public class FriendManagementController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final String sharedKey = "SHARED_KEY";
	private static final String SUCCESS_STATUS = FRIENDMANAGEMENT_SUCCESS;
	private static final String ERROR_STATUS = "error";
	private static final int CODE_SUCCESS = 100;
	private static final int AUTH_FAILURE = 102;

	// @Autowired
	public FrientMangmtService frndMngtServc;

	// @Autowired
	FriendManagementValidation fmError;

	@Autowired
	public FriendManagementController(FrientMangmtService frndMngtServc, FriendManagementValidation fmError) {
		this.frndMngtServc = frndMngtServc;
		this.fmError = fmError;
	}

	// need validation---------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<FriendManagementValidation> newFriendConnection(
			@Valid @RequestBody com.capgemini.model.UserRequest userReq, BindingResult results)
			throws ResourceNotFoundException {
		LOG.info("newFriendConnection :: ");
		FriendManagementValidation fmResponse = new FriendManagementValidation();
		// BaseResponse baseResponse = new BaseResponse();
		ResponseEntity<FriendManagementValidation> re = null;
		try {
			fmResponse = frndMngtServc.addNewFriendConnection(userReq);
			String isNewfrndMangmReqSuccess = fmResponse.getStatus();

			// LOG.info("newFriendConnection :: "+isNewfrndMangmReqSuccess);

			if (isNewfrndMangmReqSuccess.equalsIgnoreCase(FRIENDMANAGEMENT_SUCCESS)) {
				fmResponse.setStatus(SUCCESS_STATUS);
				// response.setCode(CODE_SUCCESS);
				re = new ResponseEntity<FriendManagementValidation>(fmResponse, HttpStatus.OK);
			} else {
				fmResponse.setStatus(ERROR_STATUS);
				// response.setCode(AUTH_FAILURE);
			}
			re = new ResponseEntity<FriendManagementValidation>(fmResponse, HttpStatus.OK);

		} catch (Exception e) {
			LOG.error(e.getMessage());
			re = new ResponseEntity<FriendManagementValidation>(fmResponse, HttpStatus.SERVICE_UNAVAILABLE);

		}

		return re;

	}

	/**
	 * 
	 * @param friendListRequest
	 * @param result
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value = "/listoffriends", method = RequestMethod.POST)
	public ResponseEntity<UserFriendsListResponse> getFriendList(
			@Valid @RequestBody com.capgemini.model.FriendListRequest friendListRequest, BindingResult result)
			throws ResourceNotFoundException {
		LOG.info("--getFriendList :: " + friendListRequest.getEmail());
		UserFriendsListResponse response = frndMngtServc.getFriendList(friendListRequest);
		ResponseEntity<UserFriendsListResponse> responseEntity = null;
		try {
			if (response.getStatus() == SUCCESS_STATUS) {
				response.setStatus(SUCCESS_STATUS);
				responseEntity = new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.OK);
			} else {
				response.setStatus(ERROR_STATUS);
				responseEntity = new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.BAD_REQUEST);
				// response.setCode(AUTH_FAILURE);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			responseEntity = new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return responseEntity;

	}

	@RequestMapping(value = "/mutualfriends", method = RequestMethod.POST)

	public ResponseEntity<CommonFriendsListResponse> getCommonFriendList(
			@Valid @RequestBody com.capgemini.model.CommonFriendsListRequest commonFrndReq)
			throws ResourceNotFoundException {
		LOG.info("getCommonFriendList");
		ResponseEntity<CommonFriendsListResponse> responseEntity = null;
		CommonFriendsListResponse response = new CommonFriendsListResponse();
		try {
			response = frndMngtServc.RetrieveCommonFriendList(commonFrndReq.getFriends().get(0),
					commonFrndReq.getFriends().get(1));

			if (response.getStatus() == SUCCESS_STATUS) {
				response.setStatus(SUCCESS_STATUS);
				responseEntity = new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.OK);
			} else {
				response.setStatus(ERROR_STATUS);
				responseEntity = new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.BAD_REQUEST);
				// response.setCode(AUTH_FAILURE);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			responseEntity = new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return responseEntity;
	}

	/**
	 * @param subscriber
	 * @param result
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value = "/subscribe", method = RequestMethod.POST)
	public ResponseEntity<FriendManagementValidation> subscribeFriend(
			@Valid @RequestBody com.capgemini.model.Subscriber subscriber, BindingResult result)
			throws ResourceNotFoundException {
		LOG.info("subscribeFriend ::");
		// Validation
		if (result.hasErrors()) {
			return handleValidation(result);
		}

		ResponseEntity<FriendManagementValidation> responseEntity = null;
		FriendManagementValidation fmv = new FriendManagementValidation();

		try {
			fmv = frndMngtServc.subscribeTargetFriend(subscriber);
			if (fmv.getStatus() == SUCCESS_STATUS) {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.SERVICE_UNAVAILABLE);
		}

		return responseEntity;

	}

	/**
	 * 
	 */
	@RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
	public ResponseEntity<FriendManagementValidation> unSubscribeFriend(
			@Valid @RequestBody com.capgemini.model.Subscriber subscriber, BindingResult result)
			throws ResourceNotFoundException {
		// Validation
		ResponseEntity<FriendManagementValidation> responseEntity = null;
		/*
		 * if(result.hasErrors()) { return handleValidation(result); }
		 */
		boolean isValid = validateInput(subscriber);
		if (!isValid) {
			responseEntity = new ResponseEntity<FriendManagementValidation>(HttpStatus.BAD_REQUEST);
		}

		FriendManagementValidation fmv = null;
		try {
			fmv = frndMngtServc.unSubscribeTargetFriend(subscriber);
			if (fmv.getStatus() == SUCCESS_STATUS) {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.BAD_REQUEST);
		}

		return responseEntity;
	}

	private boolean validateInput(Subscriber subscriber) {
		String requestor = subscriber.getRequestor();
		String target = subscriber.getTarget();
		if (requestor == null || target == null || requestor.equalsIgnoreCase(target)) {
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/friends/updatelist", method = RequestMethod.POST)
	public ResponseEntity<EmailsListRecievesUpdatesResponse> emailListRecievesupdates(
			@Valid @RequestBody com.capgemini.model.EmailsListRecievesUpdatesRequest emailsList, BindingResult result)
			throws ResourceNotFoundException {

		LOG.info("emailListRecievesupdates ::");

		ResponseEntity<EmailsListRecievesUpdatesResponse> responseEntity = null;
		EmailsListRecievesUpdatesResponse response = new EmailsListRecievesUpdatesResponse();
		try {
			response = frndMngtServc.emailListRecievesupdates(emailsList);
			if (response.getStatus().toString() == SUCCESS_STATUS) {
				response.setStatus(SUCCESS_STATUS);
				responseEntity = new ResponseEntity<EmailsListRecievesUpdatesResponse>(response, HttpStatus.OK);
			} else {
				response.setStatus(ERROR_STATUS);
				responseEntity = new ResponseEntity<EmailsListRecievesUpdatesResponse>(response,
						HttpStatus.BAD_REQUEST);
				// response.setCode(AUTH_FAILURE);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			responseEntity = new ResponseEntity<EmailsListRecievesUpdatesResponse>(response,
					HttpStatus.SERVICE_UNAVAILABLE);

		}
		return responseEntity;

	}

	/**
	 * This method is used for client validation
	 * 
	 * @param result
	 * @return
	 */
	private ResponseEntity<FriendManagementValidation> handleValidation(BindingResult result) {
		fmError.setStatus(FRIENDMANAGEMENT_FAILED);
		if (result.getFieldError("requestor") != null && result.getFieldError("target") != null) {
			fmError.setDescription(result.getFieldError("requestor").getDefaultMessage() + " "
					+ result.getFieldError("target").getDefaultMessage());
		} else if (result.getFieldError("target") != null) {
			fmError.setDescription(result.getFieldError("target").getDefaultMessage());
		} else {
			fmError.setDescription(result.getFieldError("requestor").getDefaultMessage());

		}
		return new ResponseEntity<FriendManagementValidation>(fmError, HttpStatus.BAD_REQUEST);

	}


}
