# My Awesome RealTime Chat!

This is a practical activity proposed by [Montreal Informatica](https://www.montreal.com.br/) as a challenge in they selection process.

The challenge is create an app that is possible to login with email and brazilian CPF and chat with some other person also logged in.

The tech stack premisses of this challenge are:

  * MVC Architecture;
  * JSF and Primefaces as frontend;
  * JDK 8 or higher;
  * JPA as persistence;
  * Oracle Express as database;
  * Sendgrid free account integration;

## How this works?
### App usage
1. User Login with Name, E-mail and brazilian CPF;
2. The app sends an e-mail to the user with information about login;
3. There is a public chat where anyone connected on the same server can text;
4. User can text to any user connected on the same server in private;
5. User is notified when send in a message words that are forbidden by the App.
6. User logout when decide to leave the chat.

### Simplified technical pipeline flow
1. App uses JSF and Primefaces as front-end, together with some javascript utilities and bootstrap as style;
2. When a User login the App uses springboot security to validate the credentials. If ok, app save in database user information and if is the first login, sends an e-mail to user with sendgrid api;
3. When login finish, the app connects to a socket to listen events about chat.
4. When user sends a message, before save and send to other connected users this messages is verified if has any forbidden word. If has, the message is not saved and is not send to other users. The user that try to send the message is notified about the fact.
5. If user send a valid message, this message is saved on database and send to everyone that is listen the correspondent socket.

## TODO list
### Backend
- [x] SetUp the tech stack; 
- [x] Create the login/security process;
- [x] Create the send e-mail process;
- [x] Create the persistence process;
- [x] Create the websocket process;
- [ ] Create the Unit tests;

### Frontend
- [x] SetUp the tech stack;
- [x] Create the login page;
- [x] Create the Chat page;
- [x] Create page Chat bean logic;
- [x] Integrate with Backend;
- [ ] Create the Unit Tests;

### Other Tasks
- [ ] Create a zero to final guide of this project;
- [ ] Modified the websocket process to get more reliable, improving security;

## Getting Started with this project
  Just clone this repo, install maven dependencies and run:

```bash
$ git clone https://github.com/gabrielnogueira/my-awesome-realtime-chat.git
$ cd my-awesome-video-player-and-converter
$ mvnw install
$ mvnw spring-boot:run
```

and enjoy!
