'use strict';

var stompClient = null;

$(document).ready(function() {
	connect();
});

function connect() {
    var userId = document.getElementById('userId').value.trim();

    if(userId) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({userId}, onConnected, onError);
    }
}

function onConnected() {
    stompClient.subscribe('/topic/public/contacts/update', onContactUpdate);
    stompClient.subscribe('/topic/public/contacts/new', onNewContact);
    stompClient.subscribe('/topic/public/message', onNewMessage);
//    stompClient.subscribe('/topic/private/message', onNewMessage);
}

function onContactUpdate(payload) {
	rcUpdateContacts([{name:"contactUpdate", value:payload.body}]);
}

function onNewContact(payload) {
	rcNewContact([{name:"newContact", value:payload.body}]);
}

function onNewMessage(payload) {
	rcReceiveNewMessage([{name:"newMessageReceived", value:payload.body}]);
}

function onError(error) {
    //do something
}

//function sendPublicMessage(event) {
//var messageContent = messageInput.value.trim();
//if(messageContent && stompClient) {
//  var chatMessage = {
//      sender: username,
//      content: messageInput.value,
//      type: 'CHAT'
//  };
//  stompClient.send("/chat/public/message", {}, JSON.stringify(chatMessage));
//  messageInput.value = '';
//}
//event.preventDefault();
//}
