package com.montreal.chat.service;

import static com.montreal.chat.common.Constants.TOPIC_PUBLIC_NEW_CONTACT;
import static com.montreal.chat.common.Constants.TOPIC_PUBLIC_NEW_MESSAGE;
import static com.montreal.chat.common.Constants.TOPIC_PUBLIC_UPDATE_CONTACT;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.montreal.chat.common.Status;
import com.montreal.chat.model.entity.Message;
import com.montreal.chat.model.entity.User;
import com.montreal.chat.model.repository.ForbiddenWordsRepository;
import com.montreal.chat.model.repository.MessageRepository;
import com.montreal.chat.model.repository.UserRepository;
import com.montreal.chat.view.dto.ChatMessageDTO;
import com.montreal.chat.view.dto.ChatUserDTO;

@Service
@Component
public class UserService {
	private static final String MESSAGE_CONTAINS_FORBIDDEN_WORDS = "Essa mensagem não pode ser enviada por que contém palavras proibidas";

	@Autowired
	UserRepository userRepository;

	@Autowired
	MessageRepository messageRepository;
	
	@Autowired
	ForbiddenWordsRepository forbiddenWordsRepository;

	@Autowired
	SimpMessagingTemplate webSocket;

	private Gson gsonInstance = new Gson();

	public ChatUserDTO doLogin(ChatUserDTO userDto) {
		User user;
		Optional<com.montreal.chat.model.entity.User> optUser = Optional
				.ofNullable(userRepository.findByCpfAndEmail(userDto.getCpf(), userDto.getEmail()));

		if (optUser.isPresent()) {
			user = optUser.get().withStatus(Status.ONLINE);
		} else {
			user = new User().withName(userDto.getName()).withCpf(userDto.getCpf()).withEmail(userDto.getEmail())
					.withStatus(Status.ONLINE);
		}

		ChatUserDTO savedUser = new ChatUserDTO(userRepository.save(user));

		sendMessage(TOPIC_PUBLIC_NEW_CONTACT,
				new ChatMessageDTO().fromUser(savedUser));

		return savedUser;
	}
	
	public ChatUserDTO getCurrentUser() {
			ChatUserDTO authUser = getAuthDetails();
			return authUser.withContacts(
					this.getContacts().stream().map(user -> new ChatUserDTO(user, Long.valueOf(authUser.getId()))).collect(toList()))
					.withMessages(this.getPublicMessages().stream().map(message -> new ChatMessageDTO().withContent(message.getContent()).fromUser(new ChatUserDTO(message.getFrom()))).collect(toList()));
	}

	public void updateUserStatus(String userId, String newStatus) {
		Optional<com.montreal.chat.model.entity.User> optUser = userRepository.findById(Long.parseLong(userId));

		optUser.ifPresent(user -> {
			userRepository.save(user.withStatus(Status.valueOf(newStatus)));
			sendMessage(TOPIC_PUBLIC_UPDATE_CONTACT,
					new ChatMessageDTO().fromUser(new ChatUserDTO(user)));
		});
	}

	public List<User> getContacts() {
		return userRepository.findAll().stream()
				.filter(user -> !String.valueOf(user.getId()).equals(getAuthDetails().getId()))
				.collect(Collectors.toList());
	}

	public void newMessage(String chatMessage, String idTo) throws Exception {
		if(forbiddenWordsRepository.findAll().stream().anyMatch(forbiddenMessage -> {
			return chatMessage.toLowerCase().contains(forbiddenMessage.getContent().toLowerCase());
		})) {
			throw new Exception(MESSAGE_CONTAINS_FORBIDDEN_WORDS);
		}
		
		Message message = new Message().withContent(chatMessage)
				.withFrom(new User(getAuthDetails()));
		
		if(idTo != null && idTo != "") {
			message.setTo(new User().withId(Long.parseLong(idTo)));
		}
		
		message = messageRepository.save(message);
		
		ChatMessageDTO messageToSend = new ChatMessageDTO().withContent(message.getContent()).fromUser(new ChatUserDTO(message.getFrom()));
		
		if(message.getTo() != null) {
			messageToSend.setUserTo(new ChatUserDTO(message.getTo()));
		}
		
		sendMessage(TOPIC_PUBLIC_NEW_MESSAGE, messageToSend);
	}
	private void sendMessage(String destination, ChatMessageDTO message) {
		String messagePayload = gsonInstance.toJson(message);
		webSocket.convertAndSend(destination, messagePayload);
	}

	public List<Message> getPublicMessages() {
		return messageRepository.findPublic();
	}

	private ChatUserDTO getAuthDetails() {
		return (ChatUserDTO) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

}
