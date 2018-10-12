package com.capgemini.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import com.capgemini.exceptionhandling.ResourceNotFoundException;
import com.capgemini.model.CommonFriendsListRequest;
import com.capgemini.model.CommonFriendsListResponse;
import com.capgemini.model.FriendListRequest;
import com.capgemini.model.Subscriber;
import com.capgemini.model.UserFriendsListResponse;
import com.capgemini.model.UserRequest;
import com.capgemini.repository.FriendMangmtRepo;
import com.capgemini.service.FrientMangmtService;
import com.capgemini.validation.FriendManagementValidation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendManagementControllerTest {
	FriendManagementController friendManagementController;
	private Subscriber subscriber;
	private UserRequest userRequest;
	private FriendListRequest friendListRequest;
	private CommonFriendsListRequest commonFriendsListRequest;
	private CommonFriendsListResponse commonFriendsListResponse;
	private UserFriendsListResponse userFriendsListResponse;
	@Mock
	private BindingResult result;
	FriendManagementValidation fmError;
	@Mock
	JdbcTemplate jdbcTemplate;
	@Mock
	FriendMangmtRepo friendMangmtRepo;
	@InjectMocks
	FrientMangmtService frndMngtServc;
	@Mock
	BindingResult bindingResult;

	@Before
	public void setUp() throws Exception {
		subscriber = new Subscriber();
		userRequest = new UserRequest();
		fmError = new FriendManagementValidation();
		friendManagementController = new FriendManagementController(frndMngtServc, fmError);
		friendListRequest = new FriendListRequest();
		commonFriendsListRequest = new CommonFriendsListRequest();
		commonFriendsListResponse = new CommonFriendsListResponse();
		userFriendsListResponse = new UserFriendsListResponse();
	}
	
	@Test
	public void test_getFriendList_success() throws ResourceNotFoundException {
		friendListRequest.setEmail("ranga@gmail.com");
		userFriendsListResponse.setStatus("Success");
		when(frndMngtServc.getFriendList(friendListRequest)).thenReturn(userFriendsListResponse);
		ResponseEntity<UserFriendsListResponse> responseEntity = friendManagementController
				.getFriendList(friendListRequest, bindingResult);
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
	}

	
}
