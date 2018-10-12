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

import com.capgemini.exceptionhandling.ResourceNotFoundException;
//import com.capgemini.model.BaseResponse;
import com.capgemini.model.CommonFriendsListResponse;
import com.capgemini.model.EmailsListRecievesUpdatesResponse;
import com.capgemini.model.Subscriber;
import com.capgemini.model.UserFriendsListResponse;
import com.capgemini.service.FrientMangmtService;
import com.capgemini.validation.FriendManagementValidation;

/**
 * @author vishwman
 *
 */
@RestController
@Validated
@EntityScan( basePackages = {"com.capgemini.entity"} )
@RequestMapping(value = "/test")
public class FriendManagementController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final String sharedKey = "SHARED_KEY";
	private static final String SUCCESS_STATUS = "Success";
	private static final String ERROR_STATUS = "error";
	private static final int CODE_SUCCESS = 100;
	private static final int AUTH_FAILURE = 102;

	//@Autowired
		public FrientMangmtService frndMngtServc;

		//@Autowired
		FriendManagementValidation fmError;

		@Autowired public FriendManagementController(FrientMangmtService frndMngtServc,FriendManagementValidation fmError) {
			this.frndMngtServc=frndMngtServc;
			this.fmError=fmError;
		}


	// need validation---------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<FriendManagementValidation> newFriendConnection(@Valid @RequestBody com.capgemini.model.UserRequest userReq, BindingResult results)throws ResourceNotFoundException {
		LOG.info("newFriendConnection :: ");
		FriendManagementValidation fmResponse = new FriendManagementValidation();
		//BaseResponse baseResponse = new BaseResponse();
		ResponseEntity<FriendManagementValidation> re = null;
		try{
			fmResponse = frndMngtServc.addNewFriendConnection(userReq)	;
			String isNewfrndMangmReqSuccess = fmResponse.getStatus();
					
			//LOG.info("newFriendConnection :: "+isNewfrndMangmReqSuccess);
			
			if(isNewfrndMangmReqSuccess.equalsIgnoreCase("Success")){
				fmResponse.setStatus(SUCCESS_STATUS);
				//response.setCode(CODE_SUCCESS);
				re =  new ResponseEntity<FriendManagementValidation>(fmResponse, HttpStatus.OK);
			} else {
				fmResponse.setStatus(ERROR_STATUS);
				//response.setCode(AUTH_FAILURE);
			}
			re =  new ResponseEntity<FriendManagementValidation>(fmResponse, HttpStatus.OK);

		}catch(Exception e) {
			LOG.error(e.getMessage());
			re =  new ResponseEntity<FriendManagementValidation>(fmResponse, HttpStatus.SERVICE_UNAVAILABLE);

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
	@RequestMapping(value = "/friendlist", method = RequestMethod.POST)
	public ResponseEntity<UserFriendsListResponse> getFriendList(@Valid @RequestBody com.capgemini.model.FriendListRequest friendListRequest, BindingResult result)throws ResourceNotFoundException {
		LOG.info("--getFriendList :: " +friendListRequest.getEmail());
		UserFriendsListResponse response = frndMngtServc.getFriendList(friendListRequest );
		ResponseEntity<UserFriendsListResponse> responseEntity = null;
		try{
			if(response.getStatus() == SUCCESS_STATUS){
				response.setStatus(SUCCESS_STATUS);
				responseEntity = new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.OK);
			} else {
				response.setStatus(ERROR_STATUS);
				responseEntity = new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.BAD_REQUEST);
				//response.setCode(AUTH_FAILURE);
			}
		}catch(Exception e) {
			LOG.error(e.getMessage());
			responseEntity =  new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);

		} 
		return responseEntity;


	}


	
	@RequestMapping(value = "/friends", method = RequestMethod.POST)

	public ResponseEntity<CommonFriendsListResponse> getCommonFriendList(@Valid @RequestBody com.capgemini.model.CommonFriendsListRequest  commonFrndReq) throws ResourceNotFoundException {	
		LOG.info("getCommonFriendList");
		ResponseEntity<CommonFriendsListResponse> responseEntity = null;
		CommonFriendsListResponse response = new CommonFriendsListResponse();
		try{
			response = frndMngtServc.RetrieveCommonFriendList(commonFrndReq.getFriends().get(0),commonFrndReq.getFriends().get(1) );


			if(response.getStatus() == SUCCESS_STATUS){
				response.setStatus(SUCCESS_STATUS);
				responseEntity = new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.OK);
			} else {
				response.setStatus(ERROR_STATUS);
				responseEntity = new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.BAD_REQUEST);
				//response.setCode(AUTH_FAILURE);
			}
		}catch(Exception e) {
			LOG.error(e.getMessage());
			responseEntity =  new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);

		} 
		return responseEntity;
	}




}
